/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ukma.bigdata.yupro.apriori.config;

import com.ukma.bigdata.yupro.apriori.model.FrequentSet;
import com.ukma.bigdata.yupro.apriori.service.DataProvider;
import com.ukma.bigdata.yupro.apriori.service.TransactionProvider;
import com.ukma.bigdata.yupro.apriori.service.impl.CsvDataProviderImpl;
import com.ukma.bigdata.yupro.apriori.service.impl.CsvTransactionProviderImpl;
import java.io.FileNotFoundException;
import java.io.IOException;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;

@Configuration
@ComponentScan(basePackages = "com.ukma.bigdata.yupro.apriori")
@PropertySource("apriori.properties")
public class Config {

    @Value("${elasticsearch.host}")
    private String elasticsearchHost;

    @Value("${elasticsearch.port}")
    private Integer elasticsearchPort;

    @Value("${elasticsearch.candidate.name}")
    private String candidateIndexName;

    @Value("${elasticsearch.transaction.name}")
    private String transactionIndexName;

    @Value("${transaction-provider.path}")
    private String transactionProviderPath;

    @Value("${data-provider.path}")
    private String dataProviderPath;

    @Bean("client")
    public TransportClient getClient() throws UnknownHostException {
	return new PreBuiltTransportClient(Settings.EMPTY)
		.addTransportAddress(new TransportAddress(InetAddress.getByName(elasticsearchHost), elasticsearchPort));
    }

    @Bean("transactionProvider")
    public TransactionProvider<Long, Long> getTransactionProvider() throws FileNotFoundException {
	return new CsvTransactionProviderImpl(transactionProviderPath, 0, 1, ',', '"');
    }

    @Bean("dataProvider")
    public DataProvider getDataProvider() throws IOException {
	return new CsvDataProviderImpl(dataProviderPath, ',', '"');
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
	return candidateIndexName;
    }

    @Bean("transactionIndexName")
    public String getTransactionIndexName() {
	return transactionIndexName;
    }

}
