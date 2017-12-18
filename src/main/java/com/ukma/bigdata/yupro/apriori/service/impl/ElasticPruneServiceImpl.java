package com.ukma.bigdata.yupro.apriori.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.ukma.bigdata.yupro.apriori.model.ItemSet;
import com.ukma.bigdata.yupro.apriori.service.PruneService;
import org.springframework.stereotype.Service;

@Service
public class ElasticPruneServiceImpl implements PruneService<Long, Long> {

    @Autowired
    private ElasticAprioriStoreService aprioriStoreService;

    @Override
    public void prune(int level) {
	int count = 0;
	int totalCount = 0;
	Iterator<ItemSet> iterator = aprioriStoreService.candidateIterator(level);
	while (iterator.hasNext()) {
	    ItemSet itemSet = iterator.next();
	    boolean isOk = true;

	    for (Set<Long> items : allSubsets(itemSet.getItemSet())) {
		isOk = isOk && aprioriStoreService.exists(items);
		if (!isOk) {
		    ++count;
		    break;
		}
	    }

	    ++totalCount;
	    if (!isOk)
		aprioriStoreService.removeCandidate(itemSet.getId(), itemSet.getItemSet());

	    System.out.println(count);
	}

	System.out.println("pruned " + count + " of " + totalCount);
	aprioriStoreService.flush();
	aprioriStoreService.getClient().admin().indices().prepareRefresh().get();
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

}
