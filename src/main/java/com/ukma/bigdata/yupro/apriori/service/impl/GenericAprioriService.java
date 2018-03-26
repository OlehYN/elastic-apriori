package com.ukma.bigdata.yupro.apriori.service.impl;

import com.ukma.bigdata.yupro.apriori.service.AprioriService;
import com.ukma.bigdata.yupro.apriori.service.EvaluateService;
import com.ukma.bigdata.yupro.apriori.service.FilterService;
import com.ukma.bigdata.yupro.apriori.service.JoinService;
import com.ukma.bigdata.yupro.apriori.service.PruneService;
import com.ukma.bigdata.yupro.apriori.service.TransactionProvider;
import java.util.HashSet;
import java.util.Set;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class GenericAprioriService implements AprioriService<Long, Long> {

    @Autowired
    private JoinService<Long, Long> joinService;

    @Autowired
    private PruneService<Long, Long> pruneService;

    @Autowired
    private FilterService<Long, Long> filterService;

    @Autowired
    private EvaluateService<Long, Long> evaluateService;

    @Autowired
    private ElasticAprioriStoreService aprioriStoreService;

    @Autowired
    private TransportClient client;

    @Autowired
    @Qualifier("transactionIndexName")
    private String indexName;

    @Autowired
    @Qualifier("candidateIndexName")
    private String candidateIndexName;

    @Override
    public void generateAprioriResult(TransactionProvider<Long, Long> transactionProvider, int level, double support,
	    double confidence) {
	for (int i = 0; i < level; i++)
	    proceedLevel(support, i, level);

    }

    @Override
    public void proceedLevel(double support, int level, int maxLevel) {
	if (level == 0) {
	    SearchResponse searchResponse = client.prepareSearch(this.indexName)
		    .addAggregation(AggregationBuilders.terms("agg0").field("transactionValues")
			    .size((int) aprioriStoreService.getSize())
			    .minDocCount((int) (support * aprioriStoreService.getSize())))
		    .get();
	    (((LongTerms) searchResponse.getAggregations().get("agg0")).getBuckets()).stream().forEach(a -> {
		Set<Long> candidate = new HashSet<>();
		candidate.add((Long) a.getKey());
		double candidiateSupport = ((double) a.getDocCount()) / aprioriStoreService.getSize();
		aprioriStoreService.saveCandidate(candidate, candidiateSupport);
	    });
	    aprioriStoreService.flush();
	    aprioriStoreService.getClient().admin().indices().prepareRefresh().get();
	}

	if (level > 1) {
	    pruneService.prune(level);
	    System.out.println("pruned...");
	}
	if (level != 0) {
	    evaluateService.evaluate(level);
	    System.out.println("evaluated...");
	}
	if (level != 0) {
	    filterService.filter(level, support);
	    System.out.println("filtered...");
	}
	if (level != maxLevel - 1) {
	    joinService.join(level);
	    System.out.println("joined...");
	}

    }

}
