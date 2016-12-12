package com.example.mkash32.lyricfinder.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mkash32.lyricfinder.Activities.FeedFragment;
import com.example.mkash32.lyricfinder.Activities.LyricsActivity;
import com.example.mkash32.lyricfinder.R;
import com.example.mkash32.lyricfinder.Services.ApiIntentService;
import com.example.mkash32.lyricfinder.Services.DataIntentService;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Aakash on 11/12/16.
 */

public class SongsAdapter extends RecyclerView.Adapter<SongsAdapter.ViewHolder>{
    private Cursor mCursor;
    private boolean usesSongTable;
    private Activity activity;

    public SongsAdapter(Activity activity, boolean usesSongTable) {
        this.activity = activity;
        this.usesSongTable = usesSongTable;
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
        final String artist = mCursor.getString(FeedFragment.COL_ARTIST);
        final String title = mCursor.getString(FeedFragment.COL_TITLE);
        viewHolder.getArtist().setText(artist);
        viewHolder.getTitle().setText(title);

        int recent = 1;
        if (usesSongTable) {
            recent = mCursor.getInt(FeedFragment.COL_RECENT);
        }

        final int position = i;
        final ImageView starRef = viewHolder.getStar();
        if (recent == 0) {
            viewHolder.getStar().setImageResource(R.drawable.ic_action_starfill);
        } else {
            viewHolder.getStar().setImageResource(R.drawable.ic_action_star);
        }

        final String img = mCursor.getString(FeedFragment.COL_IMAGE_URL);
        if(img != null && !img.isEmpty())
            Picasso.with(activity).load(img).into(viewHolder.getArtistImage());

        viewHolder.getContainer().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, LyricsActivity.class);
                i.putExtra(ApiIntentService.EXT_TITLE, title);
                i.putExtra(ApiIntentService.EXT_ARTIST, artist);
                i.putExtra(ApiIntentService.EXT_URL, img);
                activity.startActivity(i);
            }
        });

        viewHolder.getStar().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, DataIntentService.class);
                int newr = 0;
                mCursor.moveToPosition(position);

                i.putExtra(DataIntentService.EXT_ARTIST,mCursor.getString(FeedFragment.COL_ARTIST));
                i.putExtra(DataIntentService.EXT_TITLE,mCursor.getString(FeedFragment.COL_TITLE));

                if(usesSongTable) {
                    //update the table by changing the recent value for the song
                    //recent -> saved, saved -> recent
                    int current = mCursor.getInt(FeedFragment.COL_RECENT);
                    newr = (current == 0) ? 1 : 0;
                    i.putExtra(DataIntentService.EXT_RECENT, newr);
                    i.setAction(DataIntentService.ACTION_UPDATE);
                    activity.startService(i);
                } else {
                    // This song is from the search table, so insert it into the song table to save
                    i.putExtra(DataIntentService.EXT_RECENT, 0);
                    i.putExtra(DataIntentService.EXT_IMAGE, mCursor.getString(FeedFragment.COL_IMAGE_URL));
                    i.setAction(DataIntentService.ACTION_INSERT);
                    activity.startService(i);
                }

                if(newr == 0) {
                    starRef.setImageResource(R.drawable.ic_action_starfill);
                } else {
                    starRef.setImageResource(R.drawable.ic_action_star);
                }
            }
        });
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
        private CardView container;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView)itemView.findViewById(R.id.tv_title);
            artist = (TextView) itemView.findViewById(R.id.tv_artist);
            star = (ImageView) itemView.findViewById(R.id.star);
            artistImage = (CircleImageView) itemView.findViewById(R.id.artist_image);
            container = (CardView) itemView.findViewById(R.id.card);
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

        public CardView getContainer() {
            return container;
        }
    }

    public void setCursor(Cursor mCursor) {
        this.mCursor = mCursor;
        notifyDataSetChanged();
    }
}
