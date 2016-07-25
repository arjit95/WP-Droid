package com.xedus.tutorials.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.xedus.tutorials.Config;
import com.xedus.tutorials.R;
import com.xedus.tutorials.api.PostsAPI;
import com.xedus.tutorials.api.models.Posts;
import com.xedus.tutorials.api.models.callback.OnDataReceived;
import com.xedus.tutorials.utils.DateFormatter;
import com.xedus.tutorials.utils.MenuUtils;

import java.io.IOException;

public class PostActivity extends AppCompatActivity {
    private int post_id=0;
    private String featuredMediaUrl="";
   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_post);
        Intent mIntent = getIntent();
        if(mIntent==null)
            return;
        post_id = mIntent.getIntExtra("post_id",0);
        String heading = mIntent.getStringExtra("post_title");
        String category = mIntent.getStringExtra("post_category");
        String time = mIntent.getStringExtra("published_time");
        featuredMediaUrl = mIntent.getStringExtra("featured_media");
        TextView post_heading = (TextView) findViewById(R.id.post_heading);
        TextView post_category = (TextView) findViewById(R.id.post_category);
        TextView post_time = (TextView) findViewById(R.id.post_time);
        post_time.setText(DateFormatter.format(time));
        post_heading.setText(heading);
        post_category.setText(getString(R.string.posted_category,category));
       final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_bookmark);
        if(MenuUtils.isBookMarked(this,post_id))
            fab.hide();
       if (fab != null) {
           fab.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   fab.hide();
                   MenuUtils.addBookMark(getBaseContext(),post_id);
                   Snackbar.make(findViewById(R.id.parent_container),R.string.bookmark_successful,Snackbar.LENGTH_SHORT).show();
                   Intent i = new Intent();
                   i.setAction(Config.BOOKMARK_UPDATED);
                   getApplicationContext().sendBroadcast(i);
               }
           });
       }
       load();
    }
    private void load(){
        final PostsAPI api = new PostsAPI();
        findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
        api.setOnDataReceivedListener(new OnDataReceived() {
            @Override
            public void onDataSuccess(Response response) {
                try {
                    final Posts mPost = api.getPost(response);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            findViewById(R.id.progressBar).setVisibility(View.GONE);
                            findViewById(R.id.loading_indicators).setVisibility(View.GONE);
                            ImageView imageView = (ImageView) findViewById(R.id.post_image);
                            ImageLoader.getInstance().displayImage(featuredMediaUrl,imageView,new DisplayImageOptions.Builder()
                                    .cacheOnDisk(true)
                                    .imageScaleType(ImageScaleType.EXACTLY)
                                    .displayer(new FadeInBitmapDisplayer(300))
                                    .build(),new SimpleImageLoadingListener(){
                                @Override
                                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                    super.onLoadingComplete(imageUri, view, loadedImage);
                                    if(loadedImage!=null && loadedImage.getWidth()>0 && loadedImage.getHeight()>0)
                                        Palette.from(loadedImage).generate(new Palette.PaletteAsyncListener() {
                                            @Override
                                            public void onGenerated(Palette palette) {
                                                int color = palette.getVibrantColor(ContextCompat.getColor(getBaseContext(),R.color.colorPrimary));
                                                findViewById(R.id.post_meta).setBackgroundColor(color);
                                                if(Build.VERSION.SDK_INT>=21)
                                                    getWindow().setStatusBarColor(color);
                                            }
                                        });
                                }
                            });
                            WebView wv = (WebView) findViewById(R.id.post_content);
                            final String mimeType = "text/html";
                            final String encoding = "UTF-8";
                            String style="<style>\n" +
                                    "pre{\n" +
                                    "border:1px solid #ccc;\n" +
                                    "background:#333;overflow:auto;\n" +
                                    "}\n" +
                                    "code{\n" +
                                    "display:inline-block;\n" +
                                    "color:#4bc7b1;\n" +
                                    "font-weight:bold;\n" +
                                    "font-size:15px;\n" +
                                    "}\n" +
                                    ".code-embed-name{\n" +
                                    "font-weight:bold;\n" +
                                    "margin-top:-5px;\n" +
                                    "}\n" +
                                    "a{\n" +
                                    "color:#2196F3;\n" +
                                    "text-decoration:none;\n" +
                                    "}\n" +
                                    "@font-face {\n" +
                                    "    font-family: 'Ubuntu';\n" +
                                    "    src: url('fonts/Ubuntu.ttf');\n" +
                                    "}\n" +
                                    "\n" +
                                    "body {font-family: 'Ubuntu';}\n" +
                                    "</style>";
                            wv.loadDataWithBaseURL("file:///android_asset/", style+mPost.content.rendered, mimeType, encoding, "");
                        }
                    });
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
                        findViewById(R.id.no_connection).setVisibility(View.VISIBLE);
                        Snackbar.make(findViewById(R.id.parent_container),R.string.no_connection, Snackbar.LENGTH_INDEFINITE)
                                .setAction(R.string.retry, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
                                        findViewById(R.id.no_connection).setVisibility(View.GONE);
                                        load();
                                    }
                                }).show();
                    }
                });

            }

            @Override
            public void onNoData() {
               onDataFailed(null,null);
            }
        });
        api.fetchById(this,post_id);
    }
    @Override
    public void onBackPressed(){
        supportFinishAfterTransition();
    }
}
