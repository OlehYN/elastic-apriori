package com.ukma.bigdata.yupro.apriori.service;

import com.ukma.bigdata.yupro.apriori.model.AprioriResult;

public interface AprioriService<TransactionKey, TransactionValue> {
	AprioriResult<TransactionValue> generateAprioriResult(
			TransactionProvider<TransactionKey, TransactionValue> transactionProvider, int level);
}
