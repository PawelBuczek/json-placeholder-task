package org.pbuczek.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.pbuczek.exception.DuplicateIdException;
import org.pbuczek.post.Post;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class FileService {
    public static void createDirectory(String folderPath) throws IOException {
        try {
            Path path = Paths.get(folderPath);
            Files.createDirectories(path);

        } catch (IOException e) {
            throw new IOException("Failed to create directory:" + folderPath + e.getMessage());
        }
    }

    public static void downloadPostsToJsonFiles() {
        DataService dataService = new DataService(new ObjectMapper());
        try {
            String jsonPosts = dataService.getJsonFromUrlAddress("https://jsonplaceholder.typicode.com/posts");
            List<Post> posts = dataService.mapJsonToPosts(jsonPosts);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy_MM(MMM)_dd_HH_mm_ss");
            String folderPath = "results/posts_" + formatter.format(java.time.LocalDateTime.now());
            createDirectory(folderPath);

            dataService.savePostsToFiles(posts, folderPath);
        } catch (DuplicateIdException | IOException e) {
            e.printStackTrace();
        }
    }
}
