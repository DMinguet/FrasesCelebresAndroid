package com.daniminguet.trabajofrasescelebres.rest;

import com.daniminguet.trabajofrasescelebres.interfaces.IAPIService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestClient {
    private static IAPIService instance;
    public static final int PORT = 8080;
    public static final String IP_CASA = "192.168.18.23";
    public static final String IP_INSTI = "192.168.20.148";

    private static final String BASE_URL = "http://" + IP_INSTI + ":" + PORT + "/";

    public RestClient() {

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
