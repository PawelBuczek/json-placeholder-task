package org.pbuczek.service;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class DataService {
    @SuppressWarnings("SameParameterValue")
    public String getJsonFromUrlAddress(String urlAddress) throws IOException {
        try (InputStream inputStream = new URL(urlAddress).openStream()) {
            return IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        }
    }
}
