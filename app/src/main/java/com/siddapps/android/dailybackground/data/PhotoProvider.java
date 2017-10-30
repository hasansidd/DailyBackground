package com.siddapps.android.dailybackground.data;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.content.FileProvider;

public class PhotoProvider extends FileProvider {
    private static final String TAG = PhotoProvider.class.getSimpleName();
    private PhotoDbHelper mPhotoDbHelper;
    private static final int PHOTOS = 100;
    private static final int PHOTOS_WITH_ID = 101;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        // content://com.siddapps.android.dailybackground/photos
        sUriMatcher.addURI(DatabaseContract.CONTENT_AUTHORITY,
                DatabaseContract.TABLE_PHOTOS,
                PHOTOS);

        // content://com.siddapps.android.dailybackground/photos/id
        sUriMatcher.addURI(DatabaseContract.CONTENT_AUTHORITY,
                DatabaseContract.TABLE_PHOTOS + "/#",
                PHOTOS_WITH_ID);
    }

    @Override
    public boolean onCreate() {
        mPhotoDbHelper = new PhotoDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final SQLiteDatabase db = mPhotoDbHelper.getReadableDatabase();
        Cursor returnCursor;

        switch (sUriMatcher.match(uri)) {
            case PHOTOS:
                returnCursor = db.query(DatabaseContract.TABLE_PHOTOS,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case PHOTOS_WITH_ID:
                String id = uri.getPathSegments().get(1);
                String mSelection = "_id=?";
                String[] mSelectionArgs = new String[]{id};

                returnCursor = db.query(DatabaseContract.TABLE_PHOTOS,
                        projection,
                        mSelection,
                        mSelectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Illegal query URI");
        }

        returnCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return returnCursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mPhotoDbHelper.getWritableDatabase();
        Uri returnUri;
        switch (sUriMatcher.match(uri)) {
            case PHOTOS:
                long id = db.insert(DatabaseContract.TABLE_PHOTOS, null, values);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(DatabaseContract.CONTENT_URI, id);
                } else {
                    throw new SQLException("Failed to insert row into: " + uri);
                }
                break;
            default:
                throw new IllegalArgumentException("Illegal insert URI");
        }

        getContext().getContentResolver().notifyChange(uri,null);
        return returnUri;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return super.update(uri, values, selection, selectionArgs);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return super.delete(uri, selection, selectionArgs);
    }
}
