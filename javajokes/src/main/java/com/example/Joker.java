package com.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Joker {
    private String mJoke;
    public String getJoke() {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        try {
            URL url = new URL("http://api.icndb.com/jokes/random");
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null)
                return null;
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null)
                buffer.append(line + "\n");
            if (buffer.length() == 0)
                return null;
            return buffer.toString();
        } catch (Exception e) {
            System.out.println(e.toString());
            return null;
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    System.out.println(e.toString());
                }
            }
        }
    }
}
