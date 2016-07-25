package com.javaclasses.chatapp;

/**
 * Abstract class for entity id
 */
abstract class EntityId {

    private final long id;

    public EntityId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

}
