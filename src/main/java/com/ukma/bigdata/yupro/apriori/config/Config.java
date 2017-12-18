/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ukma.bigdata.yupro.apriori.config;

import com.ukma.bigdata.yupro.apriori.model.FrequentSet;
import com.ukma.bigdata.yupro.apriori.service.AprioriStoreService;
import com.ukma.bigdata.yupro.apriori.service.TransactionProvider;
import com.ukma.bigdata.yupro.apriori.service.impl.CsvTransactionProviderImpl;
import com.ukma.bigdata.yupro.apriori.service.impl.ElasticAprioriStoreService;

import java.io.FileNotFoundException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
@ComponentScan(basePackages = "com.ukma.bigdata.yupro.apriori")
public class Config {

    @Bean("client")
    public TransportClient getClient() throws UnknownHostException {
	return new PreBuiltTransportClient(Settings.EMPTY)
		.addTransportAddress(new TransportAddress(InetAddress.getByName("localhost"), 9300));
    }

    @Bean("transactionProvider")
    public TransactionProvider<Long, Long> getTransactionProvider() throws FileNotFoundException {
	return new CsvTransactionProviderImpl("test.csv", 0, 1, ',', '"');
    }

    @Bean("candidateCache")
    @Scope("prototype")
    public Map<Integer, Queue<Set<Long>>> getCandidatesCache() {
	return new HashMap<>();
    }

    @Bean("frequentSetCache")
    @Scope("prototype")
    public Map<Integer, Queue<FrequentSet<Long>>> getFrequentSetCache() {
	return new HashMap<>();
    }

    @Bean("candidateIndexName")
    public String getCandidateIndexName() {
	return "dry-candidate".intern();
    }

    @Bean("transactionIndexName")
    public String getTransactionIndexName() {
	return ".transaction".intern();
    }

}
