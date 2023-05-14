package com.example.demo.models;

public class Link {
    public int source;
    public int target;

    public Link(int source, int target) {
        this.source = source;
        this.target = target;
    }

    public Link() {
    }

    @Override
    public String toString() {
        return "{" +
                "source:" + source +
                ", target:" + target +
                '}';
    }
}
