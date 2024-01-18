package org.pbuczek.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.pbuczek.exception.DuplicateIdException;
import org.pbuczek.post.Post;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FileService {
    private DataService dataService = new DataService(new ObjectMapper());
    public static final String JSON_PLACEHOLDER_POSTS_URL_ADDRESS = "https://jsonplaceholder.typicode.com/posts";
    public static final int MAX_NUMBER_OF_POSTS_PER_USER = 3;

    public void setDataService(DataService dataService) {
        this.dataService = dataService;
    }

    public void createDirectory(String folderPath) throws IOException {
        try {
            Path path = Paths.get(folderPath);
            Files.createDirectories(path);

        } catch (IOException e) {
            throw new IOException("Failed to create directory:" + folderPath + e.getMessage());
        }
    }

    public String downloadPostsToJsonFiles() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy_MM(MMM)_dd_HH_mm_ss");
        String folderPath = "results/posts_" + formatter.format(LocalDateTime.now());

        try {
            createDirectory(folderPath);
            String jsonPosts = dataService.getJsonFromUrlAddress(JSON_PLACEHOLDER_POSTS_URL_ADDRESS);
            List<Post> posts = dataService.mapJsonToPosts(jsonPosts);
            Map<Integer, List<Post>> postsByUser = posts.stream()
                    .collect(Collectors.groupingBy(Post::getUserId));

            for (Map.Entry<Integer, List<Post>> entry : postsByUser.entrySet()) {
                int userId = entry.getKey();
                List<Post> limitedUserPosts = entry.getValue().stream().limit(MAX_NUMBER_OF_POSTS_PER_USER).toList();
                dataService.savePostsToFiles(limitedUserPosts, userId, folderPath);
            }

        } catch (DuplicateIdException | IOException e) {
            e.printStackTrace();
        }
        return folderPath;
    }
}