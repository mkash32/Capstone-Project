package com.example.mkash32.lyricfinder.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.example.mkash32.lyricfinder.adapters.MusicPagerAdapter;
import com.example.mkash32.lyricfinder.R;
import com.example.mkash32.lyricfinder.Utilities;


public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private TabLayout tabs;
    private MusicPagerAdapter pagerAdapter;
    private Activity activity = this;
    private LyricsFragment lyricsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        boolean isTab = Utilities.isTablet(this);

        viewPager = (ViewPager) findViewById(R.id.pager);
        tabs = (TabLayout) findViewById(R.id.tabs);
        pagerAdapter = new MusicPagerAdapter(getSupportFragmentManager(), this, isTab);
        viewPager.setAdapter(pagerAdapter);
        tabs.setupWithViewPager(viewPager);

        if(!isTab) {
            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Start search activity
                    Intent search = new Intent(activity, SearchActivity.class);
                    activity.startActivity(search);
                }
            });
        } else {
            lyricsFragment = (LyricsFragment) getSupportFragmentManager().findFragmentById(R.id.lyrics_frag);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    // Only relevant in tablets
    public void setSong(String title, String artist, String image) {
        lyricsFragment.setSong(title, artist, image);
    }

}
