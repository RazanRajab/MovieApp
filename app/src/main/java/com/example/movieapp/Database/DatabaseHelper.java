package com.example.movieapp.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int VERSION_NUMBER = 1;
    private static final String DATABASE_NAME = "movies.db";
    public static final String FAVORITES_TABLE = "favoriteMovies";

    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, VERSION_NUMBER);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String CREATE_FAVORITE_MOVIESS_TABLE =
                "CREATE TABLE " + FAVORITES_TABLE
                        + " ("
                        + MyContentProvider.COLUMN_MOVIE_ID + " INTEGER NOT NULL, "
                        + MyContentProvider.COLUMN_TITLE + " TEXT , "
                        + MyContentProvider.COLUMN_RELEASE_DATE + " TEXT, "
                        + MyContentProvider.COLUMN_POPULARITY + " DOUBLE , "
                        + MyContentProvider.COLUMN_POSTER + " TEXT, "
                        + MyContentProvider.COLUMN_RATING + " DOUBLE, "
                        + MyContentProvider.COLUMN_OVERVIEW + " TEXT);";
        sqLiteDatabase.execSQL(CREATE_FAVORITE_MOVIESS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // if version number is different call this method
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FAVORITES_TABLE);
        onCreate(sqLiteDatabase);
    }
}
