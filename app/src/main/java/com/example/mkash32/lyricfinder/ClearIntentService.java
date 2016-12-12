package com.example.mkash32.lyricfinder;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.example.mkash32.lyricfinder.Data.SongContract;

// Used to clear the Search Table
public class ClearIntentService extends IntentService {
    public ClearIntentService() {
        super("ClearIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            int deleted = getContentResolver().delete(SongContract.SearchEntry.CONTENT_URI, null, null);
        }
    }
}
