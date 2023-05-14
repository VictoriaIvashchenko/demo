package com.example.demo.controllers;

import com.example.demo.models.*;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


@Controller
public class MainController {
    @GetMapping("/")
    public String home(Model model) throws InterruptedException, IOException {
        model.addAttribute("title", "Приклад графа");
        /*File file = new File("src/main/resources/static/js/source.json");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Graph graph = objectMapper.readValue(file, Graph.class);
        AjaxJsonSender sender = new AjaxJsonSender();
        sender.sendJson(graph.toString());*/
        return "home";
    }

    @RequestMapping(value = "/do-stuff")
    public ResponseEntity changeGraph() throws InterruptedException, IOException {
        System.out.println("Click!");
        Diagnostic diagnostic = new Diagnostic();
        Mapper mapper = new Mapper();
        ArrayList<ArrayList<Integer>> result = diagnostic.work();
        String json = mapper.reDraw(result);
        return new ResponseEntity(json, HttpStatus.OK);
    }

    @RequestMapping(value = "/refresh")
    public ResponseEntity refreshGraph() throws InterruptedException, IOException {
        System.out.println("Click!");
        FIxElement fIxElement = new FIxElement();
        String json = fIxElement.reDraw();
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
}
