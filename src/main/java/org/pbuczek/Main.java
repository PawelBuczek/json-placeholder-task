package org.pbuczek;

import org.pbuczek.service.DataService;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            DataService dataService = new DataService();
            String jsonPosts = dataService.getJsonFromUrlAddress("https://jsonplaceholder.typicode.com/posts");
            System.out.println(jsonPosts);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}