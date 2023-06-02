package com.example.demo.models;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;

@Component
public class Test {
    private ResponseEntity entity;

    public void testMethod() throws InterruptedException, IOException {
        System.out.println("try");
        Diagnostic diagnostic = new Diagnostic();
        Mapper mapper = new Mapper();
        ArrayList<ArrayList<Integer>> result = diagnostic.work(1);
        String json = mapper.reDraw(result);
        entity = new ResponseEntity(json, HttpStatus.OK);

    }

    public ResponseEntity getEntity() {
        return entity;
    }
}
