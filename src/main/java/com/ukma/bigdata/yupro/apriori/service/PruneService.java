package com.ukma.bigdata.yupro.apriori.service;

public interface PruneService<TransactionKey, TransactionValue> {
    void prune(int level);
}