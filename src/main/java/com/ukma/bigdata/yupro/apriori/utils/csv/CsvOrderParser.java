package com.ukma.bigdata.yupro.apriori.utils.csv;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import com.ukma.bigdata.yupro.apriori.utils.LineByLineParser;

public class CsvOrderParser implements LineByLineParser<Order>{

    @Override
    public List<Order> parseAll(String path) {
        List<Order> result = null;
        try {
            Reader in = new FileReader(path);
            Iterable<CSVRecord> records = CSVFormat.RFC4180.
                    withFirstRecordAsHeader().parse(in);
            result = StreamSupport.stream(records.spliterator(), false)
                    .limit(50)
                    .map(record -> new Order(
                            record.get("order_id"), 
                            record.get("user_id"), 
                            record.get("order_number")))
                    .collect(Collectors.toList());
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CsvProductParser.class.getName()).log(Level.SEVERE, null,
                    ex);
        } catch (IOException ex) {
            Logger.getLogger(CsvProductParser.class.getName()).log(Level.SEVERE, null,
                    ex);
        }
        return result;
    }

}
