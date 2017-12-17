package com.ukma.bigdata.yupro.apriori.service;

import com.ukma.bigdata.yupro.apriori.ContextTest;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import org.junit.Test;

import com.ukma.bigdata.yupro.apriori.service.impl.ElasticAprioriStoreService;
import org.springframework.beans.factory.annotation.Autowired;

public class ElasticAprioriStoreServiceTest extends ContextTest{

    @Autowired
    private ElasticAprioriStoreService elasticAprioriStoreService;
    
//    @Test
    public void testIndex() throws IOException, InterruptedException,
            ExecutionException {
        ElasticAprioriStoreService eass = new ElasticAprioriStoreService(
                "localhost", 9300);
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
    
//    @Test
    public void testIterator() {
        System.out.println(elasticAprioriStoreService.toString());
//        Iterator<Set<Long>> iterator = elasticAprioriStoreService.candidateIterator(0);
    }

}
