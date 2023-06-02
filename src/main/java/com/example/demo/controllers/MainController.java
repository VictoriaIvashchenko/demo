package com.example.demo.controllers;

import com.example.demo.models.*;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static java.lang.Thread.sleep;

@EnableScheduling
@Controller
public class MainController {
    private int k = 1;

    @GetMapping("/")
    public String home(Model model) throws InterruptedException, IOException {
        model.addAttribute("title", "Home");
        return "home";
    }

    @Scheduled(fixedRate = 15000)
    @RequestMapping(value = "/do-stuff")
    public ResponseEntity changeGraph() throws InterruptedException, IOException {
        sleep(3000);
        System.out.println("k " + k);
        Diagnostic diagnostic = new Diagnostic();
        Mapper mapper = new Mapper();
        ArrayList<ArrayList<Integer>> result = diagnostic.work(k);
        String json = mapper.reDraw(result);
        return new ResponseEntity(json, HttpStatus.OK);
    }

    @RequestMapping(value = "/refresh")
    public ResponseEntity refreshGraph() throws IOException {
        FIxElement fIxElement = new FIxElement();
        String json = fIxElement.reDraw();
        if(k<3){
            k++;
        }
        return new ResponseEntity(json, HttpStatus.OK);
    }

    @GetMapping("/redraw")
    public ResponseEntity redrawChart() throws IOException {
        File file = new File("src/main/resources/static/js/source.json");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Graph graph = objectMapper.readValue(file, Graph.class);
        return new ResponseEntity(objectMapper.writeValueAsString(graph), HttpStatus.OK);
    }

    @GetMapping("/rdraw")
    public ResponseEntity rerawChart() throws IOException {
        File file = new File("src/main/resources/static/js/source.json");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Graph graph = objectMapper.readValue(file, Graph.class);
        return new ResponseEntity(objectMapper.writeValueAsString(graph), HttpStatus.OK);
    }
}
