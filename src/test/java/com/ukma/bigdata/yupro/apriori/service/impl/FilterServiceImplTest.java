package com.ukma.bigdata.yupro.apriori.service.impl;

import com.ukma.bigdata.yupro.apriori.model.FrequentSet;
import com.ukma.bigdata.yupro.apriori.service.AprioriStoreService;
import com.ukma.bigdata.yupro.apriori.service.impl.elastic.FilterServiceImpl;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest(LoggerFactory.class)
public class FilterServiceImplTest {

    private static final Logger LOG = mock(Logger.class);

    @InjectMocks
    private FilterServiceImpl filterService;

    @Mock
    private AprioriStoreService<Long, Long> aprioriStoreService;

    @BeforeClass
    public static void setUpBeforeClass() {
        mockStatic(LoggerFactory.class);
        when(LoggerFactory.getLogger(eq(FilterServiceImpl.class))).thenReturn(LOG);
    }


    @Test
    public void testFilter() throws InterruptedException, ExecutionException, IOException {

        int level = 1;
        when(aprioriStoreService.frequentSetIterator(level)).thenReturn(generateFrequentSetIterator());

        filterService.filter(1, 0.5);

        verify(LOG).info("Start frequentSet candidates filtration by support value");
        verify(LOG, times(1)).info("Candidate is successfuly removed");
        verify(LOG).info("Finish frequentSet candidates filtration by support value");


    }

    private Iterator<FrequentSet<Long>> generateFrequentSetIterator() {

        Set<Long> itemSet = new HashSet<>();
        itemSet.add(43L);
        itemSet.add(83L);
        itemSet.add(33L);

        FrequentSet<Long> frequentSet1 = new FrequentSet<>();
        frequentSet1.setItems(itemSet);
        frequentSet1.setSupport(0.1);

        FrequentSet<Long> frequentSet2 = new FrequentSet<>();
        frequentSet2.setItems(itemSet);
        frequentSet2.setSupport(0.7);

        Set<FrequentSet<Long>> set = new HashSet<FrequentSet<Long>>() {
            {
                add(frequentSet1);
                add(frequentSet2);
            }
        };

        return set.iterator();
    }
}
