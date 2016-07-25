package com.xedus.tutorials.activity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;

import android.support.design.widget.TabLayout;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.xedus.tutorials.R;
import com.xedus.tutorials.adapter.DrawerAdapter;
import com.xedus.tutorials.api.models.Categories;
import com.xedus.tutorials.api.models.callback.OnClickListener;
import com.xedus.tutorials.fragment.CategoryFragment;
import com.xedus.tutorials.fragment.Favourites;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity
 {

    List<Categories> categories;
    RecyclerView mNav;
     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        mNav = (RecyclerView) findViewById(R.id.nav_view);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
        .build();
        ImageLoader.getInstance().init(config);
        fetchCategories();
         ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
         viewPager.setAdapter(new TabAdapter());
         TabLayout tabLayout = (TabLayout) findViewById(R.id.tablayout);
         tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    public void fetchCategories(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String json= prefs.getString("categories","");
        Gson gson = new Gson();
        categories = Arrays.asList(gson.fromJson(json, Categories[].class));
        List<Categories> tmp = new ArrayList<>(categories);
        Categories cat =new Categories();
        cat.count=0;
        cat.id=0;
        cat.name ="Home";
        tmp.add(0,cat);
        categories=tmp;
        mNav.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        DrawerAdapter mAdapter= new DrawerAdapter(categories);
        mAdapter.setOnClickListener(new OnClickListener() {
            @Override
            public void onClickListener(View view,final int position) {
                DrawerLayout drawer= (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                if(position>0){
                    (new Handler()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                           if(position==1)
                               return;
                            Categories category= categories.get(position-1);
                            Intent i =new Intent(getBaseContext(),CategoryActivity.class);
                            i.putExtra("category_id",category.id);
                            startActivity(i);
                        }
                    },300);
                }
            }

            @Override
            public void onLongClickListener(View view, int position) {

            }
        });
        mNav.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        final MenuItem myActionMenuItem = menu.findItem( R.id.action_search);
        final SearchView searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if( ! searchView.isIconified()) {
                    searchView.setIconified(true);
                }
                myActionMenuItem.collapseActionView();
                Intent i = new Intent(getBaseContext(),SearchActivity.class);
                i.putExtra("query",query);
                startActivity(i);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                // UserFeedback.show( "SearchOnQueryTextChanged: " + s);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        return super.onOptionsItemSelected(item);
    }
     public class TabAdapter extends FragmentPagerAdapter {
         final int PAGE_COUNT = 2;
         final int pages[] = {R.string.tab_latest,R.string.tab_bookmarked};
         public TabAdapter() {
             super(getSupportFragmentManager());
         }

         @Override
         public int getCount() {
             return PAGE_COUNT;
         }
         @Override
         public CharSequence getPageTitle(int position) {
             return getString(pages[position]);
         }
         @Override
         public Fragment getItem(int position) {
             if(position==0)
                 return CategoryFragment.newInstance(0);
             return Favourites.newInstance(MainActivity.this);
         }
     }


}
