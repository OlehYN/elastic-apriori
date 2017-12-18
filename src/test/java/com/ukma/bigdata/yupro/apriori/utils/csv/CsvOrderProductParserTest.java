package com.ukma.bigdata.yupro.apriori.utils.csv;

import java.io.IOException;
import java.util.List;
import org.junit.Test;

public class CsvOrderProductParserTest {

    @Test
    public void testParseAll() throws IOException {
        System.out.println("parseAll");
        String path = this.getClass().getResource(
                "/csv/order_products__prior.csv").getPath();
        CsvOrderProductParser instance = new CsvOrderProductParser(path);
        List<OrderProduct> result = instance.parseAll();
        result.stream()
                .limit(20)
                .forEach(System.out::println);
    }

}
