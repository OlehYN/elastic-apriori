package com.ukma.bigdata.yupro.apriori.utils;

import java.util.Collection;

public interface LineByLineParser<Entity> {
    public Collection<Entity> parseAll(String path);
}
