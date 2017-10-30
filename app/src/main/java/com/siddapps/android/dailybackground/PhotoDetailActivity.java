package com.siddapps.android.dailybackground;

import android.Manifest;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;

public class PhotoDetailActivity extends AppCompatActivity {
    public static final String TAG = "PhotoDetailActivity";
    public static final int PERMISSION_CODE_WRITE_EXTERNAL_STORAGE = 1;
    private ImageView mImageView;
    private Button mButton;
    private LinearLayout mLinearLayout;
    private Bitmap mBitmap;
    private Target mTarget;

    public static Intent newIntent(Context context, String urlString) {
        Intent i = new Intent(context, PhotoDetailActivity.class);
        i.putExtra("URL", urlString);
        return i;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);
        this.getSupportActionBar().hide();
        mLinearLayout = (LinearLayout) findViewById(R.id.linear_layour);

        final String urlString = getIntent().getStringExtra("URL");
        mImageView = (ImageView) findViewById(R.id.image_view);
        mButton = (Button) findViewById(R.id.button);
        mButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mButton.setEnabled(false);
                setWallpaper();
            }
        });

        mTarget = new Target() {
            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                Log.e(TAG, "loaded");
                mBitmap = bitmap;
                mImageView.setImageBitmap(bitmap);
                mButton.setEnabled(true);
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                Log.e(TAG, "preparing");
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                Log.e(TAG, "failed");
            }
        };

        Picasso.with(PhotoDetailActivity.this)
                .load(urlString)
                .into(mTarget);
    }


    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_CODE_WRITE_EXTERNAL_STORAGE);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_CODE_WRITE_EXTERNAL_STORAGE:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    setWallpaper();
                } else {
                    mButton.setEnabled(true);
                }
                break;
            default:
                mButton.setEnabled(true);
        }
    }

    private void setWallpaper() {
        if (checkPermission()) {
            WallpaperManager wallpaperManager = WallpaperManager.getInstance(PhotoDetailActivity.this);
            Intent i = wallpaperManager.getCropAndSetWallpaperIntent(getImageUri(mBitmap, PhotoDetailActivity.this));
            startActivity(i);
            Snackbar.make(mLinearLayout, "Wallpaper set!", Snackbar.LENGTH_SHORT).show();
        }
    }

    private Uri getImageUri(Bitmap bitmap, Context context) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Title", null);
        Log.e(TAG, path);
        return Uri.parse(path);
    }
}
