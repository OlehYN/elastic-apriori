package com.ukma.bigdata.yupro.apriori.service.impl.elastic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.elasticsearch.action.search.MultiSearchRequestBuilder;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.MultiSearchResponse.Item;
import org.elasticsearch.client.transport.TransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ukma.bigdata.yupro.apriori.model.ItemSet;
import com.ukma.bigdata.yupro.apriori.service.EvaluateService;
import com.ukma.bigdata.yupro.apriori.service.impl.ElasticAprioriStoreService;

import org.springframework.stereotype.Service;

@Service
public class EvaluateServiceImpl implements EvaluateService<Long, Long> {

    @Autowired
    private ElasticAprioriStoreService aprioriStoreService;

    @Autowired
    @Qualifier("transactionIndexName")
    private String transactionIndex;

    private static final Logger LOG = LoggerFactory.getLogger(EvaluateServiceImpl.class);

    @Override
    public void evaluate(int level) {
	// Iterator<ItemSet> iterator =
	// aprioriStoreService.candidateIterator(level);
	//
	// while (iterator.hasNext()) {
	// ItemSet itemSet = iterator.next();
	// double support = ((double)
	// aprioriStoreService.countTransactions(itemSet.getItemSet()))
	// / ((double) aprioriStoreService.getSize());
	// aprioriStoreService.updateCandidate(itemSet.getId(),
	// itemSet.getItemSet(), support);
	// }
	//
	// aprioriStoreService.flush();
	// aprioriStoreService.getClient().admin().indices().prepareRefresh().get();

	// Iterator<ItemSet> iterator =
	// aprioriStoreService.candidateIterator(level);
	// TransportClient transportClient = aprioriStoreService.getClient();
	// SearchRequestBuilder searchRequestBuilder =
	// transportClient.prepareSearch(transactionIndex)
	// .setIndicesOptions(IndicesOptions.strictExpand());
	//
	// List<String> ids = new ArrayList<>();
	//
	// int count = 0;
	// final int maxCount = 5000;
	// while (iterator.hasNext()) {
	// ItemSet itemSet = iterator.next();
	// searchRequestBuilder.addAggregation(AggregationBuilders.filter(itemSet.getId(),
	// aprioriStoreService.generateQuery(itemSet.getItemSet())));
	// ids.add(itemSet.getId());
	//
	// ++count;
	// if (maxCount == count || (!iterator.hasNext() && count != 0)) {
	//
	// System.out.println(searchRequestBuilder);
	// SearchResponse searchResponse = searchRequestBuilder.get();
	// System.out.println(searchResponse);
	//
	// searchRequestBuilder =
	// transportClient.prepareSearch(transactionIndex)
	// .setIndicesOptions(IndicesOptions.strictExpand());
	//
	// for (String id : ids) {
	// long docCount = ((LongTerms)
	// searchResponse.getAggregations().get(id)).getBuckets().get(0)
	// .getDocCount();
	// double support = ((double) docCount) / ((double)
	// aprioriStoreService.getSize());
	// aprioriStoreService.updateCandidate(itemSet.getId(),
	// itemSet.getItemSet(), support);
	// }
	//
	// count = 0;
	// ids.clear();
	// }
	// }
	//
	// aprioriStoreService.flush();
	// aprioriStoreService.getClient().admin().indices().prepareRefresh().get();

	LOG.info("evaluate(): level " + level);
	int count = 0;
	Iterator<ItemSet> iterator = aprioriStoreService.candidateIterator(level);
	TransportClient transportClient = aprioriStoreService.getClient();
	MultiSearchRequestBuilder multiSearchRequestBuilder = transportClient.prepareMultiSearch();

	List<ItemSet> ids = new ArrayList<>();

	int bulkCount = 0;
	final int maxCount = 5000;
	while (iterator.hasNext()) {
	    ItemSet itemSet = iterator.next();
	    multiSearchRequestBuilder.add(aprioriStoreService.getCountTransactionsRequest(itemSet.getItemSet()));
	    ids.add(itemSet);

	    ++count;
	    ++bulkCount;
	    if (maxCount == bulkCount || (!iterator.hasNext() && bulkCount != 0)) {
		MultiSearchResponse searchResponse = multiSearchRequestBuilder.get();
		for (int i = 0; i < searchResponse.getResponses().length; i++) {
		    Item item = searchResponse.getResponses()[i];
		    long itemCount = item.getResponse().getHits().getTotalHits();
		    ItemSet processingItemSet = ids.get(i);

		    double support = ((double) itemCount) / ((double) aprioriStoreService.getSize());
		    aprioriStoreService.updateCandidate(processingItemSet.getId(), processingItemSet.getItemSet(),
			    support);
		    LOG.debug("evaluate(): " + support + " for " + processingItemSet);
		}

		multiSearchRequestBuilder = transportClient.prepareMultiSearch();
		bulkCount = 0;
		ids.clear();
	    }
	}

	LOG.info("evaluate(): evaluated " + count);
	aprioriStoreService.flush();
	aprioriStoreService.getClient().admin().indices().prepareRefresh().get();
    }
}
