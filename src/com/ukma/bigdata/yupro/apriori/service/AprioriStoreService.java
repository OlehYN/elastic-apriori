package com.ukma.bigdata.yupro.apriori.service;

import java.util.Iterator;
import java.util.Set;

import com.ukma.bigdata.yupro.apriori.model.FrequentSet;

public interface AprioriStoreService<TransactionKey, TransactionValue> {
	void init(TransactionProvider<TransactionKey, TransactionValue> transactionProvider);

	Iterator<Set<TransactionValue>> candidateIterator(int level);
	
	Iterator<FrequentSet<TransactionValue>> frequentSetIterator(int level);

	void removeCandidate(Set<TransactionValue> itemSet);

	void saveCandidate(Set<TransactionValue> itemSet);

	void updateCandidate(Set<TransactionValue> itemSet, double support);

	double getSupport(Set<TransactionValue> itemSet);

	int getSize();
}
