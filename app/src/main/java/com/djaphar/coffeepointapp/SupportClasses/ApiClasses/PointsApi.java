package com.djaphar.coffeepointapp.SupportClasses.ApiClasses;

import com.djaphar.coffeepointapp.SupportClasses.LocalDataClasses.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface PointsApi {

    @POST("api/auth/login")
    Call<User> logIn(@Body Credentials credentials);
}
