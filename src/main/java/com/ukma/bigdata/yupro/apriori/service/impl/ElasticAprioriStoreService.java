package com.ukma.bigdata.yupro.apriori.service.impl;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import com.ukma.bigdata.yupro.apriori.model.FrequentSet;
import com.ukma.bigdata.yupro.apriori.model.Transaction;
import com.ukma.bigdata.yupro.apriori.service.AprioriStoreService;
import com.ukma.bigdata.yupro.apriori.service.TransactionProvider;

import static org.elasticsearch.common.xcontent.XContentFactory.*;

public class ElasticAprioriStoreService implements AprioriStoreService<Long, Long> {

	private String candidateIndexName;
	private String transactionIndexName;

	private static final String CANDIDATE_TYPE = "candidate-";
	private static final String TRANSACTION_TYPE = "transaction";

	private static final int transactionBufferSize = 5000;

	public ElasticAprioriStoreService(String hostName, int port, String candidateIndexName, String transactionIndexName,
			TransactionProvider<Long, Long> transactionProvider)
			throws IOException, InterruptedException, ExecutionException {
		TransportClient client = new PreBuiltTransportClient(Settings.EMPTY)
				.addTransportAddress(new TransportAddress(InetAddress.getByName(hostName), port));

		this.candidateIndexName = candidateIndexName;
		this.transactionIndexName = transactionIndexName;

		Transaction<Long, Long> transaction = transactionProvider.nextTransaction();
		BulkRequestBuilder bulkRequest = client.prepareBulk();

		int bulkSize = 0;
		if (!client.admin().indices().exists(new IndicesExistsRequest(transactionIndexName)).get().isExists()) {
			System.out.println("gfdgfd");
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
		// TODO Auto-generated method stub

	}

	@Override
	public void saveCandidate(Set<Long> itemSet) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateCandidate(Set<Long> itemSet, double support) {
		// TODO Auto-generated method stub

	}

	@Override
	public double getSupport(Set<Long> itemSet) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return 0;
	}

}
