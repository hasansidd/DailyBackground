package com.siddapps.android.redditbackground;

import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.IOException;

public class PhotoDetailActivity extends AppCompatActivity {
    public static final String TAG = "PhotoDetailActivity";
    private ImageView mImageView;
    private Button mButton;
    private LinearLayout mLinearLayout;

    public static Intent newIntent(Context context, String url) {
        Intent i = new Intent(context, PhotoDetailActivity.class);
        i.putExtra("URL", url);
        return i;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);
        this.getSupportActionBar().hide();
        mLinearLayout = (LinearLayout) findViewById(R.id.linear_layour);

        final String url = getIntent().getStringExtra("URL");
        mImageView = (ImageView) findViewById(R.id.image_view);

        Picasso.with(this).load(url).into(mImageView);

        mButton = (Button) findViewById(R.id.button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Picasso.with(PhotoDetailActivity.this)
                        .load(url)
                        .into(new Target() {
                            @Override
                            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                                WallpaperManager wallpaperManager = WallpaperManager.getInstance(PhotoDetailActivity.this);
                                try {
                                    wallpaperManager.setBitmap(bitmap);
                                    Snackbar.make(mLinearLayout, "Wallpaper set!", Snackbar.LENGTH_SHORT).show();
                                } catch (IOException e) {
                                    Snackbar.make(mLinearLayout, "Wallpaper not set: error", Snackbar.LENGTH_SHORT).show();
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {
                            }

                            @Override
                            public void onBitmapFailed(Drawable errorDrawable) {
                            }
                        });
            }
        });
    }
}
