package ch.m4th1eu.awaked.launcher;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class Api {

    public static String getReturnWebsiteByLink(URL url) throws IOException {
        String sourceLine = null;
        InputStreamReader pageInput = new InputStreamReader(url.openStream());
        BufferedReader source = new BufferedReader(pageInput);
        try {
            sourceLine = source.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(sourceLine);
        return sourceLine;
    }
}
