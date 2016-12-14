package com.example.mkash32.lyricfinder.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mkash32.lyricfinder.R;

/**
 * Created by Aakash on 11/12/16.
 */

public class LyricsRecyclerAdapter extends RecyclerView.Adapter<LyricsRecyclerAdapter.RecyclerViewHolder> {
    private String lyrics = "Loading...";
    private Context c;

    public LyricsRecyclerAdapter(String lyrics, Context c) {
        this.lyrics = lyrics;
        this.c = c;
    }

    public LyricsRecyclerAdapter(Context c) {
        this.c = c;
    }

    public void setLyrics(String lyrics) {
        this.lyrics = lyrics;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.song_lyrics,parent,false);
        return new RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        TextView lyricsTv = holder.getLyricsTv();
        lyricsTv.setText(lyrics);
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder
    {
        private TextView lyricsTv;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            lyricsTv = (TextView) itemView.findViewById(R.id.tv_lyrics);
        }

        public TextView getLyricsTv() {
            return lyricsTv;
        }
    }

}
