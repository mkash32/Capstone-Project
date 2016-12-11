package com.example.mkash32.lyricfinder.Adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.mkash32.lyricfinder.Activities.RecentSavedFragment;
import com.example.mkash32.lyricfinder.Activities.PopularSongsFragment;

/**
 * Created by Aakash on 11/12/16.
 */

public class MusicPagerAdapter extends FragmentPagerAdapter {
    private Context c;
    private String[] tabTitles = {"Popular","Recents","Saved"};

    public MusicPagerAdapter(FragmentManager fm, Context c) {
        super(fm);
        this.c = c;
    }

    @Override
    public Fragment getItem(int position) {
        if(position == 0) {
            return new PopularSongsFragment();
        } else {
            RecentSavedFragment f = new RecentSavedFragment();
            if(position == 1) {
                f.setRecent(true);
            } else {
                f.setRecent(false);
            }
            return f;
        }
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
