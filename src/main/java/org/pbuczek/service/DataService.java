package org.pbuczek.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.pbuczek.exception.DuplicateIdException;
import org.pbuczek.post.Post;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
public class DataService {
    private ObjectMapper mapper;

    @SuppressWarnings("SameParameterValue")
    public String getJsonFromUrlAddress(String urlAddress) throws IOException {
        try (InputStream inputStream = new URL(urlAddress).openStream()) {
            return IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        }
    }

    public List<Post> mapJsonToPosts(String jsonPosts) throws DuplicateIdException, JsonProcessingException {
        List<Post> posts = mapper.readValue(jsonPosts, new TypeReference<>() {
        });

        Set<Integer> setOfPostIds = posts.stream()
                .map(Post::getId)
                .collect(Collectors.toSet());

        if (setOfPostIds.size() < posts.size()) {
            throw new DuplicateIdException("Returned jsonObject contains duplicated post ids");
        }

        return posts;
    }

    public void savePostsToFiles(List<Post> posts, int userId, String folderPath) throws IOException {
        mapper.writerWithDefaultPrettyPrinter().writeValue(
                new File(folderPath + "/user_" + userId + ".json"),
                posts);
    }
}
