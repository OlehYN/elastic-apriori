package com.ukma.bigdata.yupro.apriori.service;

import com.ukma.bigdata.yupro.apriori.model.Transaction;
import org.springframework.stereotype.Component;

public interface TransactionProvider<TransactionKey, TransactionValue> {
	Transaction<TransactionKey, TransactionValue> nextTransaction();
}
