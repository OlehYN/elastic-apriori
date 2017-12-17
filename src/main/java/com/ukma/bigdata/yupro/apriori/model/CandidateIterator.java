package com.ukma.bigdata.yupro.apriori.model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.search.SearchHit;

public class CandidateIterator implements Iterator<Set<Long>> {

	private final TransportClient transportClient;
	private final Queue<Set<Long>> candidates;
	private final String indexName;
	private final Integer level;

	public CandidateIterator(TransportClient transportClient, Queue<Set<Long>> candidates, String indexName,
			Integer level) {
		this.transportClient = transportClient;
		this.candidates = candidates;
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
	public Set<Long> next() {
		if (this.candidates.isEmpty()) {
			provideCandidates();
		}
		return this.candidates.poll();
	}

	private void provideCandidates() {
		SearchResponse searchResponse = transportClient.prepareSearch(String.format("%s", this.indexName))
				.setScroll(new TimeValue(60000)).setSize(100).get();
		List<SearchHit> collection = Arrays.asList(searchResponse.getHits().getHits());
		collection.stream().forEach(s -> {
			List<Long> candidateSet = (List<Long>) s.getSourceAsMap().get("transactionValues");
			this.candidates.add(new HashSet<>(candidateSet));
		});
	}

}
