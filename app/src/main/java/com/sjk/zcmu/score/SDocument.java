package com.sjk.zcmu.score;


import org.jsoup.nodes.Document;

import java.util.Map;

public class SDocument {
    private static Document doc;
    private static Map<String, String> cookies;

    public static Document getDoc() {
        return doc;
    }

    public static void setDoc(Document doc) {
        SDocument.doc = doc;
    }

    public static Map<String, String> getCookies() {
        return cookies;
    }

    public static void setCookies(Map<String, String> cookies) {
        SDocument.cookies = cookies;
    }
}
