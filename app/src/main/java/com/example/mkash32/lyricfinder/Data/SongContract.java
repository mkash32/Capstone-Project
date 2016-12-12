package com.example.mkash32.lyricfinder.Data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Aakash on 11/12/16.
 */

public class SongContract {

    public static final String CONTENT_AUTHORITY = "com.example.mkash32.lyricfinder.app";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Paths
    public static final String PATH_SONG = "song";
    public static final String PATH_SEARCH = "search";

    /* Inner class that defines the table contents of the Song table */
    public static final class SongEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_SONG).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SONG;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SONG;

        // Table name
        public static final String TABLE_NAME = "song";

        // Columns
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_ARTIST = "artist";
        public static final String COLUMN_IMAGE_URL = "image_url";
        public static final String COLUMN_LYRICS = "lyrics";
        public static final String COLUMN_RECENT = "recent";

        public static Uri buildSongUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildSongRecentUri(int isRecent) {
            return CONTENT_URI.buildUpon().appendPath(""+isRecent).build();
        }

        public static Uri buildTitleArtistUri(String title, String artist) {
            return CONTENT_URI.buildUpon().appendPath(title).appendPath(artist).build();
        }

        public static long getRecentFromUri(Uri uri) {
            return Long.parseLong(uri.getPathSegments().get(1));
        }

        public static String getTitleFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }

        public static String getArtistFromUri(Uri uri) {
            return uri.getPathSegments().get(2);
        }
    }

    /* Inner class that defines the table contents of the Search table */
    public static final class SearchEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_SEARCH).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SEARCH;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SEARCH;

        // Table name
        public static final String TABLE_NAME = "search";

        // Columns
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_ARTIST = "artist";
        public static final String COLUMN_IMAGE_URL = "image_url";
    }
}
