package com.ukma.bigdata.yupro.apriori.service.impl.elastic;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ukma.bigdata.yupro.apriori.model.ItemSet;
import com.ukma.bigdata.yupro.apriori.service.PruneService;
import com.ukma.bigdata.yupro.apriori.service.impl.ElasticAprioriStoreService;

import org.springframework.stereotype.Service;

@Service
public class PruneServiceImpl implements PruneService<Long, Long> {

    @Autowired
    private ElasticAprioriStoreService aprioriStoreService;

    private static final Logger LOG = LoggerFactory.getLogger(PruneServiceImpl.class);

    @Override
    public void prune(int level) {
	int count = 0;
	int totalCount = 0;
	Iterator<ItemSet> iterator = aprioriStoreService.candidateIterator(level);

	LOG.info("prune(): level " + level);
	while (iterator.hasNext()) {
	    ItemSet itemSet = iterator.next();
	    boolean isOk = aprioriStoreService.exists(allSubsets(itemSet.getItemSet()));

	    if (!isOk) {
		++count;
		LOG.debug("prune(): remove " + itemSet.getItemSet());
		aprioriStoreService.removeCandidate(itemSet.getId(), itemSet.getItemSet());
	    }

	    ++totalCount;
	}

	LOG.info("prune(): " + count + " of " + totalCount);
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
