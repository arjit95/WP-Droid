package com.xedus.tutorials.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.xedus.tutorials.Config;
import com.xedus.tutorials.R;
import com.xedus.tutorials.activity.PostActivity;
import com.xedus.tutorials.adapter.MainAdapter;
import com.xedus.tutorials.api.PostsAPI;
import com.xedus.tutorials.api.models.Categories;
import com.xedus.tutorials.api.models.Posts;
import com.xedus.tutorials.api.models.callback.OnButtonClickListener;
import com.xedus.tutorials.api.models.callback.OnClickListener;
import com.xedus.tutorials.api.models.callback.OnDataReceived;
import com.xedus.tutorials.utils.DividerItemDecoration;
import com.xedus.tutorials.utils.EndlessRecyclerOnScrollListener;
import com.xedus.tutorials.utils.MenuUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CategoryFragment extends Fragment {
    private int category = 0;
    List<Posts> mPosts;
    RecyclerView mRecyclerView;
    ProgressBar progressBar;
    View mConnectionView,parent;
    int page=1;
    boolean isFinished=false,isCard=false;
    MainAdapter mAdapter;
    public static CategoryFragment newInstance(int category){
            CategoryFragment mFragment = new CategoryFragment();
            mFragment.category = category;
            return mFragment;
    }
    public void isCard(boolean state){
        isCard =state;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.fragment_posts,container,false);
        mRecyclerView= (RecyclerView) view.findViewById(R.id.post_list);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        mConnectionView = view.findViewById(R.id.no_connection);
        parent = view.findViewById(R.id.parent_container);
        return view;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),R.drawable.divider));
        load();
    }
    private void load(){
        progressBar.setVisibility(View.VISIBLE);
        final PostsAPI mPostFetcher = new PostsAPI();
        mPostFetcher.setOnDataReceivedListener(new OnDataReceived() {
            @Override
            public void onDataSuccess(final Response response) {
                try {
                    mPosts = mPostFetcher.getPosts(response);
                } catch (IOException e) {
                    e.printStackTrace();
                    mPosts=new ArrayList<>();
                }
                if(getActivity()==null)
                    return;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        mAdapter =new MainAdapter(mPosts);
                        if(mPosts==null || mPosts.size()< Config.PER_PAGE) {
                            isFinished = true;
                        }

                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                        String json= prefs.getString("categories","");
                        Gson gson = new Gson();
                        final List<Categories> categories = Arrays.asList(gson.fromJson(json, Categories[].class));
                        mAdapter.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClickListener(View view,int position) {
                                Posts post= mAdapter.get(position);
                                Intent i = new Intent(getActivity(),PostActivity.class);
                                i.putExtra("post_id",post.id);
                                i.putExtra("post_title",post.title.rendered);
                                String category="Uncategorized";
                                for(Categories cat :categories){
                                    if(cat.id==post.categories[0])
                                        category=cat.name;
                                }
                                i.putExtra("post_category",category);
                                i.putExtra("published_time",post.date);
                                i.putExtra("featured_media",post.featured_media_url);
                                startActivity(i);
                            }

                            @Override
                            public void onLongClickListener(View view, int position) {

                            }
                        });
                        mAdapter.setCateogries(categories);
                        if(isCard) {
                            mAdapter.useCard(true);
                            mAdapter.setOnButtonClickListener(new OnButtonClickListener() {
                                @Override
                                public void onButtonClick(View view, int position) {
                                    int id = view.getId();
                                    if(id == R.id.btn_share)
                                        MenuUtils.share(getActivity(),mAdapter.get(position).link);
                                }
                            });
                        }
                        mRecyclerView.setAdapter(mAdapter);
                        page=page+1;
                        mRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
                            @Override
                            public void onScrolledToBottom() {
                                super.onScrolledToBottom();
                                addMore();
                            }
                        });
                    }
                });
            }

            @Override
            public void onDataFailed(Request request, IOException e) {
                if(getActivity()==null)
                    return;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mConnectionView.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        Snackbar.make(parent,R.string.no_connection, Snackbar.LENGTH_INDEFINITE)
                                .setAction(R.string.retry, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        mConnectionView.setVisibility(View.GONE);
                                        progressBar.setVisibility(View.VISIBLE);
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
       if(category==0)
        mPostFetcher.fetchAll(getActivity(),page);
        else
           mPostFetcher.fetchByCategory(getActivity(),category,page);
    }
    private void addMore(){
        if(isFinished) {
            mAdapter.isFinished(true);
            return;
        }
        final PostsAPI mPostFetcher = new PostsAPI();
        mPostFetcher.setOnDataReceivedListener(new OnDataReceived() {
            @Override
            public void onDataSuccess(final Response response) {
                try {
                    final List<Posts> mPosts = mPostFetcher.getPosts(response);
                    if(getActivity()==null)
                        return;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.merge(mPosts);
                            page=page+1;
                        }
                    });
                    if(mPosts==null || mPosts.size()<Config.PER_PAGE) {
                        isFinished = true;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onDataFailed(Request request, IOException e) {
                if(getActivity()==null)
                    return;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Snackbar.make(parent,R.string.no_connection, Snackbar.LENGTH_INDEFINITE)
                                .setAction(R.string.retry, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        addMore();
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
        if(category==0)
            mPostFetcher.fetchAll(getActivity(),page);
        else
            mPostFetcher.fetchByCategory(getActivity(),category,page);
    }
}
