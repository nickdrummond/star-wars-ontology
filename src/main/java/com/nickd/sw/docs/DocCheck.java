package com.nickd.sw.docs;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.regex.Pattern;

public class DocCheck {

    public static void main(String[] args) throws IOException {
        Pattern links = Pattern.compile("\\[(.+?)\\]\\((http(s?)://star-wars-ontology.up.railway.app/(.+?))\\)");

        File sources = new File("docs/");

        for (File source : sources.listFiles((dir, name)-> name.endsWith("md"))) {
            System.out.println("Checking " + source.getAbsolutePath());
            Scanner scanner = new Scanner(source);
            scanner.findAll(links).forEach(r -> {
                String link = r.group(1);
                String url = r.group(2);
                try {
                    System.out.println(getStatus(url) + " [" + link + "] " + url);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private static int getStatus(String urlStr) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();

        return connection.getResponseCode();
    }
}
