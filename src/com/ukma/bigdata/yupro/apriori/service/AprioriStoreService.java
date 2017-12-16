package com.ukma.bigdata.yupro.apriori.service;

import java.util.Iterator;
import java.util.Set;

public interface AprioriStoreService<TransactionKey, TransactionValue> {
	void init(TransactionProvider<TransactionKey, TransactionValue> transactionProvider);

	Iterator<TransactionValue> candidateIterator(int level);

	void removeCandidate(Set<TransactionValue> itemSet);

	void saveCandidate(Set<TransactionValue> itemSet);

	void updateCandidate(Set<TransactionValue> itemSet, double support);

	double getSupport(Set<TransactionValue> itemSet);

	int getSize();
}
