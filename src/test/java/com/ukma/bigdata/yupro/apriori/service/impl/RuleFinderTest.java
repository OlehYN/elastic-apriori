package com.ukma.bigdata.yupro.apriori.service.impl;

import com.ukma.bigdata.yupro.apriori.config.Config;
import com.ukma.bigdata.yupro.apriori.model.Rule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.ukma.bigdata.yupro.apriori.service.RuleFinder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Config.class)
public class RuleFinderTest {

    @Autowired
    private RuleFinder<Long> ruleFinder;

    @Test
    public void test() throws IOException {
	List<Rule<Long>> rules = new ArrayList<>(ruleFinder.findRules(0.2, 3));
	rules.sort((r1, r2) -> Double.valueOf(r1.getConfidence()).compareTo(Double.valueOf(r2.getConfidence())));
	System.out.println(rules);
    }
}
