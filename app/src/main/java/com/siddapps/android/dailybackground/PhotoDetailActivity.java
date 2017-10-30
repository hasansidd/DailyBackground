package com.siddapps.android.dailybackground;

import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class PhotoDetailActivity extends AppCompatActivity {
    public static final String TAG = "PhotoDetailActivity";
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
                WallpaperManager wallpaperManager = WallpaperManager.getInstance(PhotoDetailActivity.this);
                    Uri uri = Uri.parse(urlString);
                    Intent i = wallpaperManager.getCropAndSetWallpaperIntent(uri);
                    startActivity(i);
                    Snackbar.make(mLinearLayout, "Wallpaper set!", Snackbar.LENGTH_SHORT).show();
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
}
