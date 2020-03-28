package com.djaphar.coffeepointapp.SupportClasses.ApiClasses;

import com.djaphar.coffeepointapp.SupportClasses.LocalDataClasses.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface PointsApi {

    @POST("api/supervisors/auth")
    Call<SecondaryCredentials> testGetCode(@Body FirstCredentials testCredentials);

    @POST("api/supervisors/codecheck")
    Call<User> login(@Body SecondaryCredentials secondaryCredentials);

    @GET("api/supervisors/{id}")
    Call<User> requestUser(@Path("id") String id);
}
