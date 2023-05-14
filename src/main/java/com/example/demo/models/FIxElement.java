package com.example.demo.models;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

@Service
public class FIxElement {
    public String reDraw() throws IOException {
            File file = new File("src/main/resources/static/js/source.json");
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            Graph graph = objectMapper.readValue(file, Graph.class);
                for (Node node : graph.getNodes()) {
                    if(!node.getState().equals("succeed")){
                        node.setState("succeed");
                        System.out.println("set new state");
                    }
                }
            objectMapper.writeValue(file, graph);
            return objectMapper.writeValueAsString(graph);
    }
}
