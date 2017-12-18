package com.ukma.bigdata.yupro.apriori.service.impl;

import com.ukma.bigdata.yupro.apriori.config.Config;
import com.ukma.bigdata.yupro.apriori.model.Transaction;
import com.ukma.bigdata.yupro.apriori.service.TransactionProvider;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Config.class)
public class CsvTransactionProviderImplTest {

    @Autowired
    private TransactionProvider<Long, Long> transactionProvider;

    @Test
    public void test1() throws FileNotFoundException {
	Transaction<Long, Long> transaction1 = transactionProvider.nextTransaction();
	Transaction<Long, Long> transaction2 = transactionProvider.nextTransaction();
	Transaction<Long, Long> transaction3 = transactionProvider.nextTransaction();

	Transaction<Long, Long> stubTranscation1 = new Transaction<>();
	stubTranscation1.setTransactionKey(2L);
	Set<Long> stubSet1 = new HashSet<>();
	stubSet1.add(33120L);
	stubSet1.add(28985L);
	stubSet1.add(9327L);
	stubSet1.add(45918L);
	stubSet1.add(30035L);
	stubSet1.add(17794L);
	stubSet1.add(40141L);
	stubSet1.add(1819L);
	stubSet1.add(43668L);
	stubTranscation1.setTransactionValue(stubSet1);

	Transaction<Long, Long> stubTranscation2 = new Transaction<>();
	stubTranscation2.setTransactionKey(3L);
	Set<Long> stubSet2 = new HashSet<>();
	stubSet2.add(33754L);
	stubSet2.add(24838L);
	stubSet2.add(17704L);
	stubSet2.add(21903L);
	stubSet2.add(17668L);
	stubSet2.add(46667L);
	stubSet2.add(17461L);
	stubSet2.add(32665L);
	stubTranscation2.setTransactionValue(stubSet2);

	Transaction<Long, Long> stubTranscation3 = new Transaction<>();
	stubTranscation3.setTransactionKey(4L);
	Set<Long> stubSet3 = new HashSet<>();
	stubSet3.add(46842L);
	stubSet3.add(26434L);
	stubTranscation3.setTransactionValue(stubSet3);

	assertEquals(transaction1, stubTranscation1);
	assertEquals(transaction2, stubTranscation2);
	assertEquals(transaction3, stubTranscation3);
    }
}
