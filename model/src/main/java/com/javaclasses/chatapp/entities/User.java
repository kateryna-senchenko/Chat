package com.javaclasses.chatapp.entities;

import com.javaclasses.chatapp.UserId;

/**
 * User entity
 */
public class User {

    private final UserId id;

    private String username;

    private String password;

    public User(UserId id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public UserId getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
