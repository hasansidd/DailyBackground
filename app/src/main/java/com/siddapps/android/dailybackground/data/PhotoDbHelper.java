package com.siddapps.android.dailybackground.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.siddapps.android.dailybackground.data.DatabaseContract.*;

public class PhotoDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "photos.db";
    private static final int DATABASE_VERSION = 1;

    private static final String SQL_CREATE_TABLE_TASKS = String.format("CREATE TABLE %s"
                    +" (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s INTEGER)",
            TABLE_PHOTOS,
            PhotoColumns._ID,
            PhotoColumns.TITLE,
            PhotoColumns.URL
    );

    public PhotoDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_TASKS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.TABLE_PHOTOS);
        onCreate(db);
    }
}
