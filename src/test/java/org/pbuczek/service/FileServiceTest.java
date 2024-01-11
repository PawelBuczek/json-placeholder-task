package org.pbuczek.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class FileServiceTest {
    private final static ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final static PrintStream originalErr = System.err;
    private static String folderPath = "";

    @BeforeAll
    public static void setUpStreams() {
        System.setErr(new PrintStream(errContent));
    }

    @AfterAll
    public static void restoreStreams() {
        System.setErr(originalErr);
    }

    @AfterEach
    @SneakyThrows
    void tearDown() {
        FileUtils.deleteDirectory(new File(folderPath));
    }

    @Test
    void shouldSaveJsonFileWhenGivenOnePost() {
        // given
        FileService fileService = new FileService();
        mockGetJsonFromUrlAddressMethod(fileService,
                "[{\"userId\": 1, \"id\": 1, \"title\": \"Test Title\", \"body\": \"Test Body\"}]");

        // when
        folderPath = fileService.downloadPostsToJsonFiles();

        // then
        String expectedFileContents = "{\"userId\":1,\"id\":1,\"title\":\"Test Title\",\"body\":\"Test Body\"}";
        assertPostFileIsCorrect(folderPath + "/1.json", expectedFileContents);
    }

    @Test
    void shouldSaveThreeJsonFilesWhenGivenThreePosts() {
        // given
        FileService fileService = new FileService();
        mockGetJsonFromUrlAddressMethod(fileService,
                "[{\"userId\": 1, \"id\": 1, \"title\": \"Test Title1\", \"body\": \"Test Body\"}," +
                        "{\"userId\": 1, \"id\": 2, \"title\": \"Test Title2\", \"body\": \"Test Body\"}," +
                        "{\"userId\": 2, \"id\": 3, \"title\": \"Test Title3\", \"body\": \"Test Body\"}]");

        // when
        folderPath = fileService.downloadPostsToJsonFiles();

        // then
        String expectedFileOneContents = "{\"userId\":1,\"id\":1,\"title\":\"Test Title1\",\"body\":\"Test Body\"}";
        String expectedFileTwoContents = "{\"userId\":1,\"id\":2,\"title\":\"Test Title2\",\"body\":\"Test Body\"}";
        String expectedFileThreeContents = "{\"userId\":2,\"id\":3,\"title\":\"Test Title3\",\"body\":\"Test Body\"}";
        assertPostFileIsCorrect(folderPath + "/1.json", expectedFileOneContents);
        assertPostFileIsCorrect(folderPath + "/2.json", expectedFileTwoContents);
        assertPostFileIsCorrect(folderPath + "/3.json", expectedFileThreeContents);
    }

    @Test
    void shouldPrintStackTraceWhenResponseIsEmpty() {
        // given
        FileService fileService = new FileService();
        mockGetJsonFromUrlAddressMethod(fileService,
                "");

        // when
        fileService.downloadPostsToJsonFiles();

        // then
        assert (errContent.toString().contains(
                "com.fasterxml.jackson.databind.exc.MismatchedInputException: No content to map due to end-of-input"));
    }

    @Test
    void shouldThrowExceptionWhenResponseIsNull() {
        // given
        FileService fileService = new FileService();
        mockGetJsonFromUrlAddressMethod(fileService,
                null);

        // when
        Exception exception = assertThrows(IllegalArgumentException.class, fileService::downloadPostsToJsonFiles);

        // then
        assert(exception.getMessage().contains("argument \"content\" is null"));
    }

    @Test
    void shouldPrintStackTraceWhenResponseHasWrongStructure() {
        // given
        FileService fileService = new FileService();
        mockGetJsonFromUrlAddressMethod(fileService,
                "[{\"wrong\": 1, \"id\": 1, \"title\": \"Test Title\", \"body\": \"Test Body\"}]");

        // when
        folderPath = fileService.downloadPostsToJsonFiles();

        // then
        assert (errContent.toString().contains(
                "com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException: Unrecognized field \"wrong\""));
    }

    @Test
    void shouldPrintStackTraceWhenIdIsDuplicated() {
        // given
        FileService fileService = new FileService();
        mockGetJsonFromUrlAddressMethod(fileService,
                "[{\"userId\": 1, \"id\": 1, \"title\": \"Test Title1\", \"body\": \"Test Body\"}," +
                        "{\"userId\": 1, \"id\": 2, \"title\": \"Test Title2\", \"body\": \"Test Body\"}," +
                        "{\"userId\": 2, \"id\": 1, \"title\": \"Test Title3\", \"body\": \"Test Body\"}]");

        // when
        folderPath = fileService.downloadPostsToJsonFiles();

        // then
        assert (errContent.toString().contains(
                "DuplicateIdException: Returned jsonObject contains duplicated post ids"));
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