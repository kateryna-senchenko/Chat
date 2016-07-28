package com.javaclasses.chatapp.storage;

import java.util.Collection;

/**
 * Public API of Repository
 */
public interface Repository<TypeId, Type> {

    Type getItem(TypeId id);

    TypeId add(Type item);

    Collection<Type> getAll();


}
