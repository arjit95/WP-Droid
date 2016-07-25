package com.xedus.tutorials.api.models;

/**
 * Created by PRAV on 22-06-2016.
 */
public class Posts {
    public Title title;
    public String author;
    public int id;
    public String date;
    public int featured_media;
    public String featured_media_url;
    public int categories[];
    public Title content;
    public String link;
    public class Title{
        public String rendered;
    }
}
