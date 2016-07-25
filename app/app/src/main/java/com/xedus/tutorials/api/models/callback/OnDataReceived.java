package com.xedus.tutorials.api.models.callback;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

public interface OnDataReceived{
    void onDataSuccess(Response response);
    void onDataFailed(Request request, IOException e);
    void onNoData();
}