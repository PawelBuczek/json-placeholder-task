package org.pbuczek;

import org.pbuczek.service.FileService;

public class Main {
    public static void main(String[] args) {
        FileService fileService = new FileService();
        System.out.println(fileService.downloadPostsToJsonFiles());
    }
}