package com.ukma.bigdata.yupro.apriori;

import com.ukma.bigdata.yupro.apriori.config.Config;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {Config.class})
@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class})
public class ContextTest {

}