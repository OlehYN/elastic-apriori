package com.ukma.bigdata.yupro.apriori.service.impl;

import com.ukma.bigdata.yupro.apriori.model.FrequentSet;
import com.ukma.bigdata.yupro.apriori.service.AprioriStoreService;
import com.ukma.bigdata.yupro.apriori.service.FilterService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Iterator;

public class FilterServiceImpl implements FilterService<Long, Long> {

    private AprioriStoreService<Long, Long> aprioriStoreService;

    @Autowired
    public FilterServiceImpl(final AprioriStoreService<Long, Long> aprioriStoreService) {
        this.aprioriStoreService = aprioriStoreService;
    }

    public AprioriStoreService<Long, Long> getAprioriStoreService() {
        return aprioriStoreService;
    }

    @Override
    public void filter(int level, double minSupport) {
        Iterator<FrequentSet<Long>> iterator = aprioriStoreService.frequentSetIterator(level);

        while (iterator.hasNext()) {
            FrequentSet<Long> frequentItemsSet = iterator.next();

            if (frequentItemsSet.getSupport() < minSupport) {
                aprioriStoreService.removeCandidate(frequentItemsSet.getItems());
            }
        }
    }
}
