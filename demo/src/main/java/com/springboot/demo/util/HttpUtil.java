package com.springboot.demo.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtil {
    private static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);
    public static StringBuilder get(StringBuilder sbUrl) {
        StringBuilder response;

        try {
            URL url = new URL(sbUrl.toString());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            logger.warn("Requesting API data to {} with response code is {}",sbUrl, responseCode);
            if (responseCode == 404) {
                return null;
            }
            if (responseCode == 500) {
                return null;
            }
            // Read the response from the input stream
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            response = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            response = null;
        }
        return response;
    }
}
