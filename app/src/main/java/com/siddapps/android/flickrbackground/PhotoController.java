package com.siddapps.android.flickrbackground;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PhotoController implements Callback<PhotoList>{
    private static final String BASE_URL = "https://api.flickr.com/services/rest/";
    private static final String TAG = "PhotoController";

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
            List<PhotoList.Photo> photos = photoList.getPhotos().getPhoto();
            for (PhotoList.Photo photo : photos){
                Log.e(TAG, photo.getTitle());
            }
        }
    }

    @Override
    public void onFailure(Call<PhotoList> call, Throwable t) {
        t.printStackTrace();
    }
}
