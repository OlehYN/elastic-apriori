package com.ukma.bigdata.yupro.apriori.utils;

import java.util.Collection;

public interface LineByLineParser<Entity> {
    
    public boolean hasNext();
    public Entity next();
    public Collection<Entity> parseAll();
}
