package com.javaclasses.chatapp.storage;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Abstract class of the in memory repository
 */
abstract class InMemoryRepository<TypeId, Type> implements Repository<TypeId, Type> {

    private Map<TypeId, Type> entities = new ConcurrentHashMap<>();

    @Override
    public Type getItem(TypeId id) {
        return entities.get(id);
    }

    @Override
    public TypeId add(Type item) {
        TypeId id = generateId();
        entities.put(id, item);
        return id;
    }

    @Override
    public Collection<Type> getAll() {
        return entities.values();
    }

    public abstract TypeId generateId();

    @Override
    public Type remove(TypeId id) {
        return entities.remove(id);
    }
}
