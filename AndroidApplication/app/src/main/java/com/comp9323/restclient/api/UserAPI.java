package com.comp9323.restclient.api;

import com.comp9323.data.beans.User;

import java.util.Vector;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by thomas on 10/9/2017.
 */

public interface UserAPI {
    @GET("/api/mobile_users/{id}/")
    Call<User> getUser(@Path("id") int id);

    @GET("/api/mobile_users/all/")
    Call<Vector<User>> getUsers();

    @POST("/api/mobile_users/")
    Call<User> postUser(@Body User user);

    @DELETE("/api/mobile_users/{id}/")
    Call<Response<Void>> deleteUser(@Path("id") int id);

    @PUT("/api/mobile_users/{id}/")
    Call<User> putUser(@Path("id") int id, @Body User user);

    @PATCH("/api/mobile_users/{id}/")
    Call<User> patchUser(@Path("id") int id, @Body User user);
}