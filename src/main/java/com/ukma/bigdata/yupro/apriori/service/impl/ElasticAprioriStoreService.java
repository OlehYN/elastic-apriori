package com.ukma.bigdata.yupro.apriori.service.impl;

import com.ukma.bigdata.yupro.apriori.model.CandidateIterator;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import javax.annotation.PostConstruct;

import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

import com.ukma.bigdata.yupro.apriori.model.FrequentSet;
import com.ukma.bigdata.yupro.apriori.model.FrequentSetIterator;
import com.ukma.bigdata.yupro.apriori.model.Transaction;
import com.ukma.bigdata.yupro.apriori.service.AprioriStoreService;
import com.ukma.bigdata.yupro.apriori.service.TransactionProvider;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import static org.elasticsearch.common.xcontent.XContentFactory.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class ElasticAprioriStoreService implements AprioriStoreService<Long, Long> {

    @Autowired
    private TransportClient client;

    @Autowired
    @Qualifier("candidateCache")
    private Map<Integer, Queue<Set<Long>>> candidateCache;

    @Autowired
    @Qualifier("frequentSetCache")
    private Map<Integer, Queue<FrequentSet<Long>>> frequentSetCache;

    @Autowired
    @Qualifier("candidateIndexName")
    private String candidateIndexName;

    @Autowired
    @Qualifier("transactionIndexName")
    private String transactionIndexName;

    private final TransactionProvider<Long, Long> transactionProvider = new CsvTransactionProviderImpl("test.csv", 0, 1,
	    ',', '"');

    private static final String CANDIDATE_TYPE = "candidate";
    private static final String TRANSACTION_TYPE = "transaction";

    private static final int transactionBufferSize = 5000;

    private long size = 0;

    public ElasticAprioriStoreService() throws Exception {
    }

    @PostConstruct
    public void init() throws IOException, InterruptedException, ExecutionException {
	Transaction<Long, Long> transaction = transactionProvider.nextTransaction();
	BulkRequestBuilder bulkRequest = client.prepareBulk();

	int bulkSize = 0;
	if (!client.admin().indices().exists(new IndicesExistsRequest(transactionIndexName)).get().isExists()) {
	    do {
		if (bulkSize == transactionBufferSize) {
		    bulkSize = 0;
		    BulkResponse bulkResponse = bulkRequest.get();
		    if (bulkResponse.hasFailures()) {
			throw new IllegalArgumentException(bulkResponse.buildFailureMessage());
		    }
		    bulkRequest = client.prepareBulk();
		}

		bulkRequest.add(client
			.prepareIndex(transactionIndexName, TRANSACTION_TYPE,
				String.valueOf(transaction.getTransactionKey()))
			.setSource(jsonBuilder().startObject().field("transactionId", transaction.getTransactionKey())
				.field("transactionValues", transaction.getTransactionValue()).endObject()));

		++bulkSize;
	    } while ((transaction = transactionProvider.nextTransaction()) != null);

	    if (bulkSize != 0) {
		BulkResponse bulkResponse = bulkRequest.get();
		if (bulkResponse.hasFailures()) {
		    throw new IllegalArgumentException(bulkResponse.buildFailureMessage());
		}
		bulkRequest = client.prepareBulk();
	    }
	}
	size = (int) client.prepareSearch(this.transactionIndexName).setIndicesOptions(IndicesOptions.strictExpand())
		.setSize(0).execute().get().getHits().getTotalHits();
    }

    @Override
    public Iterator<Set<Long>> candidateIterator(int level) {
	if (!candidateCache.containsKey(level)) {
	    candidateCache.put(level, new LinkedList<>());
	}
	return new CandidateIterator(client, candidateIndexName, level);
    }

    @Override
    public Iterator<FrequentSet<Long>> frequentSetIterator(int level) {
	if (!frequentSetCache.containsKey(level)) {
	    frequentSetCache.put(level, new LinkedList<>());
	}
	return new FrequentSetIterator(client, candidateIndexName, level);
    }

    public TransportClient getClient() {
	return client;
    }

    @Override
    public void removeCandidate(Set<Long> itemSet) {
	SearchHits searchHits;
	try {
	    searchHits = client.prepareSearch(candidateIndexName + (itemSet.size() - 1))
		    .setQuery(generateQuery(itemSet)).setTypes(CANDIDATE_TYPE).execute().get().getHits();
	} catch (Exception e) {
	    throw new RuntimeException(e);
	}
	String id = searchHits.getAt(0).getId();

	try {
	    client.prepareDelete(candidateIndexName + (itemSet.size() - 1), CANDIDATE_TYPE, id).execute().get();
	} catch (Exception e) {
	    throw new RuntimeException(e);
	}
    }

    @Override
    public void saveCandidate(Set<Long> itemSet) {
	try {
	    String id = client.prepareIndex(candidateIndexName + (itemSet.size() - 1), CANDIDATE_TYPE)
		    .setSource(jsonBuilder().startObject().field("transactionValues", itemSet).endObject()).execute()
		    .get().getId();
	    System.out.println("created id: " + id);
	} catch (Exception e) {
	    throw new RuntimeException(e);
	}
    }

    @Override
    public void updateCandidate(Set<Long> itemSet, double support) {
	SearchHits searchHits;
	try {
	    searchHits = client.prepareSearch(candidateIndexName + (itemSet.size() - 1))
		    .setQuery(generateQuery(itemSet)).setTypes(CANDIDATE_TYPE).execute().get().getHits();
	} catch (Exception e) {
	    throw new RuntimeException(e);
	}
	if (searchHits.getTotalHits() == 0L) {
	    throw new IllegalArgumentException("itemSet does not exist");
	}
	String id = searchHits.getAt(0).getId();
	try {
	    client.prepareIndex(candidateIndexName + (itemSet.size() - 1), CANDIDATE_TYPE, id).setSource(jsonBuilder()
		    .startObject().field("transactionValues", itemSet).field("support", support).endObject()).execute()
		    .get();
	} catch (Exception e) {
	    throw new RuntimeException(e);
	}
    }

    @Override
    public double getSupport(Set<Long> itemSet) {
	SearchHits searchHits;
	try {
	    searchHits = client.prepareSearch(candidateIndexName + (itemSet.size() - 1))
		    .setQuery(generateQuery(itemSet)).setTypes(CANDIDATE_TYPE).execute().get().getHits();
	} catch (Exception e) {
	    throw new RuntimeException(e);
	}

	if (searchHits.getTotalHits() == 0) {
	    throw new IllegalArgumentException("itemSet does not exist");
	}
	return (double) searchHits.getAt(0).getSourceAsMap().get("support");
    }

    @Override
    public boolean exists(Set<Long> itemSet) {
	SearchHits searchHits;
	try {
	    searchHits = client.prepareSearch(candidateIndexName + (itemSet.size() - 1))
		    .setQuery(generateQuery(itemSet)).setTypes(CANDIDATE_TYPE).execute().get().getHits();
	} catch (Exception e) {
	    throw new RuntimeException(e);
	}

	return searchHits.getTotalHits() != 0;
    }

    public QueryBuilder generateQuery(Collection<Long> itemSet) {
	if (itemSet.size() == 0) {
	    throw new IllegalArgumentException("Zero item set");
	}

	BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();

	for (Long id : itemSet) {
	    queryBuilder.must(QueryBuilders.termQuery("transactionValues", id));
	}

	return queryBuilder;
    }

    @Override
    public long getSize() {
	return this.size;
    }

    public void close() {
	client.close();
    }

    @Override
    public long countTransactions(Set<Long> itemSet) {
	try {
	    return client.prepareSearch(transactionIndexName).setIndicesOptions(IndicesOptions.strictExpand())
		    .setQuery(generateQuery(itemSet)).execute().get().getHits().getTotalHits();
	} catch (Exception e) {
	    throw new RuntimeException(e);
	}
    }

    @Override
    public Set<Long> findOthers(List<Long> items) {
	Set<Long> result = new HashSet<>();
	SearchHit[] searchHits;
	try {
	    searchHits = client.prepareSearch(candidateIndexName + items.size())
		    .setIndicesOptions(IndicesOptions.strictExpand()).setQuery(generateQuery(items)).execute().get()
		    .getHits().getHits();
	} catch (Exception e) {
	    throw new RuntimeException(e);
	}

	for (SearchHit searchHit : searchHits) {
	    List<Integer> intTransactionValues = (List<Integer>) searchHit.getSourceAsMap().get("transactionValues");
	    List<Long> transactionValues = new ArrayList<>();
	    Arrays.stream(intTransactionValues.stream().mapToLong(i -> i).toArray())
		    .forEach(lValue -> transactionValues.add(lValue));
	    transactionValues.removeAll(items);

	    result.add(transactionValues.get(0));
	}

	return result;
    }

}
