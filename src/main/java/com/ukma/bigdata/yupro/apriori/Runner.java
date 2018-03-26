package com.ukma.bigdata.yupro.apriori;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.ukma.bigdata.yupro.apriori.config.Config;
import com.ukma.bigdata.yupro.apriori.service.AprioriService;
import com.ukma.bigdata.yupro.apriori.service.TransactionProvider;

public class Runner {

    public static void main(String[] args) {
	AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(
		Config.class);

	TransactionProvider<Long, Long> transactionProvider = (TransactionProvider<Long, Long>) annotationConfigApplicationContext
		.getBean("transactionProvider");
	AprioriService<Long, Long> aprioriService = (AprioriService<Long, Long>) annotationConfigApplicationContext
		.getBean(AprioriService.class);

	aprioriService.generateAprioriResult(transactionProvider, 2, 0.001, 0.2);
	annotationConfigApplicationContext.close();
    }

}
