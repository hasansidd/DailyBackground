package com.siddapps.android.dailybackground.data;

import android.database.Cursor;

import static com.siddapps.android.dailybackground.data.DatabaseContract.*;

public class Photo {
    public long id;
    public String title;
    public String url;

    public Photo(String title, String url) {
        this.title = title;
        this.url = url;
    }

    public Photo(Cursor c) {
        this.id = getColumnLong(c, PhotoColumns._ID);
        this.title = getColumnString(c, PhotoColumns.TITLE);
        this.url = getColumnString(c, PhotoColumns.URL);
    }

}
