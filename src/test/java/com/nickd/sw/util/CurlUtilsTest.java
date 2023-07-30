package com.nickd.sw.util;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URI;

public class CurlUtilsTest {

    @Test
    public void testCurlDownload() throws IOException {
        CurlUtils.curl(URI.create("https://starwars.fandom.com/wiki/Kassa_(episode)"), new File("test.html"));
    }
}