package com.ukma.bigdata.yupro.apriori.service;

import java.util.Set;

import com.ukma.bigdata.yupro.apriori.model.Rule;

public interface RuleFinder<Type> {
    Set<Rule<Type>> findRules(double minSupport, int levels);
}
