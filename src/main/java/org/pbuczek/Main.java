package org.pbuczek;

import org.pbuczek.service.FileService;

public class Main {
    public static void main(String[] args) {
        System.out.println(FileService.downloadPostsToJsonFiles());
    }
}