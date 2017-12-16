package com.ukma.bigdata.yupro.apriori.service.impl;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import com.ukma.bigdata.yupro.apriori.model.FrequentSet;
import com.ukma.bigdata.yupro.apriori.model.Transaction;
import com.ukma.bigdata.yupro.apriori.service.AprioriStoreService;
import com.ukma.bigdata.yupro.apriori.service.TransactionProvider;

import static org.elasticsearch.common.xcontent.XContentFactory.*;

public class ElasticAprioriStoreService implements AprioriStoreService<Long, Long> {

	private TransportClient client;

	private String candidateIndexName;
	private String transactionIndexName;

	private static final String CANDIDATE_TYPE = "candidate";
	private static final String TRANSACTION_TYPE = "transaction";

	private static final int transactionBufferSize = 5000;

	private long size = 0;

	public ElasticAprioriStoreService(String hostName, int port, String candidateIndexName, String transactionIndexName,
			TransactionProvider<Long, Long> transactionProvider)
			throws IOException, InterruptedException, ExecutionException {
		client = new PreBuiltTransportClient(Settings.EMPTY)
				.addTransportAddress(new TransportAddress(InetAddress.getByName(hostName), port));

		this.candidateIndexName = candidateIndexName;
		this.transactionIndexName = transactionIndexName;

		Transaction<Long, Long> transaction = transactionProvider.nextTransaction();
		BulkRequestBuilder bulkRequest = client.prepareBulk();

		int bulkSize = 0;
		if (!client.admin().indices().exists(new IndicesExistsRequest(transactionIndexName)).get().isExists()) {
			do {
				if (bulkSize == transactionBufferSize) {
					bulkSize = 0;
					BulkResponse bulkResponse = bulkRequest.get();
					if (bulkResponse.hasFailures()) {
						throw new IllegalArgumentException(bulkResponse.buildFailureMessage());
					}
					bulkRequest = client.prepareBulk();
				}

				bulkRequest.add(client
						.prepareIndex(transactionIndexName, TRANSACTION_TYPE,
								String.valueOf(transaction.getTransactionKey()))
						.setSource(jsonBuilder().startObject().field("transactionId", transaction.getTransactionKey())
								.field("transactionValues", transaction.getTransactionValue()).endObject()));

				++bulkSize;
			} while ((transaction = transactionProvider.nextTransaction()) != null);

			if (bulkSize != 0) {
				BulkResponse bulkResponse = bulkRequest.get();
				if (bulkResponse.hasFailures()) {
					throw new IllegalArgumentException(bulkResponse.buildFailureMessage());
				}
				bulkRequest = client.prepareBulk();
			}
		}
		size = (int) client.prepareSearch(this.transactionIndexName).setIndicesOptions(IndicesOptions.strictExpand())
				.setSize(0).execute().get().getHits().getTotalHits();
	}

	@Override
	public Iterator<Set<Long>> candidateIterator(int level) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<FrequentSet<Long>> frequentSetIterator(int level) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeCandidate(Set<Long> itemSet) {
		SearchHits searchHits;
		try {
			searchHits = client.prepareSearch(candidateIndexName + itemSet.size()).setQuery(generateQuery(itemSet))
					.setTypes(CANDIDATE_TYPE).execute().get().getHits();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		String id = searchHits.getAt(0).getId();

		try {
			client.prepareDelete(candidateIndexName + itemSet.size(), CANDIDATE_TYPE, id).execute().get();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void saveCandidate(Set<Long> itemSet) {
		try {
			String id = client.prepareIndex(candidateIndexName + itemSet.size(), CANDIDATE_TYPE)
					.setSource(jsonBuilder().startObject().field("transactionValues", itemSet).endObject()).execute()
					.get().getId();
			System.out.println("created id: " + id);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void updateCandidate(Set<Long> itemSet, double support) {
		SearchHits searchHits;
		try {
			searchHits = client.prepareSearch(candidateIndexName + itemSet.size()).setQuery(generateQuery(itemSet))
					.setTypes(CANDIDATE_TYPE).execute().get().getHits();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		if (searchHits.getTotalHits() == 0)
			throw new IllegalArgumentException("itemSet does not exist");
		String id = searchHits.getAt(0).getId();
		try {
			client.prepareIndex(candidateIndexName + itemSet.size(), CANDIDATE_TYPE, id).setSource(jsonBuilder()
					.startObject().field("transactionValues", itemSet).field("support", support).endObject()).execute()
					.get();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public double getSupport(Set<Long> itemSet) {
		SearchHits searchHits;
		try {
			searchHits = client.prepareSearch(candidateIndexName + itemSet.size()).setQuery(generateQuery(itemSet))
					.setTypes(CANDIDATE_TYPE).execute().get().getHits();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		if (searchHits.getTotalHits() == 0)
			throw new IllegalArgumentException("itemSet does not exist");
		return (double) searchHits.getAt(0).getSourceAsMap().get("support");
	}

	private QueryBuilder generateQuery(Set<Long> itemSet) {
		if (itemSet.size() == 0)
			throw new IllegalArgumentException("Zero item set");

		BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();

		for (Long id : itemSet)
			queryBuilder.must(QueryBuilders.termQuery("transactionValues", id));

		return queryBuilder;
	}

	@Override
	public long getSize() {
		return this.size;
	}

	public void close() {
		client.close();
	}

}
