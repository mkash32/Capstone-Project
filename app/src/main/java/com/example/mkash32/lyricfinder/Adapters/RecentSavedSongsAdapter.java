package com.example.mkash32.lyricfinder.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mkash32.lyricfinder.Activities.RecentSavedFragment;
import com.example.mkash32.lyricfinder.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Aakash on 11/12/16.
 */

public class RecentSavedSongsAdapter extends RecyclerView.Adapter<RecentSavedSongsAdapter.ViewHolder>{
    private Cursor mCursor;
    private Context c;

    public RecentSavedSongsAdapter(Context c) {
        this.c = c;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.songitem_recycler,viewGroup,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        mCursor.moveToPosition(i);
        viewHolder.getArtist().setText(mCursor.getString(RecentSavedFragment.COL_ARTIST));
        viewHolder.getTitle().setText(mCursor.getString(RecentSavedFragment.COL_TITLE));
        viewHolder.getStar().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: save to favorites on click
            }
        });

        Picasso.with(c).load(mCursor.getString(RecentSavedFragment.COL_IMAGE_URL)).into(viewHolder.getArtistImage());
    }

    @Override
    public int getItemCount() {
        if (mCursor == null)
            return 0;

        return mCursor.getCount();
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

    public void setCursor(Cursor mCursor) {
        this.mCursor = mCursor;
        notifyDataSetChanged();
    }
}
