package com.siddapps.android.flickrbackground;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements Callback<PhotoList> {
    private static final String TAG = "MainActivity";
    private static final String BASE_URL = "https://api.flickr.com/services/rest/";
    ImageView imageView;
    private Button mButton;
    List<PhotoList.Photo> mPhotos;
    int counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        counter = 0;
        imageView = (ImageView) findViewById(R.id.image_view);
        mButton = (Button) findViewById(R.id.button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPhotos.get(counter).getUrlk() != null) {
                    Intent i = PhotoDetailActivity.newIntent(MainActivity.this, mPhotos.get(counter).getUrlk());
                    startActivity(i);
                } else {
                    Toast.makeText(MainActivity.this, "Large Image Not Available", Toast.LENGTH_SHORT).show();
                }
            }
        });
        start();
    }

    public void start() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        FlickrAPI flickrAPI = retrofit.create(FlickrAPI.class);

        Call<PhotoList> call = flickrAPI.getPhotosByTag("dog");
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<PhotoList> call, Response<PhotoList> response) {
        if (response.isSuccessful()) {
            PhotoList photoList = response.body();
            mPhotos = photoList.getPhotos().getPhoto();

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    counter++;
                    Log.e(TAG, mPhotos.get(counter).getUrlq() + ": " + counter);
                    if (mPhotos.get(counter).getUrlq() != null) {
                        Picasso.with(MainActivity.this).load(mPhotos.get(counter).getUrlq()).into(imageView);
                    }
                }
            });

            Log.e(TAG, " Initialize imageview: " + counter);
            if (mPhotos.get(counter).getUrlq() != null) {
                Picasso.with(MainActivity.this).load(mPhotos.get(counter).getUrlq()).into(imageView);
            } else {
                while (mPhotos.get(counter).getUrlq() == null) {
                    Log.e(TAG, mPhotos.get(counter).getUrlq() + " from: " + counter);
                    Picasso.with(MainActivity.this).load(mPhotos.get(counter).getUrlq()).into(imageView);
                    counter++;
                }
            }
        }
    }

    @Override
    public void onFailure(Call<PhotoList> call, Throwable t) {
        t.printStackTrace();
    }
}



