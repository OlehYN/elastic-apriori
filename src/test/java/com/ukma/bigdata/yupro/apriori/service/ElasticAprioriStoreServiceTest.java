package com.ukma.bigdata.yupro.apriori.service;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import org.junit.Test;

import com.ukma.bigdata.yupro.apriori.service.impl.CsvTransactionProviderImpl;
import com.ukma.bigdata.yupro.apriori.service.impl.ElasticAprioriStoreService;

public class ElasticAprioriStoreServiceTest {

	@Test
	public void testIndex() throws IOException, InterruptedException, ExecutionException {
		TransactionProvider<Long, Long> csvProvider = new CsvTransactionProviderImpl("test.csv", 0, 1, ',', '"');
		ElasticAprioriStoreService eass = new ElasticAprioriStoreService("localhost", 9300, ".candidate",
				".transaction", csvProvider);
		Set<Long> itemSet = new HashSet<>();
		itemSet.add(43L);
		itemSet.add(83L);
		itemSet.add(33L);

		/*
		 * eass.saveCandidate(itemSet); eass.updateCandidate(itemSet, 0.7);
		 */

		System.out.println(eass.getSize());

		/* eass.removeCandidate(itemSet); */
	}
}
