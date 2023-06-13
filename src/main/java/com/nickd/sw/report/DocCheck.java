package com.nickd.sw.report;

import com.google.common.io.LineReader;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class DocCheck {

    public static void main(String[] args) throws IOException {
        Pattern links = Pattern.compile("\\((https://star-wars-ontology.up.railway.app/(.+?))\\)");

        File sources = new File("docs/");

        for (File source : sources.listFiles((dir, name)-> name.endsWith("md"))) {
            System.out.println("Checking " + source.getAbsolutePath());
            Scanner scanner = new Scanner(source);
            scanner.findAll(links).forEach(r -> {
                String url = r.group(1);
                System.out.print(url);
                try {
                    System.out.println(": " + getStatus(url));
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
