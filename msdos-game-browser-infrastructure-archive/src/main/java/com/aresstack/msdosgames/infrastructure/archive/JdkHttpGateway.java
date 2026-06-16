package com.aresstack.msdosgames.infrastructure.archive;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public final class JdkHttpGateway implements HttpGateway {

    private static final int CONNECT_TIMEOUT_MILLIS = 15000;
    private static final int READ_TIMEOUT_MILLIS = 30000;

    @Override
    public String getText(String url) throws IOException {
        HttpURLConnection connection = openConnection(url);
        int statusCode = connection.getResponseCode();
        InputStream responseStream = openResponseStream(connection, statusCode);
        String responseBody = readText(responseStream);
        if (statusCode < 200 || statusCode >= 300) {
            throw new IOException("HTTP " + statusCode + " for " + url + ": " + responseBody);
        }
        return responseBody;
    }

    private HttpURLConnection openConnection(String url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setConnectTimeout(CONNECT_TIMEOUT_MILLIS);
        connection.setReadTimeout(READ_TIMEOUT_MILLIS);
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json,text/html;q=0.9,*/*;q=0.8");
        connection.setRequestProperty("User-Agent", "msdos-game-browser/1.0 (+https://github.com/aresstack/msdos-game-browser)");
        return connection;
    }

    private InputStream openResponseStream(HttpURLConnection connection, int statusCode) throws IOException {
        if (statusCode >= 200 && statusCode < 300) {
            return connection.getInputStream();
        }

        InputStream errorStream = connection.getErrorStream();
        if (errorStream == null) {
            return connection.getInputStream();
        }
        return errorStream;
    }

    private String readText(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        StringBuilder text = new StringBuilder();
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                text.append(line).append('\n');
            }
        } finally {
            reader.close();
        }
        return text.toString();
    }
}
