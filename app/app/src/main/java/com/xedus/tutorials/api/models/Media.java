package com.xedus.tutorials.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by PRAV on 22-06-2016.
 */
public class Media {
    public int id;
    @SerializedName("source_url")
    public String link;
}
