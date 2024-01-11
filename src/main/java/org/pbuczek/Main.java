package org.pbuczek;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.pbuczek.exception.DuplicateIdException;
import org.pbuczek.post.Post;
import org.pbuczek.service.DataService;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            DataService dataService = new DataService();
            String jsonPosts = dataService.getJsonFromUrlAddress("https://jsonplaceholder.typicode.com/posts");
            ObjectMapper mapper = new ObjectMapper();
            List<Post> posts = dataService.mapJsonToPosts(mapper, jsonPosts);
            System.out.println(posts);

        } catch (DuplicateIdException | IOException e) {
            e.printStackTrace();
        }
    }
}