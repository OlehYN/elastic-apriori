package com.ukma.bigdata.yupro.apriori.utils.csv;

import java.util.List;
import org.junit.Test;

public class CsvProductParserTest {
    
    @Test
    public void testParseAll() {
        System.out.println("parseAll");
        String path = this.getClass().getResource("/csv/products.csv").getPath();
        CsvProductParser instance = new CsvProductParser();
        List<Product> products = instance.parseAll(path);
        products.stream()
                .limit(20)
                .forEach(System.out::println);
                        
    }
    
}
