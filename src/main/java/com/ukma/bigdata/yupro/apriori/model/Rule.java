package com.ukma.bigdata.yupro.apriori.model;

import java.util.Set;

public class Rule<Type> {
    private Set<Long> sourceItems;
    private Set<Long> targetItems;

    private double confidence;

    public Set<Long> getSourceItems() {
	return sourceItems;
    }

    public void setSourceItems(Set<Long> sourceItems) {
	this.sourceItems = sourceItems;
    }

    public Set<Long> getTargetItems() {
	return targetItems;
    }

    public void setTargetItems(Set<Long> targetItems) {
	this.targetItems = targetItems;
    }

    public double getConfidence() {
	return confidence;
    }

    public void setConfidence(double confidence) {
	this.confidence = confidence;
    }

    @Override
    public String toString() {
	return "Rule [sourceItems=" + sourceItems + ", targetItems=" + targetItems + ", confidence=" + confidence + "]";
    }

}
