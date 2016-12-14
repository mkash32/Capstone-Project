package com.example.mkash32.lyricfinder;

/**
 * Created by Aakash on 11/12/16.
 */

public class Constants {
    //public static final String LAST_FM_API_KEY = "api_key=YOUR_LAST_FM_API_KEY";
    //public static final String MM_API_KEY = "apikey=YOUR_MUSIX_MATCH_API_KEY";
    //public static final String GN_USERNAME = "username=YOUR_GEONAME_USERNAME";

    //LastFm API Endpoint Construction Constants
    public static final int maxResults = 10;
    public static final String LF_BASE_URL = "http://ws.audioscrobbler.com/2.0/?";

        public static final String LF_TRACK_SEARCH = "method=track.search";
        public static final String LF_GEO_TOP = "method=geo.gettoptracks";
        public static final String JSON_FORMAT = "format=json";


    public static String getLFTopTracksURL(String Country) {
        return LF_BASE_URL + LF_GEO_TOP + "&country=" + "india" + "&" + LAST_FM_API_KEY + "&" + JSON_FORMAT;
    }

    public static String getLFSearchURL(String title) {
        return LF_BASE_URL + LF_TRACK_SEARCH + "&track=" + title + "&" + LAST_FM_API_KEY + "&" + JSON_FORMAT;
    }

    // MusixMatch API Endpoint Construction Constants
    public static final String MM_BASE_URL = "http://api.musixmatch.com/ws/1.1/";

        public static final String MM_GET_LYRICS = "track.lyrics.get?track_id=";
        public static final String MM_GET_TRACK = "matcher.track.get?";
        public static final String MM_ARTIST = "q_artist=";
        public static final String MM_TITLE = "q_track=";
        public static final String MM_CALLBACK = "callback=callback";

    public static String getMMLyricsUrl(long trackId) {
        return MM_BASE_URL + MM_GET_LYRICS + trackId + "&" + MM_CALLBACK + "&" + MM_API_KEY;
    }

    public static String getMMTrackUrl(String title, String artist) {
        return MM_BASE_URL + MM_GET_TRACK + MM_ARTIST + artist + "&" + MM_TITLE + title + "&" + MM_API_KEY;
    }


    // Geonames API Endpoint Construction Constants
    public static final String GN_BASE_URL = "http://api.geonames.org/countryCodeJSON?";

            public static final String GN_LAT = "lat=";
            public static final String GN_LON = "lng=";

    // Default country in case of geoname/lastfm failure
    public static final String DEFAULT_COUNTRY = "india";

    public static String getGeoNameUrl(float lat, float lon) {
        return GN_BASE_URL + GN_LAT + lat + "&" + GN_LON + lon + "&" + GN_USERNAME;
    }

}
