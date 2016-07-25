package com.xedus.tutorials.api;

import com.google.gson.Gson;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.xedus.tutorials.Config;
import com.xedus.tutorials.api.models.Media;
import com.xedus.tutorials.api.models.Posts;
import com.xedus.tutorials.api.models.callback.OnDataReceived;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by PRAV on 22-06-2016.
 */
public class MediaAPI {
    OnDataReceived mReceived;
    public int TIMEOUT = Config.TIMEOUT;
    public void setOnDataReceivedListener(OnDataReceived mListener){
        mReceived = mListener;
    }
    public void fetchMedia(int id){

        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(TIMEOUT, TimeUnit.SECONDS); // connect timeout
        client.setReadTimeout(TIMEOUT, TimeUnit.SECONDS);    // socket timeout
        // code request code here
        Request request = new Request.Builder()
                .url(Config.Media_BASE_URL+"?id="+id)
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
    public Response fetchMediaSync(int id){

        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(TIMEOUT, TimeUnit.SECONDS); // connect timeout
        client.setReadTimeout(TIMEOUT, TimeUnit.SECONDS);    // socket timeout
        // code request code here
        Request request = new Request.Builder()
                .url(Config.Media_BASE_URL+"?id="+id)
                .build();

        Call call = client.newCall(request);
        try {
            return call.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public List<Media> getMedia(Response response) throws IOException {
        Gson gson = new Gson();
        if(response==null)
            return new ArrayList<Media>();
        return Arrays.asList(gson.fromJson(response.body().charStream(), Media[].class));

    }
}
