package com.ukma.bigdata.yupro.apriori.service;

public interface JoinService<TransactionKey, TransactionValue> {

	void join(int level);
}