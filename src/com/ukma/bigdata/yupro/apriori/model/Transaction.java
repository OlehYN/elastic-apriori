package com.ukma.bigdata.yupro.apriori.model;

import java.util.Set;

public class Transaction<TransactionKey, TransactionValue> {
	private TransactionKey transactionKey;
	private Set<TransactionValue> transactionValue;

	public TransactionKey getTransactionKey() {
		return transactionKey;
	}

	public void setTransactionKey(TransactionKey transactionKey) {
		this.transactionKey = transactionKey;
	}

	public Set<TransactionValue> getTransactionValue() {
		return transactionValue;
	}

	public void setTransactionValue(Set<TransactionValue> transactionValue) {
		this.transactionValue = transactionValue;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Transaction<?, ?> other = (Transaction<?, ?>) obj;
		if (transactionKey == null) {
			if (other.transactionKey != null)
				return false;
		} else if (!transactionKey.equals(other.transactionKey))
			return false;
		if (transactionValue == null) {
			if (other.transactionValue != null)
				return false;
		} else if (!transactionValue.equals(other.transactionValue))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Transaction [transactionKey=" + transactionKey + ", transactionValue=" + transactionValue + "]";
	}

}
