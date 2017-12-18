package com.ukma.bigdata.yupro.apriori.model;

import java.util.Set;

public class FrequentSet<K> {
    private double support;
    private Set<K> items;
    private String id;

    public String getId() {
	return id;
    }

    public void setId(String id) {
	this.id = id;
    }

    public double getSupport() {
	return support;
    }

    public void setSupport(double support) {
	this.support = support;
    }

    public Set<K> getItems() {
	return items;
    }

    public void setItems(Set<K> items) {
	this.items = items;
    }

    @Override
    public String toString() {
	return "FrequentSet [support=" + support + ", items=" + items + "]";
    }

}
