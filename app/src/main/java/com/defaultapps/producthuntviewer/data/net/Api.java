package com.defaultapps.producthuntviewer.data.net;


import com.defaultapps.producthuntviewer.data.model.post.PostsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Api {
    @GET("v1/posts")
    Call<PostsResponse> getPosts(
            @Query("access_token") String accessToken,
            @Query("search[category]") String category
    );
}
