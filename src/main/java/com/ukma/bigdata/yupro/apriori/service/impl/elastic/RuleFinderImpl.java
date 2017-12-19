package com.ukma.bigdata.yupro.apriori.service.impl.elastic;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ukma.bigdata.yupro.apriori.model.FrequentSet;
import com.ukma.bigdata.yupro.apriori.model.Rule;
import com.ukma.bigdata.yupro.apriori.service.RuleFinder;
import com.ukma.bigdata.yupro.apriori.service.RuleGenerator;
import com.ukma.bigdata.yupro.apriori.service.impl.ElasticAprioriStoreService;

@Component
public class RuleFinderImpl implements RuleFinder<Long> {

    @Autowired
    private ElasticAprioriStoreService elasticAprioriStoreService;

    @Autowired
    private RuleGenerator<Long> ruleGenerator;

    @Override
    public Set<Rule<Long>> findRules(double minSupport, int levels) {
	Set<Rule<Long>> result = new HashSet<>();
	for (int i = 1; i < levels; i++) {
	    Iterator<FrequentSet<Long>> iterator = elasticAprioriStoreService.frequentSetIterator(i);

	    while (iterator.hasNext()) {
		FrequentSet<Long> frequentSet = iterator.next();
		Set<Rule<Long>> rules = ruleGenerator.generateRules(frequentSet.getItems());

		for (Rule<Long> rule : rules) {
		    double sourceSupport = elasticAprioriStoreService.getSupport(rule.getSourceItems());
		    double targetSupport = frequentSet.getSupport();
		    double support = targetSupport / sourceSupport;

		    rule.setConfidence(support);
		    if (support > minSupport) {
			result.add(rule);
		    }

		}
	    }
	}

	return result;
    }

}
