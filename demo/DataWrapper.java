package com.example.mutualfollowers.model;

public class DataWrapper {
    private UsersRequest users;

    public DataWrapper() {
    }

    public UsersRequest getUsers() {
        return users;
    }

    public void setUsers(UsersRequest users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "DataWrapper{" +
                "users=" + users +
                '}';
    }
}