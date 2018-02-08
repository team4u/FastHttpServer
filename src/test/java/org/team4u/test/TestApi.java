package org.team4u.test;

import cn.hutool.core.lang.Dict;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

/**
 * @author Jay Wu
 */
public interface TestApi {

    @Headers("y: y")
    @GET("test/j")
    Call<Dict> json(@Query("x") String x);

    @GET("test/model")
    Call<String> model(@Query("name") String name, @Query("numbers[0]") String number0);

    @GET("test/session/1")
    Call<String> session();

    @GET("test/rest/{x}/{y}")
    Call<String> rest(@Path("x") String x, @Path("y") String y);

    @POST("test/body")
    Call<List<TestController.User>> body(@Body List<TestController.User> users);

    @GET("test/error")
    Call<Void> error();

    @GET("test/download")
    Call<String> download();

    @POST("test/upload")
    Call<Dict> upload(@Body MultipartBody multipartBody);

    @Multipart
    @POST("test/upload")
    Call<Dict> upload(@Part MultipartBody.Part body0, @Part MultipartBody.Part body1);

    @FormUrlEncoded
    @POST("test/jet")
    Call<String> jet(@Field("name") String name);

    @GET("static/index.html")
    Call<String> html();
}