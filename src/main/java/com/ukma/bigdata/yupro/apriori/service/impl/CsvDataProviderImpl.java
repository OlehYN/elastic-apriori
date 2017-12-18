package com.ukma.bigdata.yupro.apriori.service.impl;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import com.ukma.bigdata.yupro.apriori.service.DataProvider;

import au.com.bytecode.opencsv.CSVReader;

public class CsvDataProviderImpl implements DataProvider {

    private CSVReader reader;
    private Map<Integer, String> names;

    public CsvDataProviderImpl(String transactionsCsv, char delimeter, char escape) throws IOException {
	this.reader = new CSVReader(new FileReader(transactionsCsv), delimeter, escape, 0);
	String[] columns = reader.readNext();
	names = new HashMap<>();
	
	for (int i = 0; i < columns.length; i++)
	    names.put(i, columns[i]);
    }

    @Override
    public Map<String, String> nextTransaction() {
	String[] values = null;
	try {
	    values = reader.readNext();
	} catch (IOException e) {
	    throw new RuntimeException(e);
	}

	if (values == null)
	    return null;

	Map<String, String> map = new HashMap<>();
	for (int i = 0; i < values.length; i++) {
	    map.put(names.get(i), values[i]);
	}
	return map;
    }

}
