package com.ukma.bigdata.yupro.apriori.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.search.SearchHit;

public class FrequentSetIterator implements Iterator<FrequentSet<Long>> {

    private final TransportClient transportClient;
    private final Queue<FrequentSet<Long>> candidates;
    private final String indexName;
    private final Integer level;
    private String scrollId;

    public FrequentSetIterator(TransportClient transportClient, String indexName, Integer level) {
	this.transportClient = transportClient;
	this.candidates = new LinkedList<>();
	this.indexName = indexName;
	this.level = level;
    }

    @Override
    public boolean hasNext() {
	if (this.candidates.isEmpty()) {
	    provideCandidates();
	}
	return !this.candidates.isEmpty();
    }

    @Override
    public FrequentSet<Long> next() {
	if (this.candidates.isEmpty()) {
	    provideCandidates();
	}
	return this.candidates.poll();
    }

    private void provideCandidates() {
	SearchResponse searchResponse;
	if (scrollId == null) {
	    searchResponse = transportClient.prepareSearch(String.format("%s%s", this.indexName, this.level))
		    .setScroll(new TimeValue(600000)).setSize(100).get();
	    this.scrollId = searchResponse.getScrollId();
	} else {
	    searchResponse = transportClient.prepareSearchScroll(scrollId).setScroll(new TimeValue(600000)).get();
	    this.scrollId = searchResponse.getScrollId();
	}

	List<SearchHit> collection = Arrays.asList(searchResponse.getHits().getHits());
	collection.stream().forEach(searchHit -> {
	    List<Integer> intTransactionValues = (List<Integer>) searchHit.getSourceAsMap().get("transactionValues");
	    Double support = (Double) searchHit.field("support").getValue();
	    List<Long> transactionValues = new ArrayList<>();
	    Arrays.stream(intTransactionValues.stream().mapToLong(i -> i).toArray())
		    .forEach(lValue -> transactionValues.add(lValue));
	    FrequentSet<Long> frequentSet = new FrequentSet<>();
	    frequentSet.setId(searchHit.getId());
	    frequentSet.setSupport(support);
	    frequentSet.setItems(transactionValues.stream().collect(Collectors.toSet()));
	    this.candidates.add(frequentSet);
	});
    }
}
