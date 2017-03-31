package com.flyman3046.allnews.Model;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class DataConstants {
    public static final String ARTICLE_LINK_MESSAGE = "article_link";
    public static final String ARTICLE_SOURCE_NAME = "article_source_name";
    public static final String ARTICLE_URL_IMAGE = "getUrlToImage";

    public static final Map<String, String> NEWS_URL_TO_SELECTOR_MAP = createUrl2SelectorMap();
    private static Map<String, String> createUrl2SelectorMap()
    {
        Map<String,String> myMap = new HashMap<>();
        myMap.put("techcrunch", "body div[class*=\"article\"] p");
        myMap.put("engadget", "body div[class*=\"o-article_block\"] p");
        myMap.put("techradar", "body div[class*=\"text-copy bodyCopy auto\"] p");
        myMap.put("ars-technica", "body div[class*=\"article-content post-page\"] p");
        myMap.put("recode", "body div[class*=\"c-entry-content\"] p");
        myMap.put("the-next-web", "body div[class*=\"post-body fb-quotable u-m-3\"] p");
        return myMap;
    }

    public static final Map<String, String> NEWS_SOURCE_TO_SHORT_URL_MAP = createSource2UrlMap();
    private static Map<String, String> createSource2UrlMap()
    {
        Map<String,String> myMap = new HashMap<>();
        myMap.put("techcrunch", "techcrunch");
        myMap.put("engadget", "engadget");
        myMap.put("techradar", "techradar");
        myMap.put("ars-technica", "arstechnica");
        myMap.put("recode", "recode");
        myMap.put("the-next-web", "thenextweb");
        return myMap;
    }
}
