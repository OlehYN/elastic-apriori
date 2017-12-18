package com.ukma.bigdata.yupro.apriori.service.impl;

import com.ukma.bigdata.yupro.apriori.config.Config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import org.elasticsearch.client.transport.TransportClient;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.ukma.bigdata.yupro.apriori.service.AprioriService;
import com.ukma.bigdata.yupro.apriori.service.AprioriStoreService;
import com.ukma.bigdata.yupro.apriori.service.TransactionProvider;
import com.ukma.bigdata.yupro.apriori.service.impl.elastic.PruneServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Config.class)
public class ElasticAprioriStoreServiceTest {

    @Autowired
    private AprioriStoreService<Long, Long> elasticAprioriStoreService;

    @Autowired
    private TransportClient client;

    @Autowired
    private AprioriService<Long, Long> aprioriService;

    @Autowired
    private TransactionProvider<Long, Long> transactionProvider;

    // @Test
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

	// ElasticJoinServiceImpl ejs = new ElasticJoinServiceImpl();
	// ejs.setAprioriStoreService(elasticAprioriStoreService);
	// ejs.join(1);

	PruneServiceImpl eps = new PruneServiceImpl();
	eps.prune(1);
    }

    // @Test
    public void testIterator() {
	System.out.println(elasticAprioriStoreService.toString());
	// Iterator<Set<Long>> iterator =
	// elasticAprioriStoreService.candidateIterator(0);
    }

    @Test
    public void test() throws FileNotFoundException {
	aprioriService.generateAprioriResult(transactionProvider, 2, 0.001, 0.2);
    }
}
