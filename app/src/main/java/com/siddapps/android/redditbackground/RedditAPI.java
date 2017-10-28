package com.siddapps.android.redditbackground;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RedditAPI {
    String FROM_HOUR = "hour";
    String FROM_WEEK = "week";
    String FROM_MONTH = "month";
    String FROM_YEAR = "year";
    String FROM_ALL = "all";

    @GET("r/{subreddit}/top.json")
    Call<RedditListing> getRecentTopSubmissions(@Path("subreddit") String subreddit,
                                         @Query("t") String time,
                                         @Query("limit") int limit);
}

