package com.xedus.tutorials.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.xedus.tutorials.R;
import com.xedus.tutorials.api.CategoryAPI;
import com.xedus.tutorials.api.models.Categories;
import com.xedus.tutorials.api.models.callback.OnDataReceived;

import java.io.IOException;
import java.util.List;

import pl.tajchert.nammu.Nammu;
import pl.tajchert.nammu.PermissionCallback;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Nammu.init(getApplicationContext());
        Button button  = (Button) findViewById(R.id.start);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadCategories();
            }
        });
        checkPermissions();
    }
    final PermissionCallback permissionCallback = new PermissionCallback() {
        @Override
        public void permissionGranted() {
            Log.i("permission","Granter");
            findViewById(R.id.start).setVisibility(View.VISIBLE);
        }

        @Override
        public void permissionRefused() {
            new AlertDialog.Builder(getBaseContext())
                    .setTitle(R.string.confirm)
                    .setMessage(R.string.confirm_message)
                    .setCancelable(false)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            checkPermissions();
                        }
                    })
                    .show();
        }
    };


    private void checkPermissions(){
        if(Build.VERSION.SDK_INT>=23) {
            if(Nammu.checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                if(PreferenceManager.getDefaultSharedPreferences(this).contains("categories"))
                    finishSplash();
                else
                    findViewById(R.id.start).setVisibility(View.VISIBLE);
            }
            if (Nammu.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                //User already refused to give us this permission or removed it
                //Now he/she can mark "never ask again" (sic!)
                Snackbar.make(findViewById(R.id.parent_container), "Please allow storage to cache offline data on storage.",
                        Snackbar.LENGTH_INDEFINITE)
                        .setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Nammu.askForPermission(SplashActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE, permissionCallback);
                            }
                        }).show();

            } else {
                Nammu.askForPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, permissionCallback);
            }
        }
        else {
            if(PreferenceManager.getDefaultSharedPreferences(this).contains("categories"))
                finishSplash();
            else
                findViewById(R.id.start).setVisibility(View.VISIBLE);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Nammu.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    private void finishSplash(){
       runOnUiThread(new Runnable() {
           @Override
           public void run() {
               findViewById(R.id.start).setVisibility(View.GONE);
               findViewById(R.id.progressBar).setVisibility(View.GONE);
               Intent i = new Intent(getBaseContext(),MainActivity.class);
               startActivity(i);
               finish();
           }
       });

    }
    private void loadCategories(){
        final SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        if(mPrefs.contains("categories")) {
            finishSplash();
            return;
        }
        findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
        findViewById(R.id.start).setVisibility(View.GONE);
        final CategoryAPI mApi = new CategoryAPI();
        mApi.setOnDataReceivedListener(new OnDataReceived() {
            @Override
            public void onDataSuccess(Response response) {
                try {
                    List<Categories> mCategories= mApi.getCategories(response);
                    SharedPreferences.Editor prefsEditor = mPrefs.edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(mCategories);
                    prefsEditor.putString("categories", json);
                    prefsEditor.commit();
                    finishSplash();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onDataFailed(Request request, IOException e) {
                  runOnUiThread(new Runnable() {
                      @Override
                      public void run() {
                          findViewById(R.id.progressBar).setVisibility(View.GONE);
                          findViewById(R.id.start).setVisibility(View.VISIBLE);
                          Snackbar.make(findViewById(R.id.parent_container), "Cannot fetch data from server.",
                                  Snackbar.LENGTH_SHORT).show();
                      }
                  });
            }

            @Override
            public void onNoData() {
                 onDataFailed(null,null);
            }
        });
        mApi.fetchCategories();
    }
}
