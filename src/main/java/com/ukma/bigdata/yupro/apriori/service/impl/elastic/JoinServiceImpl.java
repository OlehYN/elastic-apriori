package com.ukma.bigdata.yupro.apriori.service.impl.elastic;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ukma.bigdata.yupro.apriori.model.ItemSet;
import com.ukma.bigdata.yupro.apriori.service.JoinService;
import com.ukma.bigdata.yupro.apriori.service.impl.ElasticAprioriStoreService;

import org.springframework.stereotype.Service;

@Service
public class JoinServiceImpl implements JoinService<Long, Long> {

    @Autowired
    private ElasticAprioriStoreService aprioriStoreService;

    private static final Logger LOG = LoggerFactory.getLogger(JoinServiceImpl.class);

    @Override
    public void join(int level) {
	Iterator<ItemSet> iterator = aprioriStoreService.candidateIterator(level);
	int count = 0;
	LOG.info("join(): level " + level);
	if (level == 0) {
	    while (iterator.hasNext()) {
		Set<Long> parentId = iterator.next().getItemSet();
		Long parentValue = parentId.iterator().next();
		Iterator<ItemSet> nestedIterator = aprioriStoreService.candidateIterator(level);
		while (nestedIterator.hasNext()) {
		    Long sonId = nestedIterator.next().getItemSet().iterator().next();
		    if (parentValue.compareTo(sonId) < 0) {
			Set<Long> toSave = new HashSet<>(parentId);
			toSave.add(sonId);
			LOG.debug("join(): save " + toSave);
			aprioriStoreService.saveCandidate(toSave);
			++count;
		    }
		}
	    }
	} else {
	    while (iterator.hasNext()) {
		Set<Long> itemSet = iterator.next().getItemSet();
		List<Long> items = new ArrayList<>(itemSet);
		items.sort((val1, val2) -> val1.compareTo(val2));

		Long lastValue = items.get(items.size() - 1);
		items.remove(items.size() - 1);
		Set<Long> possibleValues = aprioriStoreService.findOthers(items);

		List<Long> values = possibleValues.stream().filter(value -> value.compareTo(lastValue) > 0)
			.collect(Collectors.toList());

		for (Long value : values) {
		    List<Long> stub = new ArrayList<>(items);
		    stub.add(value);
		    stub.add(lastValue);

		    aprioriStoreService.saveCandidate(new HashSet<>(stub));
		    LOG.debug("join(): save " + stub);
		    ++count;
		}

	    }
	}

	LOG.info("join(): " + count + " were joined.");
	aprioriStoreService.flush();
	aprioriStoreService.getClient().admin().indices().prepareRefresh().get();
    }

}
