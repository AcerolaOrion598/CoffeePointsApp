package com.djaphar.coffeepointapp.SupportClasses.ApiClasses;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiBuilder {

    public static PointsApi getPointsApi() {
        return new Retrofit
                .Builder()
                .baseUrl("http://212.109.219.69:3007/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(PointsApi.class);
    }
}
