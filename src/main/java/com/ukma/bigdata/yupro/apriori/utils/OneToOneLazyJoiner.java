package com.ukma.bigdata.yupro.apriori.utils;

import java.util.Collection;
import java.util.Map;

public interface OneToOneLazyJoiner<Entity1, Entity2> {

    public Collection<Entity1> getJoin(LineByLineParser<Entity1> entities1,
            Map<Object, Entity2> entities2Parser);

    public Collection<Entity2> getReversedJoin(Map<Object, Entity1> entities1Parser,
            LineByLineParser<Entity2> entities2);
}
