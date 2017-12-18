package com.ukma.bigdata.yupro.apriori.utils;

import java.util.Collection;

public interface OneToOneJoiner<Entity1, Entity2> {

    public Collection<Entity1> getJoin(Collection<Entity1> entities1,
            Collection<Entity2> entities2);

    public Collection<Entity2> getReversedJoin(Collection<Entity1> entities1,
            Collection<Entity2> entities2);
}