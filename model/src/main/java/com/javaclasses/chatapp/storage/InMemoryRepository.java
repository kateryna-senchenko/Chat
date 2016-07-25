package com.javaclasses.chatapp.storage;

import java.util.HashMap;
import java.util.Map;

/**
 * Abstract class of in memory repository
 */
abstract class InMemoryRepository<TypeId, Type> implements Repository<TypeId, Type> {

    private Map<TypeId, Type> entities = new HashMap();


    @Override
    public Type getItem(TypeId id) {
        return entities.get(id);
    }

    @Override
    public void add(TypeId id, Type item) {
        entities.put(id, item);
    }
}
