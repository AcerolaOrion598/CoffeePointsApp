package com.djaphar.coffeepointapp.SupportClasses.ApiClasses;

import com.djaphar.coffeepointapp.SupportClasses.LocalDataClasses.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface PointsApi {

    @POST("api/supervisors/auth")
    Call<SecondaryCredentials> testGetCode(@Body FirstCredentials testCredentials);

    @POST("api/supervisors/codecheck")
    Call<User> testLogIn(@Body SecondaryCredentials secondaryCredentials);
}
