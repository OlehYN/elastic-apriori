package com.ukma.bigdata.yupro.apriori.utils.csv;

import java.io.IOException;
import java.util.List;
import org.junit.Test;

public class CsvProductParserTest {
    
    @Test
    public void testParseAll() throws IOException {
        System.out.println("parseAll");
        String path = this.getClass().getResource("/csv/products.csv").getPath();
        CsvProductParser instance = new CsvProductParser(path);
        List<Product> products = instance.parseAll();
        products.stream()
                .limit(20)
                .forEach(System.out::println);
                        
    }
    
}
