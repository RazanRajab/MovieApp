package com.example.movieapp.Database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class MyContentProvider extends ContentProvider {

    private DatabaseHelper databaseHelper;

    public static final Uri URI_BASE = Uri.parse("content://com.example.movieapp");
    public static final Uri CONTENT_URI = URI_BASE.buildUpon()
            .appendPath(DatabaseHelper.FAVORITES_TABLE).build();

    //the columns names
    public static final String COLUMN_MOVIE_ID = "movie_id";
    public static final String COLUMN_TITLE = "original_title";
    public static final String COLUMN_POSTER = "poster_path";
    public static final String COLUMN_OVERVIEW = "overview";
    public static final String COLUMN_POPULARITY = "popularity";
    public static final String COLUMN_RATING = "user_rating";
    public static final String COLUMN_RELEASE_DATE = "release_date";

    @Override
    public boolean onCreate() {
        databaseHelper = new DatabaseHelper(this.getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        Cursor cursor;
        cursor = databaseHelper.getReadableDatabase().query(
                DatabaseHelper.FAVORITES_TABLE,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final SQLiteDatabase db = databaseHelper.getWritableDatabase();
        long movieID = db.insert(databaseHelper.FAVORITES_TABLE, null, contentValues);
        if (movieID > 0) {
            Uri result = ContentUris.withAppendedId(uri, movieID);
            getContext().getContentResolver().notifyChange(result, null);
            return result;
        } else {
            return null;
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase database = databaseHelper.getWritableDatabase();
        if (null == selection) {
            selection = "1";
        }
        int deleted = database.delete(DatabaseHelper.FAVORITES_TABLE, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return deleted;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }
}
