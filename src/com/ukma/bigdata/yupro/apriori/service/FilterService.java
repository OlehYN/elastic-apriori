package com.ukma.bigdata.yupro.apriori.service;

public interface FilterService<TransactionKey, TransactionValue> {

	void filter(AprioriStoreService<TransactionKey, TransactionValue> candidates, int level);
}
