package com.example.mkash32.lyricfinder.Services;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

import com.example.mkash32.lyricfinder.Constants;
import com.example.mkash32.lyricfinder.Data.SongContract;
import com.example.mkash32.lyricfinder.Utilities;

import org.jmusixmatch.MusixMatch;
import org.jmusixmatch.MusixMatchException;
import org.jmusixmatch.entity.lyrics.Lyrics;
import org.jmusixmatch.entity.track.Track;
import org.jmusixmatch.entity.track.TrackData;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ApiIntentService extends IntentService {

    private OkHttpClient okClient = new OkHttpClient();
    private MusixMatch musixMatch = new MusixMatch(Constants.MUSIX_MATCH_API_KEY);

    public final static String ACTION_SEARCH = "search";
    public final static String ACTION_TOP_TRACKS = "top";
    public final static String ACTION_GEONAME = "geo";
    public final static String ACTION_LYRICS = "lyrics";

    public ApiIntentService() {
        super("ApiIntentService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            String action = intent.getAction();
            if(action.equals(ACTION_GEONAME)) {
                handleGeoname();
            } else if(action.equals(ACTION_LYRICS)) {
                handleLyrics(null,null);    //TODO: modify this
            } else if(action.equals(ACTION_SEARCH)) {
                handleSearchTop(intent.getStringExtra("url"), true);
            } else {
                handleSearchTop(intent.getStringExtra("url"), false);
            }
        }
    }

    public void handleSearchTop(String url, boolean isSearch) {
        String response = executeRequest(url);
        if(response == null) {
            Log.d("IntentService", "Null response from OkHttp");
            return;
        }
        ContentValues[] values = null;
        if(isSearch) {
            values = Utilities.parseJsonSearch(response);
        } else {
            values = Utilities.parseJsonTop(response);
        }

        if (values != null) {
            getContentResolver().bulkInsert(SongContract.SearchEntry.CONTENT_URI, values);
        }
    }

    public void handleGeoname() {

    }

    public void handleLyrics(String title, String artist) {
        Log.d("musix", "finding lyrics");
        title = "Don't stop the Party";
        artist = "The Black Eyed Peas";
        Track track = null;
        try {
            Log.d("musix", "matching");
            track = musixMatch.getMatchingTrack(title, artist);
            TrackData data = track.getTrack();

            int trackID = data.getTrackId();
            Log.d("musix", "track id is " + trackID);
            Lyrics lyrics = musixMatch.getLyrics(trackID);
            Log.d("musix", "Getting lyrics " + lyrics.getLyricsBody());
        } catch (MusixMatchException e) {
            e.printStackTrace();
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
