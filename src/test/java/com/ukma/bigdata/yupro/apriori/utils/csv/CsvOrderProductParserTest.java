package com.ukma.bigdata.yupro.apriori.utils.csv;

import java.util.List;
import org.junit.Test;

public class CsvOrderProductParserTest {

    @Test
    public void testParseAll() {
        System.out.println("parseAll");
        String path = this.getClass().getResource(
                "/csv/order_products__prior.csv").getPath();
        CsvOrderProductParser instance = new CsvOrderProductParser();
        List<OrderProduct> result = instance.parseAll(path);
        result.stream()
                .limit(20)
                .forEach(System.out::println);
    }

}
