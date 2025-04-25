package com.example.mutualfollowers.model;

import java.util.List;

public class User {
    private int id;
    private String name;
    private List<Integer> follows;

    public User() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getFollows() {
        return follows;
    }

    public void setFollows(List<Integer> follows) {
        this.follows = follows;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", follows=" + follows +
                '}';
    }
}