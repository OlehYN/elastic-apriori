package com.ukma.bigdata.yupro.apriori.service.impl;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.ukma.bigdata.yupro.apriori.model.Rule;
import com.ukma.bigdata.yupro.apriori.service.RuleGenerator;

@Component
public class RuleGeneratorImpl implements RuleGenerator<Long> {

    @Override
    public Set<Rule<Long>> generateRules(Set<Long> itemSet) {
	Set<Rule<Long>> result = new HashSet<>();
	Set<Set<Long>> allSubsets = allSubsets(itemSet);
	for (Set<Long> subset : allSubsets) {
	    Rule<Long> rule = new Rule<>();
	    rule.setSourceItems(subset);
	    rule.setTargetItems(findOther(itemSet, subset));

	    result.add(rule);
	}
	return result;
    }

    public Set<Long> findOther(Set<Long> itemSet, Set<Long> subset) {
	return itemSet.stream().filter(value -> !subset.contains(value)).collect(Collectors.toSet());
    }

    public Set<Set<Long>> allSubsets(Set<Long> itemSet) {
	Set<Set<Long>> resultSet = new HashSet<>();
	resultSet.add(new HashSet<>());

	for (Long item : itemSet) {
	    Set<Set<Long>> copy = new HashSet<>();
	    for (Set<Long> subset : resultSet) {
		copy.add(new HashSet<>(subset));
		subset.add(item);
	    }
	    resultSet.addAll(copy);
	}

	return resultSet.stream().filter(set -> set.size() != 0 && set.size() != itemSet.size())
		.collect(Collectors.toSet());
    }
}
