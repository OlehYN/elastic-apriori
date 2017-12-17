package com.ukma.bigdata.yupro.apriori.service.impl;

import com.ukma.bigdata.yupro.apriori.model.AprioriResult;
import com.ukma.bigdata.yupro.apriori.service.AprioriService;
import com.ukma.bigdata.yupro.apriori.service.AprioriStoreService;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GenericAprioriService
        implements AprioriService<Long, Long> {

    @Autowired
    private JoinService<Long, Long> joinService;
    @Autowired
    private PruneService<Long, Long> pruneService;
    @Autowired
    private FilterService<Long, Long> filterService;

    @Autowired
    private EvaluateService<Long, Long> evaluateService;
    @Autowired
    private AprioriStoreService<Long, Long> aprioriStoreService;

    @Autowired
    private TransportClient client;

    @Autowired
    private String indexName;

    @Override
    public AprioriResult<Long> generateAprioriResult(
            TransactionProvider<Long, Long> transactionProvider,
            int level, double support,
            double confidence) {

        // TODO prepare candidate-0 index using _search and aggs
        if (level == 0) {
            SearchResponse searchResponse = client
                    .prepareSearch(this.indexName)
                    .addAggregation(AggregationBuilders
                            .terms("agg0")
                            .field("transactionValues")
                            .size((int) aprioriStoreService.getSize())
                            .minDocCount((int) (support * aprioriStoreService.
                                    getSize())))
                    .get();
            searchResponse.getAggregations().asList().stream()
                    .forEach(a -> {
                        Set<Long> candidate = new HashSet<>();
                        candidate.add((Long) a.getMetaData().get("key"));
                        double candidiateSupport = ((double) a.getMetaData().
                                get("doc_count")) / aprioriStoreService.
                                getSize();
                        aprioriStoreService.updateCandidate(candidate, support);
                    });
        }

        for (int i = 0; i < level; i++) {
            if (level != 0) {
                evaluateService.evaluate(i);
            }
            if (level != 0) {
                filterService.filter(i, support);
            }
            joinService.join(i);
            pruneService.prune(i);
        }

        // TODO use aprioriStoreService to retrieve everything
        // TODO new service to form AssociationRules
        return null;
    }

}
