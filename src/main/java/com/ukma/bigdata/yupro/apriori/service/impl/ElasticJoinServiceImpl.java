package com.ukma.bigdata.yupro.apriori.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.ukma.bigdata.yupro.apriori.service.AprioriStoreService;
import com.ukma.bigdata.yupro.apriori.service.JoinService;
import org.springframework.stereotype.Service;

@Service
public class ElasticJoinServiceImpl implements JoinService<Long, Long> {

    @Autowired
    private AprioriStoreService<Long, Long> aprioriStoreService;

    public AprioriStoreService<Long, Long> getAprioriStoreService() {
	return aprioriStoreService;
    }

    public void setAprioriStoreService(AprioriStoreService<Long, Long> aprioriStoreService) {
	this.aprioriStoreService = aprioriStoreService;
    }

    @Override
    public void join(int level) {
	Iterator<Set<Long>> iterator = aprioriStoreService.candidateIterator(level);

	if (level == 0) {
	    while (iterator.hasNext()) {
		Set<Long> parentId = iterator.next();

		Long parentValue = parentId.iterator().next();
		Iterator<Set<Long>> nestedIterator = aprioriStoreService.candidateIterator(level);
		while (nestedIterator.hasNext()) {
		    Long sonId = nestedIterator.next().iterator().next();
		    if (parentValue.compareTo(sonId) < 0) {
			Set<Long> toSave = new HashSet<>(parentId);
			toSave.add(sonId);
			aprioriStoreService.saveCandidate(toSave);
		    }
		}
	    }
	} else {
	    while (iterator.hasNext()) {
		Set<Long> itemSet = iterator.next();
		List<Long> items = new ArrayList<>(itemSet);
		items.sort((val1, val2) -> val1.compareTo(val2));

		Long lastValue = items.get(items.size() - 1);
		items.remove(items.size() - 1);
		Set<Long> possibleValues = aprioriStoreService.findOthers(items);

		possibleValues.stream().filter(value -> value.compareTo(lastValue) > 0).collect(Collectors.toList())
			.stream().forEach(value -> {
			    List<Long> stub = new ArrayList<>(items);
			    stub.add(value);
			    stub.add(lastValue);

			    aprioriStoreService.saveCandidate(new HashSet<>(stub));
			});

	    }
	}
    }

}
