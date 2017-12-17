package com.ukma.bigdata.yupro.apriori.service.impl;

import com.ukma.bigdata.yupro.apriori.model.AprioriResult;
import com.ukma.bigdata.yupro.apriori.service.AprioriService;
import com.ukma.bigdata.yupro.apriori.service.AprioriStoreService;
import com.ukma.bigdata.yupro.apriori.service.EvaluateService;
import com.ukma.bigdata.yupro.apriori.service.FilterService;
import com.ukma.bigdata.yupro.apriori.service.JoinService;
import com.ukma.bigdata.yupro.apriori.service.PruneService;
import com.ukma.bigdata.yupro.apriori.service.TransactionProvider;

public class GenericAprioriService<TransactionKey, TransactionValue>
	implements AprioriService<TransactionKey, TransactionValue> {

    private JoinService<TransactionKey, TransactionValue> joinService;
    private PruneService<TransactionKey, TransactionValue> pruneService;
    private FilterService<TransactionKey, TransactionValue> filterService;

    private EvaluateService<TransactionKey, TransactionValue> evaluateService;
    private AprioriStoreService<TransactionKey, TransactionValue> aprioriStoreService;

    @Override
    public AprioriResult<TransactionValue> generateAprioriResult(
	    TransactionProvider<TransactionKey, TransactionValue> transactionProvider, int level, double support,
	    double confidence) {

	// TODO prepare candidate-0 index using _search and aggs

	for (int i = 0; i < level; i++) {
	    if (level != 0)
		evaluateService.evaluate(i);
	    if (level != 0)
		filterService.filter(i, support);
	    joinService.join(i);
	    pruneService.prune(i);
	}

	// TODO use aprioriStoreService to retrieve everything
	// TODO new service to form AssociationRules
	return null;
    }

}
