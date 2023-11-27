package com.example.projectmove.api;

import static com.example.projectmove.Utilis.Class.Constants.CONTENT_TYPE;
import static com.example.projectmove.Utilis.Class.Constants.SERVER_KEY;

import com.example.projectmove.Model.PushNotification;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface apiInterface {
    @Headers({"Authorization: key="+SERVER_KEY,"Content-Type:" +CONTENT_TYPE})
    @POST("fcm/send")
    Call<PushNotification> sendNotification(@Body PushNotification notification);

}
