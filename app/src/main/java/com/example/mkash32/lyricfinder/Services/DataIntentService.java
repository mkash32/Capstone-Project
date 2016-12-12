package com.example.mkash32.lyricfinder.Services;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.example.mkash32.lyricfinder.Data.SongContract;

// Used to clear the Search Table
public class DataIntentService extends IntentService {
    public final static String ACTION_DELETE = "delete";
    public final static String ACTION_UPDATE = "update";
    public final static String ACTION_INSERT = "insert";

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
                handleUpdate(intent.getStringExtra("title"), intent.getStringExtra("artist"), intent.getIntExtra("recent",0));
            } else if(action.equals(ACTION_INSERT)) {
                handleInsert(intent.getStringExtra("title"), intent.getStringExtra("artist"),
                        intent.getStringExtra("image"), intent.getIntExtra("recent",0));
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

    public void handleInsert(String title, String artist, String image, int recent) {
        ContentValues values = new ContentValues();
        values.put(SongContract.SongEntry.COLUMN_TITLE, title);
        values.put(SongContract.SongEntry.COLUMN_ARTIST, artist);
        values.put(SongContract.SongEntry.COLUMN_IMAGE_URL, image);
        values.put(SongContract.SongEntry.COLUMN_RECENT, recent);

        Uri inserted = getContentResolver().insert(SongContract.SongEntry.CONTENT_URI, values);
    }

}