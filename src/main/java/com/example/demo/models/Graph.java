package com.example.demo.models;

import java.util.ArrayList;

public class Graph {
    private ArrayList<Node> nodes;
    private ArrayList<Link> links;

    public Graph(ArrayList<Node> nodes, ArrayList<Link> links) {
        this.nodes = nodes;
        this.links = links;
    }

    public Graph() {
    }

    public ArrayList<Node> getNodes() {
        return nodes;
    }

    public ArrayList<Link> getLinks() {
        return links;
    }

    public void setLinks(ArrayList<Link> links) {
        this.links = links;
    }

    @Override
    public String toString() {
        return "{" +
                "'nodes':" + nodes +
                ", 'links':" + links +
                '}';
    }
}
