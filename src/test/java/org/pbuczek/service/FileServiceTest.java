package org.pbuczek.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

class FileServiceTest {

    @Test
    void downloadPostsToJsonFilesSimpleTest() throws IOException {
        // Mock the getJsonFromUrlAddress method
        String mockedJsonResponse = "[{\"userId\": 1, \"id\": 1, \"title\": \"Test Title\", \"body\": \"Test Body\"}]";
        DataService dataService = Mockito.spy(new DataService(new ObjectMapper()));
        when(dataService.getJsonFromUrlAddress("https://jsonplaceholder.typicode.com/posts"))
                .thenReturn(mockedJsonResponse);

        FileService.setDataService(dataService);

        String folderPath = FileService.downloadPostsToJsonFiles();

        File file = new File(folderPath + "/1.json");
        assertTrue(file.exists());
        // assertions to make - is file created and are its contents what we expect
    }
}