package com.example.mkash32.lyricfinder;

/**
 * Created by Aakash on 11/12/16.
 */

public class Constants {
    public static final int maxResults = 10;
    public static final String LF_BASE_URL = "http://ws.audioscrobbler.com/2.0/?";

        public static final String LF_TRACK_SEARCH = "method=track.search";
        public static final String LF_GEO_TOP = "method=geo.gettoptracks";
        //public static final String LAST_FM_API_KEY = "api_key=";
        public static final String JSON_FORMAT = "format=json";

    //public static final String MUSIX_MATCH_API_KEY = "";

    public static String getTopTracksURL(String Country) {
        return LF_BASE_URL + LF_GEO_TOP + "&country=" + "india" + "&" + LAST_FM_API_KEY + "&" + JSON_FORMAT;
    }

    public static String getSearchURL(String title) {
        return LF_BASE_URL + LF_TRACK_SEARCH + "&track=" + title + "&" + LAST_FM_API_KEY + "&" + JSON_FORMAT;
    }
}
