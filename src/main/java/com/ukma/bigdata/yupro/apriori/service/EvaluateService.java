package com.ukma.bigdata.yupro.apriori.service;

public interface EvaluateService<TransactionKey, TransactionValue> {

	void evaluate(AprioriStoreService<TransactionKey, TransactionValue> candidates, int level);
}
