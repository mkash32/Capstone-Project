package com.example.mkash32.lyricfinder.Adapters;

import android.content.Context;
import android.location.Location;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.mkash32.lyricfinder.Activities.FeedFragment;
import com.example.mkash32.lyricfinder.Activities.SearchFragment;

/**
 * Created by Aakash on 11/12/16.
 */

public class MusicPagerAdapter extends FragmentPagerAdapter {
    private Context c;
    private String[] tabTitles = {"Popular","Recents","Saved"};
    private String[] tabTitlesT = {"Popular", "Recents", "Saved", "Search"};
    private boolean isTab;

    public MusicPagerAdapter(FragmentManager fm, Context c, boolean isTab) {
        super(fm);
        this.c = c;
        this.isTab = isTab;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 3) {
            return new SearchFragment();
        } else {
            FeedFragment f = new FeedFragment();
            f.setType(position);
            return f;
        }
    }
    @Override
    public int getCount() {
        return isTab ? tabTitlesT.length : tabTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return isTab ? tabTitlesT[position] : tabTitles[position];
    }
}
