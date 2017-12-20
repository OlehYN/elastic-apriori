package com.ukma.bigdata.yupro.apriori.service.impl;

import com.ukma.bigdata.yupro.apriori.config.Config;

import java.io.IOException;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.ukma.bigdata.yupro.apriori.service.AprioriService;
import com.ukma.bigdata.yupro.apriori.service.DataProvider;
import com.ukma.bigdata.yupro.apriori.service.TransactionProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Config.class)
public class ElasticAprioriStoreServiceTest {

    @Autowired
    private ElasticAprioriStoreService elasticAprioriStoreService;

    @Autowired
    private AprioriService<Long, Long> aprioriService;

    @Autowired
    private TransactionProvider<Long, Long> transactionProvider;

    @Autowired
    private DataProvider dataProvider;

    @Test
    public void test0() {

    }

    // @Test
    public void test1() {
	aprioriService.generateAprioriResult(transactionProvider, 2, 0.001, 0.2);
    }

    // @Test
    public void test2() throws IOException {
	elasticAprioriStoreService.readCsv(dataProvider, "bd-orders", "orders", "order_id");
    }
}
