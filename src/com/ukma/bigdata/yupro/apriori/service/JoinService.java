package com.ukma.bigdata.yupro.apriori.service;

public interface JoinService<TransactionKey, TransactionValue> {
	void join(AprioriStoreService<TransactionKey, TransactionValue> candidateProvider, int level);
}
