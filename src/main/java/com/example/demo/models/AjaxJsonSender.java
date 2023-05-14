package com.example.demo.models;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class AjaxJsonSender {

    public void sendJson(String stringJson) throws IOException {
        // Set up the AJAX request
        URL url = new URL("http://localhost:8081/");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        // Send the JSON string in the request body
        OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
        out.write(stringJson);
        out.close();
    }
}
