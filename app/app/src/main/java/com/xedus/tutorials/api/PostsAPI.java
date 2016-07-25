package com.xedus.tutorials.api;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.xedus.tutorials.Config;
import com.xedus.tutorials.api.models.Categories;
import com.xedus.tutorials.api.models.Posts;
import com.xedus.tutorials.api.models.callback.OnDataReceived;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PostsAPI {
    OnDataReceived mReceived;
    public int NUM_POSTS=Config.PER_PAGE;
    public int CACHE_SIZE = 20*1024*1024;
    public int TIMEOUT = Config.TIMEOUT;
    public void setOnDataReceivedListener(OnDataReceived mListener){
        mReceived = mListener;
    }
    public void fetchAll(Context context){
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(TIMEOUT, TimeUnit.SECONDS); // connect timeout
        client.setReadTimeout(TIMEOUT, TimeUnit.SECONDS);    // socket timeout

        // code request code here
        Cache responseCache = new Cache(context.getCacheDir(),CACHE_SIZE);
        client.setCache(responseCache);
        Request request = new Request.Builder()
                .url(Config.POST_BASE_URL)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                          if(mReceived!=null)
                              mReceived.onDataFailed(request,e);
            }
            @Override
            public void onResponse(Response response)  {
                  if(response!=null && response.isSuccessful())
                      mReceived.onDataSuccess(response);
                  else
                      mReceived.onNoData();
            }
        });

    }
    public void fetchAll(Context context,int page){
        OkHttpClient client = new OkHttpClient();
        Cache responseCache = new Cache(context.getCacheDir(), CACHE_SIZE);
        client.setCache(responseCache);
        client.setConnectTimeout(TIMEOUT, TimeUnit.SECONDS); // connect timeout
        client.setReadTimeout(TIMEOUT, TimeUnit.SECONDS);    // socket timeout
        // code request code here
        Request request = new Request.Builder()
                .url(Config.POST_BASE_URL+"?per_page="+NUM_POSTS+"&page="+page)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                if(mReceived!=null)
                    mReceived.onDataFailed(request,e);
            }
            @Override
            public void onResponse(Response response)  {
                if(response!=null && response.isSuccessful())
                    mReceived.onDataSuccess(response);
                else
                    mReceived.onNoData();
            }
        });

    }

    public void fetchByTag(Context context,String tag,int page){
        OkHttpClient client = new OkHttpClient();
        // code request code here
        Cache responseCache = new Cache(context.getCacheDir(), CACHE_SIZE);
        client.setCache(responseCache);
        client.setConnectTimeout(TIMEOUT, TimeUnit.SECONDS); // connect timeout
        client.setReadTimeout(TIMEOUT, TimeUnit.SECONDS);    // socket timeout
        Request request = new Request.Builder()
                .url(Config.POST_BASE_URL+"?search="+tag+"&per_page="+NUM_POSTS+"&page="+page)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                if(mReceived!=null)
                    mReceived.onDataFailed(request,e);
            }
            @Override
            public void onResponse(Response response)  {
                if(response!=null && response.isSuccessful())
                    mReceived.onDataSuccess(response);
                else
                    mReceived.onNoData();
            }
        });
    }
    public void fetchByCategory(Context context,int category,int page){
        OkHttpClient client = new OkHttpClient();
        // code request code here
        Cache responseCache = new Cache(context.getCacheDir(), CACHE_SIZE);
        client.setCache(responseCache);
        client.setConnectTimeout(TIMEOUT, TimeUnit.SECONDS); // connect timeout
        client.setReadTimeout(TIMEOUT, TimeUnit.SECONDS);    // socket timeout
        Request request = new Request.Builder()
                .url(Config.POST_BASE_URL+"?categories="+category+"&per_page="+NUM_POSTS+"&page="+page)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                if(mReceived!=null)
                    mReceived.onDataFailed(request,e);

            }
            @Override
            public void onResponse(Response response)  {
                if(response!=null && response.isSuccessful())
                    mReceived.onDataSuccess(response);
                else
                    mReceived.onNoData();
            }
        });
    }
    public void fetchById(Context context,int id){
        OkHttpClient client = new OkHttpClient();
        // code request code here
        Cache responseCache = new Cache(context.getCacheDir(), CACHE_SIZE);
        client.setCache(responseCache);
        client.setConnectTimeout(TIMEOUT, TimeUnit.SECONDS); // connect timeout
        client.setReadTimeout(TIMEOUT, TimeUnit.SECONDS);    // socket timeout
        Request request = new Request.Builder()
                .url(Config.POST_BASE_URL+"?id="+id)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                if(mReceived!=null)
                    mReceived.onDataFailed(request,e);
            }
            @Override
            public void onResponse(Response response)  {
                if(response!=null && response.isSuccessful())
                    mReceived.onDataSuccess(response);
                else
                    mReceived.onNoData();
            }
        });
    }
    public void fetchFavourite(Context context,List<Integer> id,int page){
        OkHttpClient client = new OkHttpClient();
        // code request code here
        Cache responseCache = new Cache(context.getCacheDir(), CACHE_SIZE);
        client.setCache(responseCache);
        client.setConnectTimeout(TIMEOUT, TimeUnit.SECONDS); // connect timeout
        client.setReadTimeout(TIMEOUT, TimeUnit.SECONDS);    // socket timeout
        String fav="";
        for (int anId : id) fav += Integer.toString(anId) + ",";
        fav=fav.substring(0,fav.length()-1);
        Request request = new Request.Builder()
                .url(Config.POST_BASE_URL+"?include="+fav+"&per_page="+Config.PER_PAGE+"&page="+page)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                if(mReceived!=null)
                    mReceived.onDataFailed(request,e);
            }
            @Override
            public void onResponse(Response response)  {
                if(response!=null && response.isSuccessful())
                    mReceived.onDataSuccess(response);
                else
                    mReceived.onNoData();
            }
        });
    }
    public List<Posts> getPosts(Response response) throws IOException {
        Gson gson = new Gson();
       if(response==null)
           return new ArrayList<>();
        return Arrays.asList(gson.fromJson(response.body().charStream(), Posts[].class));

    }
    public Posts getPost(Response response) throws IOException {
        Gson gson = new Gson();
        if(response==null)
            return new Posts();
        return gson.fromJson(response.body().charStream(), Posts.class);

    }
}