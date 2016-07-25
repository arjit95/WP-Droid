package com.xedus.tutorials.api;

import com.google.gson.Gson;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.xedus.tutorials.Config;
import com.xedus.tutorials.api.models.Categories;
import com.xedus.tutorials.api.models.callback.OnDataReceived;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CategoryAPI{
    OnDataReceived mReceived;
    public int TIMEOUT = Config.TIMEOUT;
    public void setOnDataReceivedListener(OnDataReceived mListener){
        mReceived = mListener;
    }
    public void fetchCategories(){

        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(TIMEOUT, TimeUnit.SECONDS); // connect timeout
        client.setReadTimeout(TIMEOUT, TimeUnit.SECONDS);    // socket timeout
        // code request code here
        Request request = new Request.Builder()
                .url(Config.CATEGORY_BASE_URL)
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
    public List<Categories> getCategories(Response response) throws IOException {
        Gson gson = new Gson();
       if(response==null)
           return new ArrayList<Categories>();
        return Arrays.asList(gson.fromJson(response.body().charStream(), Categories[].class));

    }
}