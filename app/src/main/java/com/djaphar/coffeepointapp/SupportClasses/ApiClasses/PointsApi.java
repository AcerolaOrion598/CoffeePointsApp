package com.djaphar.coffeepointapp.SupportClasses.ApiClasses;

import com.djaphar.coffeepointapp.SupportClasses.LocalDataClasses.Product;
import com.djaphar.coffeepointapp.SupportClasses.LocalDataClasses.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PointsApi {

    @POST("api/supervisors/auth")
    Call<SecondCredentials> getCode(@Body FirstCredentials credentials);

    @POST("api/supervisors/codecheck")
    Call<User> login(@Body SecondCredentials credentials);

    @GET("api/supervisors/me")
    Call<User> requestUser(@HeaderMap Map<String, String> headers);

    @PUT("api/supervisors/me")
    Call<User> updateUser(@HeaderMap Map<String, String> headers, @Body User user);

    @GET("api/supervisors/me/products")
    Call<List<Product>> requestUserProducts(@HeaderMap Map<String, String> headers);

    @POST("api/products")
    Call<Product> requestAddProduct(@HeaderMap Map<String, String> headers, @Body Product product);

    @DELETE("api/products/{id}")
    Call<Product> requestDeleteProduct(@Path("id") String id, @HeaderMap Map<String, String> headers);

    @POST("api/couriers/addsupervisor")
    Call<Void> requestBindCourier(@HeaderMap Map<String, String> headers, @Body BindCourierModel bindCourierModel);

    @GET("api/couriers/my")
    Call<ArrayList<Point>> requestMyPoints(@HeaderMap Map<String, String> headers);

    @PUT("api/couriers/{id}")
    Call<Void> requestUpdatePoint(@Path("id") String id, @HeaderMap Map<String, String> headers, @Body PointUpdateModel pointUpdateModel);

    @GET("api/couriers/{id}")
    Call<Point> requestSinglePoint(@Path("id") String id, @HeaderMap Map<String, String> headers);

    @POST("api/couriers/removesupervisor")
    Call<Void> requestDeletePoint(@HeaderMap Map<String, String> headers, @Body PointDeleteModel pointDeleteModel);

    @GET("api/couriers/all")
    Call<ArrayList<Point>> requestPointsInBox(@Query("box") String box);

    @GET("api/supervisors/{id}")
    Call<SupervisorModel> requestSupervisor(@Path("id") String id);
}
