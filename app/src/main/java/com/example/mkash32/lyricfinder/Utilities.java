package com.example.mkash32.lyricfinder;

import android.content.ContentValues;

import com.example.mkash32.lyricfinder.Data.SongContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Aakash on 11/12/16.
 */

public class Utilities {

    public static ContentValues[] parseJsonTop(String string) {
        try {
            JSONObject object = new JSONObject(string);
            JSONArray array = object.getJSONObject("tracks").getJSONArray("track");
            int size = Math.min(array.length(), Constants.maxResults);
            ContentValues[] values = new ContentValues[size];
            for(int i = 0; i < size; i++) {
                JSONObject songObj = array.getJSONObject(i);
                String title = songObj.getString("name");
                String artist = songObj.getJSONObject("artist").getString("name");
                String imageUrl = songObj.getJSONArray("image").getJSONObject(2).getString("#text");

                ContentValues value = new ContentValues();
                value.put(SongContract.SearchEntry.COLUMN_TITLE, title);
                value.put(SongContract.SearchEntry.COLUMN_ARTIST, artist);
                value.put(SongContract.SearchEntry.COLUMN_IMAGE_URL, imageUrl);
                values[i] = value;
            }
            return values;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ContentValues[] parseJsonSearch(String string) {
        try {
            JSONObject object = new JSONObject(string);
            JSONArray array = object.getJSONObject("results").getJSONObject("trackmatches").getJSONArray("track");
            int size = Math.min(array.length(), Constants.maxResults);
            ContentValues[] values = new ContentValues[size];
            for(int i = 0; i < size; i++) {
                JSONObject songObj = array.getJSONObject(i);
                String title = songObj.getString("name");
                String artist = songObj.getString("artist");
                String imageUrl = songObj.getJSONArray("image").getJSONObject(2).getString("#text");

                ContentValues value = new ContentValues();
                value.put(SongContract.SearchEntry.COLUMN_TITLE, title);
                value.put(SongContract.SearchEntry.COLUMN_ARTIST, artist);
                value.put(SongContract.SearchEntry.COLUMN_IMAGE_URL, imageUrl);
                values[i] = value;
            }
            return values;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static long parseTrack(String string) {
        try {
            JSONObject object = new JSONObject(string);
            JSONObject track = object.getJSONObject("message").getJSONObject("body").getJSONObject("track");
            long trackId = track.getLong("track_id");
            int has_lyrics = track.getInt("has_lyrics");
            if(has_lyrics == 0) {
                return -1;
            }
            return trackId;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return -2;
    }

    public static String parseLyrics(String string) {
        try {
            JSONObject object = new JSONObject(string);
            JSONObject lyricsObj = object.getJSONObject("message").getJSONObject("body").getJSONObject("lyrics");
            String lyrics = lyricsObj.getString("lyrics_body");
            return lyrics;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
