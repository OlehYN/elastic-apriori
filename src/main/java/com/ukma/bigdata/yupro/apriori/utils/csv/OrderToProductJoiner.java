package com.ukma.bigdata.yupro.apriori.utils.csv;

import com.ukma.bigdata.yupro.apriori.utils.ManyToManyJoiner;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OrderToProductJoiner implements ManyToManyJoiner<Product, OrderProduct, Order>{
    
    @Override
    public Collection<Order> getReversedJoin(Collection<Order> orders,
            Collection<OrderProduct> orderProducts,
            Collection<Product> products) {
        Map<String, List<OrderProduct>> map = orderProducts.stream()
                .collect(Collectors.groupingBy(OrderProduct::getOrderId));
        Map<String, Product> productsMap = products.stream()
                .collect(Collectors.toMap(Product::getProductId, p -> p));
        return orders.stream()
                .peek(o -> {
                    o.setProducts(map.get(o.getOrderId()).stream()
                            .map(op -> productsMap.get(op.getProductId()))
                            .collect(Collectors.toList()));
                }).collect(Collectors.toList());
    }

    @Override
    public Collection<Product> getJoin(Collection<Product> en1,
            Collection<OrderProduct> en1ToEn2, Collection<Order> en2) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
