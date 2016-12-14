package com.example.mkash32.lyricfinder.services;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.example.mkash32.lyricfinder.activities.FeedFragment;
import com.example.mkash32.lyricfinder.activities.LyricsActivity;
import com.example.mkash32.lyricfinder.Constants;
import com.example.mkash32.lyricfinder.data.SongContract;
import com.example.mkash32.lyricfinder.Utilities;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ApiIntentService extends IntentService {

    private OkHttpClient okClient = new OkHttpClient();

    public final static String ACTION_SEARCH = "search";
    public final static String ACTION_TOP_TRACKS = "top";
    public final static String ACTION_GEONAME = "geo";
    public final static String ACTION_LYRICS = "lyrics";

    public final static String EXT_TITLE = "title";
    public final static String EXT_ARTIST = "artist";
    public final static String EXT_URL = "url";
    public final static String EXT_LAT = "lat";
    public final static String EXT_LON = "lng";

    public final static String RESPONSE_LYRICS = ACTION_LYRICS;
    public final static String RESPONSE_SHOULD_INSERT = "insert";
    public final static String RESPONSE_COUNTRY = "country";

    public final static float LAT_LON_ERR = 200;    // LAT/LON not found

    public ApiIntentService() {
        super("ApiIntentService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            String action = intent.getAction();
            if(action.equals(ACTION_GEONAME)) {
                handleGeoname(intent.getFloatExtra(EXT_LAT, LAT_LON_ERR), intent.getFloatExtra(EXT_LON, LAT_LON_ERR));
            } else if(action.equals(ACTION_LYRICS)) {
                handleLyrics(intent.getStringExtra(EXT_TITLE), intent.getStringExtra(EXT_ARTIST));
            } else if(action.equals(ACTION_SEARCH)) {
                handleSearchTop(intent.getStringExtra(EXT_URL), true);
            } else {
                handleSearchTop(intent.getStringExtra(EXT_URL), false);
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
            if(values[0].containsKey("error")) {
                // Country param error received, retry request with default country
                handleSearchTop(Constants.getLFTopTracksURL(Constants.DEFAULT_COUNTRY),false);
                return;
            }
        }

        if (values != null) {
            getContentResolver().bulkInsert(SongContract.SearchEntry.CONTENT_URI, values);
        }
    }

    // Broadcasts the country name retreived from Geonames api
    // In case of invalid response, Default country is returned
    public void handleGeoname(float lat, float lon) {
        if(lat == LAT_LON_ERR || lon == LAT_LON_ERR) {
            broadcastCountryName(Constants.DEFAULT_COUNTRY);
            return;
        }

        String url = Constants.getGeoNameUrl(lat, lon);
        String response = executeRequest(url);
        if(response == null) {
            Log.d("IntentService", "Null response from OkHttp");
            broadcastCountryName(Constants.DEFAULT_COUNTRY);
            return;
        }

        String country = Utilities.getCountryFromJson(response);
        broadcastCountryName(country);
    }

    // Looks for lyrics in the DB, if not present requests MusixMatch
    // Lyrics will be broadcast to the LyricsActivity
    public void handleLyrics(String title, String artist) {
        Uri uri = SongContract.SongEntry.buildTitleArtistUri(title, artist);
        Cursor mCursor = getContentResolver().query(uri, null, null, null, null);
        if(mCursor.moveToFirst()) {
            String lyrics = mCursor.getString(FeedFragment.COL_LYRICS);
            if(lyrics != null && !lyrics.isEmpty()) {
                broadcastLyrics(lyrics, false);
                return;
            }
        }
        mCursor.close();
        String url = Constants.getMMTrackUrl(title,artist);
        String response = executeRequest(url);
        if(response == null) {
            Log.d("IntentService", "Null response from OkHttp");
            return;
        }

        long trackId = Utilities.parseTrack(response);
        if(trackId == -1) {
            // Lyrics not present in MusixMatch DB
        } else if(trackId == -2) {
            // Parsing error
            return;
        }

        url = Constants.getMMLyricsUrl(trackId);
        response = executeRequest(url);
        if(response == null) {
            Log.d("IntentService", "Null response from OkHttp");
            return;
        }

        String lyrics = Utilities.parseLyrics(response);
        int updated = 0;
        if(lyrics != null && !lyrics.isEmpty()) {
            lyrics = lyrics.substring(0, lyrics.length() - 70);  // Remove "non commercial use" suffix of the lyric string
            ContentValues values = new ContentValues();
            values.put(SongContract.SongEntry.COLUMN_LYRICS, lyrics);
            String[] selectionArgs = new String[]{title, artist};

            updated = getContentResolver().update(SongContract.SongEntry.buildSongRecentUri(0), values, null, selectionArgs);
            Log.d("API Intent service", "Rows (lyrics) updated " + updated);
        }

        // No rows updated means the particular song doesn't exist in the db, should be inserted
        if(updated == 0)
            broadcastLyrics(lyrics, true);
        else
            broadcastLyrics(lyrics, false);
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

    public void broadcastLyrics(String lyrics, boolean shouldInsert) {
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(LyricsActivity.LyricsReceiver.PROCESS_RESPONSE);
        broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
        broadcastIntent.putExtra(RESPONSE_LYRICS, lyrics);
        broadcastIntent.putExtra(RESPONSE_SHOULD_INSERT, shouldInsert);
        sendBroadcast(broadcastIntent);
    }

    public void broadcastCountryName(String country) {
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(FeedFragment.CountryReceiver.PROCESS_COUNTRY_RESPONSE);
        broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
        broadcastIntent.putExtra(RESPONSE_COUNTRY, country);
        sendBroadcast(broadcastIntent);
    }

}
