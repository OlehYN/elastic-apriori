package com.ukma.bigdata.yupro.apriori.model;

import java.util.Set;

public class ItemSet {
    private String id;
    private Set<Long> itemSet;

    public String getId() {
	return id;
    }

    public void setId(String id) {
	this.id = id;
    }

    public Set<Long> getItemSet() {
	return itemSet;
    }

    public void setItemSet(Set<Long> itemSet) {
	this.itemSet = itemSet;
    }

    @Override
    public String toString() {
	return "ItemSet [id=" + id + ", itemSet=" + itemSet + "]";
    }

}
