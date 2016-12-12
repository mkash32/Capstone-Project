package com.example.mkash32.lyricfinder;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

import com.example.mkash32.lyricfinder.Data.SongContract;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SearchIntentService extends IntentService {

    private OkHttpClient okClient = new OkHttpClient();

    public final static String ACTION_SEARCH = "search";
    public final static String ACTION_TOP_TRACKS = "top";

    public SearchIntentService() {
        super("SearchIntentService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            String url = intent.getStringExtra("url");
            String response = executeRequest(url);
            if(response == null) {
                Log.d("IntentService", "Null response from OkHttp");
                return;
            }
            ContentValues[] values = null;
            if(action.equals(ACTION_SEARCH)) {
                values = Utilities.parseJsonSearch(response);
            } else if(action.equals(ACTION_TOP_TRACKS)) {
                values = Utilities.parseJsonTop(response);
            }

            if (values != null) {
                getContentResolver().bulkInsert(SongContract.SearchEntry.CONTENT_URI, values);
            }
        }
    }

    public String executeRequest(String url) {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = null;
        try {
            response = okClient.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
