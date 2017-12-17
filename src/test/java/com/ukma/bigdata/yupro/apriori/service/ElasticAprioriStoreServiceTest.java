package com.ukma.bigdata.yupro.apriori.service;

import com.ukma.bigdata.yupro.apriori.ContextTest;
import com.ukma.bigdata.yupro.apriori.config.Config;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.search.SearchHit;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.ukma.bigdata.yupro.apriori.service.impl.ElasticAprioriStoreService;
import com.ukma.bigdata.yupro.apriori.service.impl.ElasticJoinServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Config.class)
public class ElasticAprioriStoreServiceTest {

	@Autowired
	private ElasticAprioriStoreService elasticAprioriStoreService;

	@Autowired
	private TransportClient client;

	@Test
	public void testIndex() throws IOException, InterruptedException, ExecutionException {
		Set<Long> itemSet = new HashSet<>();
		itemSet.add(43L);
		itemSet.add(83L);
		itemSet.add(33L);

		/*
		 * eass.saveCandidate(itemSet); eass.updateCandidate(itemSet, 0.7);
		 */
		System.out.println(client);

		/* eass.removeCandidate(itemSet); */

		ElasticJoinServiceImpl ejs = new ElasticJoinServiceImpl();
		ejs.setAprioriStoreService(elasticAprioriStoreService);
		ejs.join(0);
	}

	// @Test
	public void testIterator() {
		System.out.println(elasticAprioriStoreService.toString());
		// Iterator<Set<Long>> iterator =
		// elasticAprioriStoreService.candidateIterator(0);
	}
}
