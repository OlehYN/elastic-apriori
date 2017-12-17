package com.ukma.bigdata.yupro.apriori.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.ukma.bigdata.yupro.apriori.service.AprioriStoreService;
import com.ukma.bigdata.yupro.apriori.service.PruneService;

public class ElasticPruneServiceImpl implements PruneService<Long, Long> {

    @Autowired
    private AprioriStoreService<Long, Long> aprioriStoreService;

    @Override
    public void prune(int level) {
	Iterator<Set<Long>> iterator = aprioriStoreService.candidateIterator(level);
	while (iterator.hasNext()) {
	    Set<Long> itemSet = iterator.next();
	    boolean isOk = true;

	    for (Set<Long> items : allSubsets(itemSet)) {
		isOk = isOk && aprioriStoreService.exists(items);
		if (!isOk)
		    break;
	    }

	    if (!isOk)
		aprioriStoreService.removeCandidate(itemSet);
	}
    }

    public List<Set<Long>> allSubsets(Set<Long> itemSet) {
	List<Set<Long>> result = new ArrayList<>();
	List<Long> list = new ArrayList<>(itemSet);

	for (int i = 0; i < list.size(); i++) {
	    List<Long> tempList = new ArrayList<>(list);
	    tempList.remove(i);
	    result.add(new HashSet<>(tempList));
	}
	return result;
    }

    public AprioriStoreService<Long, Long> getAprioriStoreService() {
	return aprioriStoreService;
    }

    public void setAprioriStoreService(AprioriStoreService<Long, Long> aprioriStoreService) {
	this.aprioriStoreService = aprioriStoreService;
    }

}
