package com.edu.JavaLearning.Netty.longpolling;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * @author tangzh
 * @version 1.0
 * @date 2020/5/3 11:20 PM
 **/
public class HttpLongPollingClient {

    public static void main(String[] args) throws IOException {
        URL url = new URL("http://" + "10.211.55.4" + ":80");

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Id", "66036");

        connection.connect();

        if (connection.getResponseCode() == 200) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            String line;
            StringBuilder builder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                builder.append(line + "\n");
            }
            System.out.println("Date:" + new Date().toString() + "Result: " + builder.toString());
            reader.close();
        }
    }
}
