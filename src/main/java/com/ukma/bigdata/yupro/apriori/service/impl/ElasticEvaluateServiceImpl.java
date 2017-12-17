package com.ukma.bigdata.yupro.apriori.service.impl;

import java.util.Iterator;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.ukma.bigdata.yupro.apriori.service.AprioriStoreService;
import com.ukma.bigdata.yupro.apriori.service.EvaluateService;
import org.springframework.stereotype.Service;

@Service
public class ElasticEvaluateServiceImpl implements EvaluateService<Long, Long> {

    @Autowired
    private AprioriStoreService<Long, Long> aprioriStoreService;

    public AprioriStoreService<Long, Long> getAprioriStoreService() {
	return aprioriStoreService;
    }

    public void setAprioriStoreService(AprioriStoreService<Long, Long> aprioriStoreService) {
	this.aprioriStoreService = aprioriStoreService;
    }

    @Override
    public void evaluate(int level) {
	Iterator<Set<Long>> iterator = aprioriStoreService.candidateIterator(level);

	while (iterator.hasNext()) {
	    Set<Long> itemSet = iterator.next();
	    double support = aprioriStoreService.countTransactions(itemSet) / aprioriStoreService.getSize();
	    aprioriStoreService.updateCandidate(itemSet, support);
	}
    }
}
