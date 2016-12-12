package com.example.mkash32.lyricfinder.Data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class SongProvider extends ContentProvider {
    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private SongDBHelper mSongHelper;

    static final int SONG = 100;    // General
    static final int SONG_RS = 101; // Recent or Saved songs
    static final int SEARCH = 102;

    private static final SQLiteQueryBuilder songQueryBuilder;
    private static final SQLiteQueryBuilder searchQueryBuilder;

    static{
        songQueryBuilder = new SQLiteQueryBuilder();
        songQueryBuilder.setTables(SongContract.SongEntry.TABLE_NAME);


        searchQueryBuilder = new SQLiteQueryBuilder();
        searchQueryBuilder.setTables(SongContract.SearchEntry.TABLE_NAME);
    }

    //title = ?
    private static final String sTitleSelection = SongContract.SongEntry.COLUMN_TITLE + " = ? ";

    //artist = ?
    private static final String sArtistSelection = SongContract.SongEntry.COLUMN_ARTIST + " = ? ";

    //title = ? AND artist = ?
    private static final String sTitleAndArtistSelection = SongContract.SongEntry.COLUMN_TITLE + " = ? AND " +
                    SongContract.SongEntry.COLUMN_ARTIST + " = ? ";
    // recent = 1
    private static final String sRecentSearchedSelection = SongContract.SongEntry.COLUMN_RECENT + " = 1 ";

    // recent  = 0
    private static final String sSavedSelection = SongContract.SongEntry.COLUMN_RECENT + "= 0 ";


    private Cursor getRecentOrSavedSongs(Uri uri, String[] projection, String sortOrder) {
        long recent = SongContract.SongEntry.getRecentFromUri(uri);

        String selection;

        if (recent == 1) {
            selection = sRecentSearchedSelection;
        } else {
            selection = sSavedSelection;
        }

        return songQueryBuilder.query(mSongHelper.getReadableDatabase(),
                projection,
                selection,
                null,
                null,
                null,
                sortOrder
        );
    }

    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = SongContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, SongContract.PATH_SONG, SONG);
        matcher.addURI(authority, SongContract.PATH_SONG + "/#" , SONG_RS);
        matcher.addURI(authority, SongContract.PATH_SEARCH, SEARCH);

        return matcher;
    }

    public SongProvider() {
    }

    @Override
    public boolean onCreate() {
        mSongHelper = new SongDBHelper(getContext());
        return true;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mSongHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;

        // this makes delete all rows return the number of rows deleted
        if ( null == selection ) selection = "1";

        switch (match) {
            case SONG:
                rowsDeleted = db.delete(SongContract.SongEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case SEARCH:
                rowsDeleted = db.delete(SongContract.SearchEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case SONG:
                return SongContract.SongEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mSongHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case SONG: {
                long _id = db.insert(SongContract.SongEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = SongContract.SongEntry.buildSongUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            // "song"
            case SONG: {
                retCursor = mSongHelper.getReadableDatabase().query(
                        SongContract.SongEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            // "song/#"
            case SONG_RS: {
                retCursor = getRecentOrSavedSongs(uri, projection, sortOrder);
                break;
            }
            // "search"
            case SEARCH: {
                retCursor = mSongHelper.getReadableDatabase().query(
                        SongContract.SearchEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        final SQLiteDatabase db = mSongHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case SONG:
                rowsUpdated = db.update(SongContract.SongEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mSongHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case SEARCH:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        try {
                        long _id = db.insertOrThrow(SongContract.SearchEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        } } catch (SQLiteConstraintException e) {
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }
}
