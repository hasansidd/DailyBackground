package com.siddapps.android.flickrbackground;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class PhotoDetailActivity extends AppCompatActivity {
    public static final String TAG = "PhotoDetailActivity";
    private ImageView mImageView;

    public static Intent newIntent(Context context, String url) {
        Intent i = new Intent(context, PhotoDetailActivity.class);
        i.putExtra("URL", url);
        return i;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);

        String url = getIntent().getStringExtra("URL");
        mImageView = (ImageView) findViewById(R.id.image_view);

        Log.e(TAG, url);
        Picasso.with(this).load(url).into(mImageView);
    }
}
