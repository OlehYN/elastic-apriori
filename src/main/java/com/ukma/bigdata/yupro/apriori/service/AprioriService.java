package com.ukma.bigdata.yupro.apriori.service;

public interface AprioriService<TransactionKey, TransactionValue> {
    void generateAprioriResult(TransactionProvider<TransactionKey, TransactionValue> transactionProvider, int level,
	    double support, double confidence);

    void proceedLevel(double support, int level, int maxLevel);
}
