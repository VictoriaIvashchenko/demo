package com.example.demo.models;

public class Node {
    private int id;
    private String name;
    private String state;

    public Node() {
    }

    public Node(int id, String name, String state) {
        this.id = id;
        this.name = name;
        this.state = state;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "{" +
                "id:" + id +
                ", name:'" + name + '\'' +
                ", state:'" + state + '\'' +
                '}';
    }
}
