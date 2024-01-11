package org.pbuczek;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.pbuczek.exception.DuplicateIdException;
import org.pbuczek.post.Post;
import org.pbuczek.service.DataService;
import org.pbuczek.service.FileService;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        DataService dataService = new DataService(new ObjectMapper());
        FileService fileService = new FileService();

        try {
            String jsonPosts = dataService.getJsonFromUrlAddress("https://jsonplaceholder.typicode.com/posts");
            List<Post> posts = dataService.mapJsonToPosts(jsonPosts);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy_MM(MMM)_dd_HH_mm_ss");
            String folderPath = "results/posts_" + formatter.format(java.time.LocalDateTime.now());
            fileService.createDirectory(folderPath);

            dataService.savePostsToFiles(posts, folderPath);
        } catch (DuplicateIdException | IOException e) {
            e.printStackTrace();
        }
    }
}