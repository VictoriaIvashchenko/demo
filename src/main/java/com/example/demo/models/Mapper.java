package com.example.demo.models;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

@Service
public class Mapper {
    public String reDraw(ArrayList<ArrayList<Integer>> newStates) throws IOException {
        if(newStates.size() > 0) {
            File file = new File("src/main/resources/static/js/source.json");
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            Graph graph = objectMapper.readValue(file, Graph.class);
            for (int i = 0; i < newStates.size(); i++) {
                for (Node node : graph.getNodes()) {
                    if (node.getId() == newStates.get(i).get(0)) {
                        if (newStates.get(i).get(1) == 0) {
                            node.setState("succeed");
                        }
                        if (newStates.get(i).get(1) == 1) {
                            node.setState("failure");
                            System.out.println(node.getId() + " failure");
                        }
                        if (newStates.get(i).get(1) == 2) {
                            node.setState("breakdown");
                            System.out.println(node.getId() + " breakdown");

                        }
                    }
                }
            }
            objectMapper.writeValue(file, graph);
            return objectMapper.writeValueAsString(graph);
        }
        return null;
    }

}
