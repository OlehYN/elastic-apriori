package com.ukma.bigdata.yupro.apriori.utils.csv;

import java.util.ArrayList;
import java.util.List;

public class Order {

    private String orderId;
    private String userId;
    private String orderNumber;
    private List<Product> products;

    public Order(String orderId, String userId, String orderNumber) {
        this.orderId = orderId;
        this.userId = userId;
        this.orderNumber = orderNumber;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public void addProduct(Product product) {
        if (this.products == null) {
            this.products = new ArrayList<>();
        }
        this.products.add(product);
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
    
    public List<Product> getProducts() {
        return products;
    }

    @Override
    public String toString() {
        return "Order{" + "orderId=" + orderId + ", userId=" + userId
                + ", orderNumber=" + orderNumber + '}';
    }
}
