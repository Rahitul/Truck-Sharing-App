package com.example.projectmove.api;

import static com.example.projectmove.Utilis.Class.Constants.BASE_URL;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiUtilities {
    private static Retrofit retrofit=null;

    public static apiInterface getClient(){
        if(retrofit == null){
            retrofit=new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(apiInterface.class);
    }
}
