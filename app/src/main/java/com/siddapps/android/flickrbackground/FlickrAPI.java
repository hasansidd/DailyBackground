package com.siddapps.android.flickrbackground;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FlickrAPI {
    @GET("?method=flickr.photos.search&&api_key=8c428b9e6fa55da02aeadcd7e6fe1b2f&format=json&nojsoncallback=true&extras=url_k,url_q")
    Call<PhotoList> getPhotosByTag(@Query("tags") String tag);

}