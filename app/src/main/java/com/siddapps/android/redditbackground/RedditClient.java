package com.siddapps.android.redditbackground;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RedditClient {
    private static final String BASE_URL = "https://www.reddit.com";
    private static final String TAG = "RedditClient";
    private final String SUBREDDITS =  "EarthPorn+CityPorn+SkyPorn+WeatherPorn+BotanicalPorn+LakePorn+VillagePorn+BeachPorn+WaterPorn+SpacePorn+RoomPorn+wallpapers+wallpaper";

    public Call<RedditListing> start() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        RedditAPI redditAPI = retrofit.create(RedditAPI.class);


        Call<RedditListing> call = redditAPI.getRecentTopSubmissions(
                SUBREDDITS,
                RedditAPI.FROM_WEEK,
                50);

        return call;
    }
}
