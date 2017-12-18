package com.ukma.bigdata.yupro.apriori.utils.csv;

import java.io.IOException;
import java.util.List;
import org.junit.Test;


public class CsvOrderParserTest {
    
    
    @Test
    public void testParseAll() throws IOException {
        System.out.println("parseAll");
        String path = this.getClass().getResource(
                "/csv/orders.csv").getPath();
        CsvOrderParser instance = new CsvOrderParser(path);
        List<Order> result = instance.parseAll();
        result.stream()
                .forEach(System.out::println);
    }
    
}
