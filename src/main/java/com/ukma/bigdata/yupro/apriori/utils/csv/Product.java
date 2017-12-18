package com.ukma.bigdata.yupro.apriori.utils.csv;

public class Product {
    
    private String productId;
    private String productName;
    private String aisleId;

    public Product(String productId, String productName, String aisleId) {
        this.productId = productId;
        this.productName = productName;
        this.aisleId = aisleId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getAisleId() {
        return aisleId;
    }

    public void setAisleId(String aisleId) {
        this.aisleId = aisleId;
    }

    @Override
    public String toString() {
        return "Product{" + "productId=" + productId + ", productName=" +
                productName + ", aisleId=" + aisleId + '}';
    }

}