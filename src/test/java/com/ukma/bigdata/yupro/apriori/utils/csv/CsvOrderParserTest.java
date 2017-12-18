package com.ukma.bigdata.yupro.apriori.utils.csv;

import java.util.List;
import org.junit.Test;


public class CsvOrderParserTest {
    
    
    @Test
    public void testParseAll() {
        System.out.println("parseAll");
        String path = this.getClass().getResource(
                "/csv/orders.csv").getPath();
        CsvOrderParser instance = new CsvOrderParser();
        List<Order> result = instance.parseAll(path);
        result.stream()
                .forEach(System.out::println);
    }
    
}
