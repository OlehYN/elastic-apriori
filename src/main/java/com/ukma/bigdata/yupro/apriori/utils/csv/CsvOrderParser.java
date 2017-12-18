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

public class CsvOrderParser implements LineByLineParser<Order> {

    private final Iterable<CSVRecord> records;
    private final Iterator<CSVRecord> iterator;

    public CsvOrderParser(String path) throws FileNotFoundException,
            IOException {
        Reader in = new FileReader(path);
        this.records = CSVFormat.RFC4180.
                withFirstRecordAsHeader().parse(in);
        this.iterator = records.iterator();
    }

    @Override
    public Order next() {
        CSVRecord record = this.iterator.next();
        Order product = null;
        if (record != null) {
            product = new Order(
                    record.get("order_id"),
                    record.get("user_id"),
                    record.get("order_number"));
        }
        return product;
    }

    @Override
    public boolean hasNext() {
        return this.iterator.hasNext();
    }

    @Override
    public List<Order> parseAll() {
        return StreamSupport.stream(records.spliterator(), false)
                .map(record -> new Order(
                        record.get("order_id"),
                        record.get("user_id"),
                        record.get("order_number")))
                .collect(Collectors.toList());
    }

}
