package com.djaphar.coffeepointapp.SupportClasses.ApiClasses;

import com.djaphar.coffeepointapp.SupportClasses.LocalDataClasses.User;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface PointsApi {

    @POST("api/supervisors/auth")
    Call<SecondCredentials> getCode(@Body FirstCredentials credentials);

    @POST("api/supervisors/codecheck")
    Call<User> login(@Body SecondCredentials credentials);

    @GET("api/supervisors/{id}")
    Call<User> requestUser(@Path("id") String id);

    @PUT("api/supervisors/{id}")
    Call<User> updateUser(@Path("id") String id, @HeaderMap Map<String, String> headers, @Body User user);
}
