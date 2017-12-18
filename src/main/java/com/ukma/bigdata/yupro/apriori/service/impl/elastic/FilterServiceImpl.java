package com.ukma.bigdata.yupro.apriori.service.impl.elastic;

import com.ukma.bigdata.yupro.apriori.model.FrequentSet;
import com.ukma.bigdata.yupro.apriori.service.AprioriStoreService;
import com.ukma.bigdata.yupro.apriori.service.FilterService;
import com.ukma.bigdata.yupro.apriori.service.impl.ElasticAprioriStoreService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Iterator;
import org.springframework.stereotype.Service;

@Service
public class FilterServiceImpl implements FilterService<Long, Long> {

    private static final Logger LOG = LoggerFactory.getLogger(FilterServiceImpl.class);

    private ElasticAprioriStoreService aprioriStoreService;

    @Autowired
    public FilterServiceImpl(final ElasticAprioriStoreService aprioriStoreService) {
	this.aprioriStoreService = aprioriStoreService;
    }

    public AprioriStoreService<Long, Long> getAprioriStoreService() {
	return aprioriStoreService;
    }

    @Override
    public void filter(int level, double minSupport) {
	LOG.info("Start frequentSet candidates filtration by support value");
	LOG.info("filter(): level " + level);
	int count = 0;
	Iterator<FrequentSet<Long>> iterator = aprioriStoreService.frequentSetIterator(level);
	while (iterator.hasNext()) {
	    FrequentSet<Long> frequentItemsSet = iterator.next();

	    if (frequentItemsSet.getSupport() < minSupport) {
		aprioriStoreService.removeCandidate(frequentItemsSet.getId(), frequentItemsSet.getItems());
		// LOG.info("Candidate is successfuly removed");
		LOG.debug("filter(): remove " + frequentItemsSet);
	    }
	}
	LOG.info("Finish frequentSet candidates filtration by support value");
	LOG.info("filter(): filtered " + count);
	aprioriStoreService.flush();
	aprioriStoreService.getClient().admin().indices().prepareRefresh().get();
    }
}
