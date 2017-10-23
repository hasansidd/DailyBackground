package com.siddapps.android.flickrbackground;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.image_view);
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
            final List<PhotoList.Photo> photos = photoList.getPhotos().getPhoto();

            imageView.setOnClickListener(new View.OnClickListener() {
                int counter = 1;

                @Override
                public void onClick(View view) {
                    Log.e(TAG, photos.get(counter).getUrl());
                    Picasso.with(MainActivity.this).load(photos.get(counter).getUrl()).into(imageView);
                    counter++;
                }
            });



            Log.e(TAG, photos.get(0).getUrl());
            Picasso.with(this).load(photos.get(0).getUrl()).into(imageView);
        }
    }

    @Override
    public void onFailure(Call<PhotoList> call, Throwable t) {
        t.printStackTrace();
    }
}
