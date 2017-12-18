package com.ukma.bigdata.yupro.apriori.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ukma.bigdata.yupro.apriori.model.ItemSet;
import com.ukma.bigdata.yupro.apriori.service.EvaluateService;
import org.springframework.stereotype.Service;

@Service
public class ElasticEvaluateServiceImpl implements EvaluateService<Long, Long> {

    @Autowired
    private ElasticAprioriStoreService aprioriStoreService;

    @Autowired
    @Qualifier("transactionIndexName")
    private String transactionIndex;

    @Override
    public void evaluate(int level) {
	Iterator<ItemSet> iterator = aprioriStoreService.candidateIterator(level);

	while (iterator.hasNext()) {
	    ItemSet itemSet = iterator.next();
	    double support = ((double) aprioriStoreService.countTransactions(itemSet.getItemSet()))
		    / ((double) aprioriStoreService.getSize());
	    aprioriStoreService.updateCandidate(itemSet.getId(), itemSet.getItemSet(), support);
	}

	aprioriStoreService.flush();
	aprioriStoreService.getClient().admin().indices().prepareRefresh().get();

//	Iterator<ItemSet> iterator = aprioriStoreService.candidateIterator(level);
//	TransportClient transportClient = aprioriStoreService.getClient();
//	SearchRequestBuilder searchRequestBuilder = transportClient.prepareSearch(transactionIndex)
//		.setIndicesOptions(IndicesOptions.strictExpand());
//
//	List<String> ids = new ArrayList<>();
//
//	int count = 0;
//	final int maxCount = 5000;
//	while (iterator.hasNext()) {
//	    ItemSet itemSet = iterator.next();
//	    searchRequestBuilder.addAggregation(AggregationBuilders.filter(itemSet.getId(),
//		    aprioriStoreService.generateQuery(itemSet.getItemSet())));
//	    ids.add(itemSet.getId());
//
//	    ++count;
//	    if (maxCount == count || (!iterator.hasNext() && count != 0)) {
//
//		System.out.println(searchRequestBuilder);
//		SearchResponse searchResponse = searchRequestBuilder.get();
//		System.out.println(searchResponse);
//
//		searchRequestBuilder = transportClient.prepareSearch(transactionIndex)
//			.setIndicesOptions(IndicesOptions.strictExpand());
//
//		for (String id : ids) {
//		    long docCount = ((LongTerms) searchResponse.getAggregations().get(id)).getBuckets().get(0)
//			    .getDocCount();
//		    double support = ((double) docCount) / ((double) aprioriStoreService.getSize());
//		    aprioriStoreService.updateCandidate(itemSet.getId(), itemSet.getItemSet(), support);
//		}
//
//		count = 0;
//		ids.clear();
//	    }
//	}
//
//	aprioriStoreService.flush();
//	aprioriStoreService.getClient().admin().indices().prepareRefresh().get();
    }
}
