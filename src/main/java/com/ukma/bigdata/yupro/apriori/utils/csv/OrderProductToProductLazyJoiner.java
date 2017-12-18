package com.ukma.bigdata.yupro.apriori.utils.csv;

import com.ukma.bigdata.yupro.apriori.utils.LineByLineParser;
import com.ukma.bigdata.yupro.apriori.utils.OneToOneLazyJoiner;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class OrderProductToProductLazyJoiner implements OneToOneLazyJoiner<OrderProduct, Product> {

    @Override
    public Collection<OrderProduct> getJoin(
            LineByLineParser<OrderProduct> parser,
            Map<Object, Product> products) {
        List<OrderProduct> result = new ArrayList<>();
        while (parser.hasNext()) {
            OrderProduct op = parser.next();
            op.setProduct(products.get((String) op.getProductId()));
            result.add(op);
        }
        return result;
    }

    @Override
    public Collection<Product> getReversedJoin(
            Map<Object, OrderProduct> entities,
            LineByLineParser<Product> parser) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
