package com.ukma.bigdata.yupro.apriori.service;

import java.util.Map;

public interface DataProvider {
    Map<String, String> nextTransaction();
}
