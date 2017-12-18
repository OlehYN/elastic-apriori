package com.ukma.bigdata.yupro.apriori.service;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.ukma.bigdata.yupro.apriori.model.FrequentSet;
import com.ukma.bigdata.yupro.apriori.model.ItemSet;

public interface AprioriStoreService<TransactionKey, TransactionValue> {

    Iterator<ItemSet> candidateIterator(int level);

    Iterator<FrequentSet<TransactionValue>> frequentSetIterator(int level);

    void removeCandidate(String id, Set<TransactionValue> itemSet);

    void saveCandidate(Set<TransactionValue> itemSet);

    void saveCandidate(Set<TransactionValue> itemSet, double support);

    void updateCandidate(String id, Set<TransactionValue> itemSet, double support);

    double getSupport(String id, Set<TransactionValue> itemSet);

    long getSize();

    long countTransactions(Set<TransactionValue> itemSet);

    boolean exists(List<Set<Long>> itemSet);

    Set<Long> findOthers(List<Long> items);
}
