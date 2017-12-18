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

public class CsvOrderProductParser implements LineByLineParser<OrderProduct> {

    private final Iterable<CSVRecord> records;
    private final Iterator<CSVRecord> iterator;

    public CsvOrderProductParser(String path) throws FileNotFoundException,
            IOException {
        Reader in = new FileReader(path);
        this.records = CSVFormat.RFC4180.
                withFirstRecordAsHeader().parse(in);
        this.iterator = records.iterator();
    }

    @Override
    public OrderProduct next() {
        CSVRecord record = this.iterator.next();
        OrderProduct product = null;
        if (record != null) {
            product = new OrderProduct(
                    record.get("order_id"),
                    record.get("product_id"),
                    record.get("add_to_cart_order"));
        }
        return product;
    }

    @Override
    public boolean hasNext() {
        return this.iterator.hasNext();
    }

    @Override
    public List<OrderProduct> parseAll() {
        return StreamSupport.stream(records.spliterator(), false)
                .map(record -> new OrderProduct(
                        record.get("order_id"),
                        record.get("product_id"),
                        record.get("add_to_cart_order")))
                .collect(Collectors.toList());
    }

}
