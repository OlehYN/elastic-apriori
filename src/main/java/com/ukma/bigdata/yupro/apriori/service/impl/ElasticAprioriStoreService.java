package com.ukma.bigdata.yupro.apriori.service.impl;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import com.ukma.bigdata.yupro.apriori.model.FrequentSet;
import com.ukma.bigdata.yupro.apriori.service.AprioriStoreService;
import com.ukma.bigdata.yupro.apriori.service.TransactionProvider;

public class ElasticAprioriStoreService implements AprioriStoreService<Long, Long> {

	private String typePattern;
	private String indexName;

	public ElasticAprioriStoreService(String hostName, int port, String indexName, String typePattern,
			TransactionProvider<Long, Long> transactionProvider) throws UnknownHostException {
		TransportClient client = new PreBuiltTransportClient(Settings.EMPTY)
				.addTransportAddress(new TransportAddress(InetAddress.getByName(hostName), port));

		this.typePattern = typePattern;
		this.indexName = indexName;
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
