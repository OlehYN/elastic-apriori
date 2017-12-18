package com.ukma.bigdata.yupro.apriori.service.impl;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import com.ukma.bigdata.yupro.apriori.model.Transaction;
import com.ukma.bigdata.yupro.apriori.service.TransactionProvider;

import au.com.bytecode.opencsv.CSVReader;

public class CsvTransactionProviderImpl implements TransactionProvider<Long, Long> {

	private CSVReader reader;
	private Long transactionId;
	private Long transactionValue;

	private int transactionIdField;
	private int transactionValueField;

    public CsvTransactionProviderImpl(String transactionsCsv, int transactionIdField, int transactionValueField,
			char delimeter, char escape) throws FileNotFoundException {
		this.reader = new CSVReader(new FileReader(transactionsCsv), delimeter, escape, 1);
		this.transactionIdField = transactionIdField;
		this.transactionValueField = transactionValueField;
	}

	@Override
	public Transaction<Long, Long> nextTransaction() {
		String[] transactionLine;
		Long currentTransactionId;
		Long currentTransactionValue;

		if (transactionId == null) {
			try {
				transactionLine = reader.readNext();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}

			if (transactionLine == null)
				return null;

			currentTransactionId = Long.parseLong(transactionLine[transactionIdField]);
			currentTransactionValue = Long.parseLong(transactionLine[transactionValueField]);

			transactionId = currentTransactionId;
			transactionValue = currentTransactionValue;
		} else {
			currentTransactionId = transactionId;
			currentTransactionValue = transactionValue;
		}

		boolean end = false;
		Transaction<Long, Long> transaction = new Transaction<>();
		Set<Long> items = new HashSet<>();

		items.add(transactionValue);

		transaction.setTransactionKey(currentTransactionId);

		while (currentTransactionId.equals(transactionId)) {
			items.add(currentTransactionValue);

			try {
				transactionLine = reader.readNext();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}

			if (transactionLine == null) {
				end = true;
				break;
			}

			currentTransactionId = Long.parseLong(transactionLine[transactionIdField]);
			currentTransactionValue = Long.parseLong(transactionLine[transactionValueField]);
		}

		if (!end) {
			this.transactionId = currentTransactionId;
			this.transactionValue = currentTransactionValue;
		} else {
			this.transactionId = null;
			this.transactionValue = null;
		}
		transaction.setTransactionValue(items);
		return transaction;
	}

}
