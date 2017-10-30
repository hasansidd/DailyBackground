package com.siddapps.android.dailybackground.data;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

public class PhotoUpdateService extends IntentService {
    private static final String TAG = PhotoUpdateService.class.getSimpleName();
    public static final String ACTION_INSERT = "PhotoUpdateService.INSERT";
    public static final String ACTION_UPDATE = "PhotoUpdateService.UPDATE";
    public static final String ACTION_DELETE = "PhotoUpdateService.DELETE";
    public static final String EXTRA_VALUES = "PhotoUpdateService.ContentValues";

    public PhotoUpdateService() {
        super(TAG);
    }

    public static void insertNewPhoto(Context context, ContentValues values) {
        Intent intent = new Intent(context, PhotoUpdateService.class);
        intent.setAction(ACTION_INSERT);
        intent.putExtra(EXTRA_VALUES, values);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        ContentValues values;
        switch (intent.getAction().toString()) {
            case ACTION_INSERT:
                values = intent.getParcelableExtra(EXTRA_VALUES);
                performInsert(values);
                return;
            case ACTION_UPDATE:
                values = intent.getParcelableExtra(EXTRA_VALUES);
                performUpdate(intent.getData(), values);
                return;
            case ACTION_DELETE:
                performDelete(intent.getData());
                return;
            default:
                return;
        }
    }

    private void performDelete(Uri uri) {
        int count = getContentResolver().delete(uri, null, null);
        Log.d(TAG, "Deleted " + count + " tasks");
    }

    private void performUpdate(Uri uri, ContentValues values) {
        int count = getContentResolver().update(uri, values, null, null);
        Log.d(TAG, "Updated " + count + " task items");
    }

    private void performInsert(ContentValues values) {
        if (getContentResolver().insert(DatabaseContract.CONTENT_URI, values) != null) {
            Log.d(TAG, "Inserted new task");
        } else {
            Log.w(TAG, "Error inserting new task");
        }
    }
}
