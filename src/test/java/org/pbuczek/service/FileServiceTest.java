package org.pbuczek.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

class FileServiceTest {
    private static String folderPath = "";

    @AfterEach
    @SneakyThrows
    void tearDown() {
        FileUtils.deleteDirectory(new File(folderPath));
    }

    @Test
    void shouldSaveJsonFileWhenGivenOnePost() {
        // given
        String expectedFileContents = "{\"userId\":1,\"id\":1,\"title\":\"Test Title\",\"body\":\"Test Body\"}";
        FileService fileService = new FileService();
        mockGetJsonFromUrlAddressMethod(fileService,
                "[{\"userId\": 1, \"id\": 1, \"title\": \"Test Title\", \"body\": \"Test Body\"}]");

        // when
        folderPath = fileService.downloadPostsToJsonFiles();

        // then
        assertPostFileIsCorrect(folderPath + "/1.json", expectedFileContents);
    }

    @Test
    void shouldSaveThreeJsonFilesWhenGivenThreePosts() {
        // given
        String expectedFileOneContents = "{\"userId\":1,\"id\":1,\"title\":\"Test Title1\",\"body\":\"Test Body\"}";
        String expectedFileTwoContents = "{\"userId\":1,\"id\":2,\"title\":\"Test Title2\",\"body\":\"Test Body\"}";
        String expectedFileThreeContents = "{\"userId\":2,\"id\":3,\"title\":\"Test Title3\",\"body\":\"Test Body\"}";
        FileService fileService = new FileService();
        mockGetJsonFromUrlAddressMethod(fileService,
                "[{\"userId\": 1, \"id\": 1, \"title\": \"Test Title1\", \"body\": \"Test Body\"}," +
                        "{\"userId\": 1, \"id\": 2, \"title\": \"Test Title2\", \"body\": \"Test Body\"}," +
                        "{\"userId\": 2, \"id\": 3, \"title\": \"Test Title3\", \"body\": \"Test Body\"}]");

        // when
        folderPath = fileService.downloadPostsToJsonFiles();

        // then
        assertPostFileIsCorrect(folderPath + "/1.json", expectedFileOneContents);
        assertPostFileIsCorrect(folderPath + "/2.json", expectedFileTwoContents);
        assertPostFileIsCorrect(folderPath + "/3.json", expectedFileThreeContents);
    }

    @SneakyThrows
    private void assertPostFileIsCorrect(String filePath, String expectedFileContents) {
        File file = new File(filePath);
        assertTrue(file.exists());
        assertEquals(expectedFileContents, FileUtils.readFileToString(file, "utf-8"));
    }

    @SneakyThrows
    private void mockGetJsonFromUrlAddressMethod(FileService fileService, String mockedJsonResponse) {
        DataService dataService = Mockito.spy(new DataService(new ObjectMapper()));
        when(dataService.getJsonFromUrlAddress("https://jsonplaceholder.typicode.com/posts"))
                .thenReturn(mockedJsonResponse);
        fileService.setDataService(dataService);
    }
}