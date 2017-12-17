package com.ukma.bigdata.yupro.apriori.service;

public interface EvaluateService<TransactionKey, TransactionValue> {

	void evaluate(int level);
}
