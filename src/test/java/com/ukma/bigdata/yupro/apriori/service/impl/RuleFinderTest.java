package com.ukma.bigdata.yupro.apriori.service.impl;

import com.ukma.bigdata.yupro.apriori.config.Config;

import java.io.IOException;
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
	System.out.println(ruleFinder.findRules(0.2, 3));

    }
}
