package com.example.mkash32.lyricfinder.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.mkash32.lyricfinder.activities.FeedFragment;
import com.example.mkash32.lyricfinder.data.SongContract;
import com.example.mkash32.lyricfinder.R;

public class LyricsViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private Cursor cursor;
    private Context context;

    public LyricsViewsFactory(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        if (cursor != null) {
            cursor.close();
        }

        // URI for saved data items
        Uri uri = SongContract.SongEntry.buildSongRecentUri(0);

        cursor = context.getContentResolver().query(uri, null, null, null, null);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if (cursor == null) {
            return 0;
        } else {
            return cursor.getCount();
        }
    }

    @Override
    public RemoteViews getViewAt(int i) {
        if (!cursor.moveToPosition(i)) {
            return null;
        }

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_list_item);
        views.setTextViewText(R.id.tv_title, cursor.getString(FeedFragment.COL_TITLE));
        views.setTextViewText(R.id.tv_artist, cursor.getString(FeedFragment.COL_ARTIST));

        final Intent fillInIntent = new Intent();
        fillInIntent.putExtra("url", cursor.getString(FeedFragment.COL_IMAGE_URL));
        fillInIntent.putExtra("title", cursor.getString(FeedFragment.COL_TITLE));
        fillInIntent.putExtra("artist", cursor.getString(FeedFragment.COL_ARTIST));
        views.setOnClickFillInIntent(R.id.list_item, fillInIntent);
        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
