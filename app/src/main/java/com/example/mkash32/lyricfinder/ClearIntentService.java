package com.example.mkash32.lyricfinder;

import android.app.IntentService;
import android.content.Intent;

import com.example.mkash32.lyricfinder.Data.SongContract;

// Used to clear the Search Table after exiting the activity
public class ClearIntentService extends IntentService {
    public ClearIntentService() {
        super("ClearIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            getContentResolver().delete(SongContract.SearchEntry.CONTENT_URI, null, null);
        }
    }
}
