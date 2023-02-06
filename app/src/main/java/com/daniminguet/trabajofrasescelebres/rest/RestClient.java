package com.daniminguet.trabajofrasescelebres.rest;

import com.daniminguet.trabajofrasescelebres.interfaces.IAPIService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestClient {
    private static IAPIService instance;
    private static final int PORT = 8080;
    private static final String BASE_URL = "http://192.168.20.148" + ":" + PORT + "/";

    private RestClient() {

    }

    public static synchronized IAPIService getInstance() {
        if(instance == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            instance = retrofit.create(IAPIService.class);
        }
        return instance;
    }
}
