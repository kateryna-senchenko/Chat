package com.javaclasses.chatapp.entities;

import com.javaclasses.chatapp.tinytypes.UserId;

/**
 * User entity
 */
public class User {

    private UserId id;

    private final String username;

    private final String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public UserId getId() {
        return id;
    }

    public void setId(UserId id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return id.equals(user.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
