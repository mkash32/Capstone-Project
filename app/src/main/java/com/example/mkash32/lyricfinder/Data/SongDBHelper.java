package com.example.mkash32.lyricfinder.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Aakash on 11/12/16.
 */

public class SongDBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "songs.db";

    public SongDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Create a table to hold songs.
        // Songs consist of 5 values: title, artist name, artist image url, lyrics, recent (t/f)
        final String SQL_CREATE_SONG_TABLE = "CREATE TABLE " + SongContract.SongEntry.TABLE_NAME + " (" +
                SongContract.SongEntry.COLUMN_TITLE + " TEXT UNIQUE NOT NULL, " +
                SongContract.SongEntry.COLUMN_ARTIST + " TEXT NOT NULL, " +
                SongContract.SongEntry.COLUMN_IMAGE_URL + " TEXT, " +
                SongContract.SongEntry.COLUMN_LYRICS + " TEXT, " +
                SongContract.SongEntry.COLUMN_RECENT + " INTEGER, " +
                "PRIMARY KEY (" + SongContract.SongEntry.COLUMN_TITLE + ", " + SongContract.SongEntry.COLUMN_TITLE + ") " +
                " );";

        // Create table to hold search results and most popular
        // Search consists of 3 values: title, artist name, artist image url
        final String SQL_CREATE_SEARCH_TABLE = "CREATE TABLE " + SongContract.SearchEntry.TABLE_NAME + " (" +
                SongContract.SearchEntry.COLUMN_TITLE + " TEXT UNIQUE NOT NULL, " +
                SongContract.SearchEntry.COLUMN_ARTIST + " TEXT NOT NULL, " +
                SongContract.SearchEntry.COLUMN_IMAGE_URL + " TEXT, " +
                "PRIMARY KEY (" + SongContract.SearchEntry.COLUMN_TITLE + ", " + SongContract.SearchEntry.COLUMN_TITLE + ") " +
                " );";

        sqLiteDatabase.execSQL(SQL_CREATE_SONG_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_SEARCH_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + SongContract.SongEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + SongContract.SearchEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
