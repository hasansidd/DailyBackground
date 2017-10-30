package com.siddapps.android.dailybackground.data;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseContract {
    public static final String TABLE_PHOTOS = "photos";

    public static final class PhotoColumns implements BaseColumns {
        public static final String TITLE = "title";
        public static final String URL = "url";
    }
    public static final String CONTENT_AUTHORITY = "com.siddapps.android.dailybackground";

    public static final Uri CONTENT_URI = new Uri.Builder().scheme("content")
            .authority(CONTENT_AUTHORITY)
            .appendPath(TABLE_PHOTOS)
            .build();

    public static String getColumnString(Cursor cursor, String columnName) {
        return cursor.getString( cursor.getColumnIndex(columnName) );
    }

    public static long getColumnLong(Cursor cursor, String columnName) {
        return cursor.getLong( cursor.getColumnIndex(columnName) );
    }
}
