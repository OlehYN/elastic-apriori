package com.ukma.bigdata.yupro.apriori.service;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import org.junit.Test;

import com.ukma.bigdata.yupro.apriori.service.impl.CsvTransactionProviderImpl;
import com.ukma.bigdata.yupro.apriori.service.impl.ElasticAprioriStoreService;

public class ElasticAprioriStoreServiceTest {

	@Test
	public void testIndex() throws IOException, InterruptedException, ExecutionException {
		TransactionProvider<Long, Long> csvProvider = new CsvTransactionProviderImpl("test.csv", 0, 1, ',', '"');
		new ElasticAprioriStoreService("localhost", 9300, ".candidate", ".transaction", csvProvider);
	}
}
