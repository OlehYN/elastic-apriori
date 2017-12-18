/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ukma.bigdata.yupro.apriori.utils.csv;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import com.ukma.bigdata.yupro.apriori.utils.LineByLineParser;
import java.util.Iterator;

public class CsvProductParser implements LineByLineParser<Product> {

    private final Iterable<CSVRecord> records;
    private final Iterator<CSVRecord> iterator;

    public CsvProductParser(String path) throws FileNotFoundException,
            IOException {
        Reader in = new FileReader(path);
        this.records = CSVFormat.RFC4180.
                withFirstRecordAsHeader().parse(in);
        this.iterator = records.iterator();
    }

    @Override
    public Product next() {
        CSVRecord record = this.iterator.next();
        Product product = null;
        if (record != null) {
            product = new Product(
                    record.get("product_id"),
                    record.get("product_name"),
                    record.get("aisle_id"));
        }
        return product;
    }

    @Override
    public boolean hasNext() {
        return this.iterator.hasNext();
    }

    @Override
    public List<Product> parseAll() {
        return StreamSupport.stream(records.spliterator(), false)
//                .limit(100)
                .map(record -> new Product(
                        record.get("product_id"),
                        record.get("product_name"),
                        record.get("aisle_id")))
                .collect(Collectors.toList());
    }

}
