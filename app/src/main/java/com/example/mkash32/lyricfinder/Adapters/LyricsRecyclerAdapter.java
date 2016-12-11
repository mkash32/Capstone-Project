package com.example.mkash32.lyricfinder.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mkash32.lyricfinder.R;
import com.example.mkash32.lyricfinder.Song;

/**
 * Created by Aakash on 11/12/16.
 */

public class LyricsRecyclerAdapter extends RecyclerView.Adapter<LyricsRecyclerAdapter.RecyclerViewHolder> {
    private Song song;
    private Context c;

    public LyricsRecyclerAdapter(Song song, Context c) {
        this.song = song;
        this.c = c;
    }

    public void setSong(Song song) {
        this.song = song;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.song_lyrics,parent,false);
        return new RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        TextView lyrics = holder.getLyrics();
        lyrics.setText(song.getLyrics());
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder
    {
        private TextView lyrics;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            lyrics = (TextView) itemView.findViewById(R.id.tv_lyrics);
        }

        public TextView getLyrics() {
            return lyrics;
        }
    }

}
