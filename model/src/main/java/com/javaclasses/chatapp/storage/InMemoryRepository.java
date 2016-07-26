package com.javaclasses.chatapp.storage;

import java.util.*;

/**
 * Abstract class of in memory repository
 */
abstract class InMemoryRepository<TypeId, Type> implements Repository<TypeId, Type> {

    private Map<TypeId, Type> entities = Collections.synchronizedMap(new HashMap<>());

    @Override
    public Type getItem(TypeId id) {
        return entities.get(id);
    }

    @Override
    public void add(TypeId id, Type item) {
        entities.put(id, item);
    }

    @Override
    public Collection<Type> getAll() {
        return entities.values();
    }
}
