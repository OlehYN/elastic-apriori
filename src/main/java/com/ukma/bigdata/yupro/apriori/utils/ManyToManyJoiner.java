package com.ukma.bigdata.yupro.apriori.utils;

import java.util.Collection;

public interface ManyToManyJoiner<Entity1, Entity1ToEntity2, Entity2> {

    public Collection<Entity1> getJoined(Collection<Entity1> en1,
            Collection<Entity1ToEntity2> en1ToEn2, Collection<Entity2> en2);
    
    public Collection<Entity2> getJoinedReversed(Collection<Entity2> en1,
            Collection<Entity1ToEntity2> en1ToEn2, Collection<Entity1> en2);
}
