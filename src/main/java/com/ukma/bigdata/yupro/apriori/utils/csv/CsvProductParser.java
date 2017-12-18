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
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import com.ukma.bigdata.yupro.apriori.utils.LineByLineParser;

public class CsvProductParser implements LineByLineParser<Product> {

    @Override
    public List<Product> parseAll(String path) {
        List<Product> result = null;
        try {
            Reader in = new FileReader(path);
            Iterable<CSVRecord> records = CSVFormat.RFC4180.
                    withFirstRecordAsHeader().parse(in);
            result = StreamSupport.stream(records.spliterator(), false)
                    .map(record -> new Product(record.get("product_id"), record.
                            get("product_name")))
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
