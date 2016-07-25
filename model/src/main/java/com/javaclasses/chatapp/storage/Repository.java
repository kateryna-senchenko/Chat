package com.javaclasses.chatapp.storage;

/**
 * Public API of Repository
 */
public interface Repository<TypeId, Type> {

    Type getItem(TypeId id);

    void add(TypeId id, Type item);


}
