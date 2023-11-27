package com.example.admin.api;



import com.example.admin.Model.PushNotification;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface apiInterface {
    @Headers({"Authorization: key="+"AAAAh8LDDMM:APA91bFEYr5iq3fcIY1qo_DxQRjzfXHBnCLUigPGdeotTO4BhsCzc16r2adAVYrppSLDxEnVhtJDh7e0Xensd5VBfTiPp-XJCIUcccXfLNqRYaiDYs08N_85h35s8VKhkpwLoL3aNrvb","Content-Type:" +"application/json"})
    @POST("fcm/send")
    Call<PushNotification> sendNotification(@Body PushNotification notification);

}
