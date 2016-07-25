package com.xedus.tutorials;
public class Config{
    public static final String BASE_URL = "http://www.tutorialwebz.com";
    public static final String POST_BASE_URL = Config.BASE_URL+"/posts-api.php";
    public static final String CATEGORY_BASE_URL = Config.BASE_URL+"/wp-json/wp/v2/categories";
    public static final String Media_BASE_URL = Config.BASE_URL+"/wp-json/wp/v2/media";
    public static final String BOOKMARK_UPDATED = "com.xedus.tutorials.bookmark.update";
    public static final int PER_PAGE = 8;
    public static final int TIMEOUT = 15;
}