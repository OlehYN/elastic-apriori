package com.ukma.bigdata.yupro.apriori.service.impl;

import com.ukma.bigdata.yupro.apriori.model.AprioriResult;
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
    public AprioriResult<Long> generateAprioriResult(TransactionProvider<Long, Long> transactionProvider, int level,
	    double support, double confidence) {

	for (int i = 0; i < level; i++) {
	    if (i == 0) {
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
	    if (i > 1) {
		pruneService.prune(i);
		System.out.println("pruned...");
	    }
	    if (i != 0) {
		evaluateService.evaluate(i);
		System.out.println("evaluated...");
	    }
	    if (i != 0) {
		filterService.filter(i, support);
		System.out.println("filtered...");
	    }
	    if (i != level - 1) {
		joinService.join(i);
		System.out.println("joined...");
	    }
	}

	// TODO use aprioriStoreService to retrieve everything
	// TODO new service to form AssociationRules
	return null;
    }

}
