package com.example.mkash32.lyricfinder.Adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.mkash32.lyricfinder.Activities.FeedFragment;

/**
 * Created by user on 11/12/16.
 */

public class MusicPagerAdapter extends FragmentPagerAdapter {
    private Context c;
    private String[] tabTitles = {"Recents","Popular","Saved"};

    public MusicPagerAdapter(FragmentManager fm, Context c) {
        super(fm);
        this.c = c;
    }

    @Override
    public Fragment getItem(int position) {
        FeedFragment f = new FeedFragment();
        f.setFeedType(position);
        return f;
    }
    @Override
    public int getCount() {
        return tabTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}