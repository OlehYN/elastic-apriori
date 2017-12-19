package com.ukma.bigdata.yupro.apriori.service;

import java.util.Set;

import com.ukma.bigdata.yupro.apriori.model.Rule;

public interface RuleGenerator<Type> {
    Set<Rule<Type>> generateRules(Set<Type> itemSet);
}
