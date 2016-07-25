package com.xedus.tutorials.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Home on 6/28/2016.
 */
public class MenuUtils {
    public static void share(Context mContext, String url){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        mContext.startActivity(intent);
    }
    public static void addBookMark(Context mContext,int id){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        Gson gson = new Gson();
        List<Integer> mList;
        if(!preferences.contains("fav"))
            mList = new ArrayList<>();
        else
            mList = Arrays.asList(gson.fromJson(preferences.getString("fav",""),Integer[].class));
        ArrayList<Integer> arrayList = new ArrayList<>(mList);
        arrayList.add(id);
        String json = gson.toJson(arrayList);
        preferences.edit().putString("fav",json).apply();
    }
    public static void removeBookMark(Context mContext,int id){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        Gson gson = new Gson();
        List<Integer> mList;
        if(!preferences.contains("fav"))
            mList = new ArrayList<>();
        else
            mList = Arrays.asList(gson.fromJson(preferences.getString("fav",""),Integer[].class));
        ArrayList<Integer> arrayList = new ArrayList<>(mList);
        arrayList.remove((Object)id);
        String json = gson.toJson(arrayList);
        preferences.edit().putString("fav",json).apply();
    }
    public static boolean isBookMarked(Context mContext,int id){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        Gson gson = new Gson();
        List<Integer> mList;
        if(!preferences.contains("fav"))
            mList = new ArrayList<>();
        else
            mList = Arrays.asList(gson.fromJson(preferences.getString("fav",""),Integer[].class));
        if(mList.indexOf(id)>=0)
            return true;
        return false;
    }
}
