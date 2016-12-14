package com.example.mkash32.lyricfinder;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.mkash32.lyricfinder.data.SongContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Aakash on 11/12/16.
 */

public class Utilities {

    public static ContentValues[] parseJsonTop(String string) {
        try {
            JSONObject object = new JSONObject(string);
            if(object.has("error")) {
                int errorCode = object.getInt("error");
                if(errorCode == 6) {
                    // Country param error
                    ContentValues[] errors = new ContentValues[1];
                    ContentValues error = new ContentValues();
                    error.put("error", 6);
                    errors[0] = error;
                    return errors;
                }
            }
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

    public static String getCountryFromJson(String string) {
        try {
            JSONObject object = new JSONObject(string);
            String countryName = object.getString("countryName");
            return countryName.trim().toLowerCase();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return Constants.DEFAULT_COUNTRY;
    }


    public static boolean isNetworkAvailable(Context c) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static final String md5(final String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                String h = Integer.toHexString(0xFF & messageDigest[i]);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
