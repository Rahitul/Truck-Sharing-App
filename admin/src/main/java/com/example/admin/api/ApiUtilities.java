package com.example.admin.api;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiUtilities {
    private static Retrofit retrofit=null;

    public static apiInterface getClient(){
        if(retrofit == null){
            retrofit=new Retrofit.Builder()
                    .baseUrl("https://fcm.googleapis.com")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(apiInterface.class);
    }
}
