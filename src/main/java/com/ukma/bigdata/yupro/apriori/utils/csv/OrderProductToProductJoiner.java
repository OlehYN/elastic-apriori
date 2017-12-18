package com.ukma.bigdata.yupro.apriori.utils.csv;

import com.ukma.bigdata.yupro.apriori.utils.OneToOneJoiner;
import java.util.Collection;
import java.util.List;


public class OrderProductToProductJoiner implements OneToOneJoiner<OrderProduct, Product>{

    @Override
    public Collection<OrderProduct> getJoin(Collection<OrderProduct> entities1,
            Collection<Product> entities2) {
        List<OrderProduct> result = null;
        return result;
    }

    @Override
    public Collection<Product> getReversedJoin(
            Collection<OrderProduct> entities1, Collection<Product> entities2) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
