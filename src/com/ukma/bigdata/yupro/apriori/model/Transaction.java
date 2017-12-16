package com.ukma.bigdata.yupro.apriori.model;

public class Transaction<TransactionKey, TransactionValue> {
	private TransactionKey transactionKey;
	private TransactionValue transactionValue;

	public TransactionKey getTransactionKey() {
		return transactionKey;
	}

	public void setTransactionKey(TransactionKey transactionKey) {
		this.transactionKey = transactionKey;
	}

	public TransactionValue getTransactionValue() {
		return transactionValue;
	}

	public void setTransactionValue(TransactionValue transactionValue) {
		this.transactionValue = transactionValue;
	}

	@Override
	public String toString() {
		return "Transaction [transactionKey=" + transactionKey + ", transactionValue=" + transactionValue + "]";
	}

}
