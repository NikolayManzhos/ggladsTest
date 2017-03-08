package com.defaultapps.producthuntviewer.data.net;

import javax.inject.Inject;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkService {

    private final String BASE_URL = "https://api.producthunt.com/";
    private Retrofit retrofit;

    @Inject
    public NetworkService() {
        retrofit = getRetrofit();
    }

    private Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build();
    }

    public Api getNetworkCall() {
        return retrofit.create(Api.class);
    }
}
