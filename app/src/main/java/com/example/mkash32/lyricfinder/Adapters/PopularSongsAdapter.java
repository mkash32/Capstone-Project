package com.example.mkash32.lyricfinder.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mkash32.lyricfinder.R;
import com.example.mkash32.lyricfinder.Song;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Aakash on 11/12/16.
 */

public class PopularSongsAdapter extends RecyclerView.Adapter<RecentSavedSongsAdapter.ViewHolder>{
    private ArrayList<Song> songs;
    private Context c;

    public PopularSongsAdapter(ArrayList<Song> songs, Context c) {
        this.songs = songs;
        this.c = c;
    }

    @Override
    public RecentSavedSongsAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.songitem_recycler,viewGroup,false);
        return new RecentSavedSongsAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecentSavedSongsAdapter.ViewHolder viewHolder, int i) {
        final Song current = songs.get(i);
        viewHolder.getArtist().setText(current.getArtist());
        viewHolder.getTitle().setText(current.getTitle());
        viewHolder.getStar().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: save to favorites on click
            }
        });

        Picasso.with(c).load(current.getImageUrl()).into(viewHolder.getArtistImage());
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView title, artist;
        private ImageView star;
        private CircleImageView artistImage;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView)itemView.findViewById(R.id.tv_title);
            artist = (TextView) itemView.findViewById(R.id.tv_artist);
            star = (ImageView) itemView.findViewById(R.id.star);
            artistImage = (CircleImageView) itemView.findViewById(R.id.artist_image);
        }

        public TextView getTitle() {
            return title;
        }

        public TextView getArtist() {
            return artist;
        }

        public ImageView getStar() {
            return star;
        }

        public CircleImageView getArtistImage() {
            return artistImage;
        }
    }
}

