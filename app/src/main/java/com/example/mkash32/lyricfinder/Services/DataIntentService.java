package com.example.mkash32.lyricfinder.services;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.example.mkash32.lyricfinder.data.SongContract;

// Used to clear the Search Table
public class DataIntentService extends IntentService {
    public final static String ACTION_DELETE = "delete";
    public final static String ACTION_UPDATE = "update";
    public final static String ACTION_INSERT = "insert";

    public final static String EXT_TITLE = "title";
    public final static String EXT_ARTIST = "artist";
    public final static String EXT_IMAGE = "image";
    public final static String EXT_RECENT = "recent";
    public final static String EXT_LYRICS = "lyrics";



    public DataIntentService() {
        super("DataIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            String action = intent.getAction();
            if(action.equals(ACTION_DELETE)) {
                handleDelete();
            } else if(action.equals(ACTION_UPDATE)) {
                handleUpdate(intent.getStringExtra(EXT_TITLE), intent.getStringExtra(EXT_ARTIST), intent.getIntExtra(EXT_RECENT,0));
            } else if(action.equals(ACTION_INSERT)) {
                handleInsert(intent.getStringExtra(EXT_TITLE), intent.getStringExtra(EXT_ARTIST),
                        intent.getStringExtra(EXT_IMAGE), intent.getStringExtra(EXT_LYRICS),
                        intent.getIntExtra(EXT_RECENT,0));
            }
        }
    }

    public void handleDelete() {
        int deleted = getContentResolver().delete(SongContract.SearchEntry.CONTENT_URI, null, null);
        Log.d("Data service", "Rows deleted " + deleted);
    }

    public void handleUpdate(String title, String artist, int recent) {
        ContentValues values = new ContentValues();
        values.put(SongContract.SongEntry.COLUMN_RECENT, recent);
        String[] selectionArgs = new String[]{title, artist};

        int updated = getContentResolver().update(SongContract.SongEntry.buildSongRecentUri(recent), values, null, selectionArgs);
        Log.d("Data service", "Rows updated " + updated);
    }

    public void handleInsert(String title, String artist, String image, String lyrics, int recent) {
        ContentValues values = new ContentValues();
        values.put(SongContract.SongEntry.COLUMN_TITLE, title);
        values.put(SongContract.SongEntry.COLUMN_ARTIST, artist);
        values.put(SongContract.SongEntry.COLUMN_IMAGE_URL, image);
        values.put(SongContract.SongEntry.COLUMN_RECENT, recent);
        if(lyrics != null)
            values.put(SongContract.SongEntry.COLUMN_LYRICS, lyrics);


        Uri inserted = getContentResolver().insert(SongContract.SongEntry.CONTENT_URI, values);
    }

}