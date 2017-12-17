package com.ukma.bigdata.yupro.apriori.service;

public interface FilterService<TransactionKey, TransactionValue> {

    void filter(int level, double minSupport);
}