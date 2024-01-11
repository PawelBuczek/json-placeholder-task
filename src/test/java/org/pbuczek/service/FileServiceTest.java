package org.pbuczek.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

class FileServiceTest {

    @Test
    void downloadPostsToJsonFilesSimpleTest() throws IOException {
        // given
        String expectedFileContents = "{\"userId\":1,\"id\":1,\"title\":\"Test Title\",\"body\":\"Test Body\"}";
        String mockedJsonResponse = "[{\"userId\": 1, \"id\": 1, \"title\": \"Test Title\", \"body\": \"Test Body\"}]";
        DataService dataService = Mockito.spy(new DataService(new ObjectMapper()));
        when(dataService.getJsonFromUrlAddress("https://jsonplaceholder.typicode.com/posts"))
                .thenReturn(mockedJsonResponse);
        FileService fileService = new FileService();
        fileService.setDataService(dataService);

        // when
        String folderPath = fileService.downloadPostsToJsonFiles();

        // then
        File file = new File(folderPath + "/1.json");
        assertTrue(file.exists());
        assertEquals(expectedFileContents, FileUtils.readFileToString(file, "utf-8"));
    }
}