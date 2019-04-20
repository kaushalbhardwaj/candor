package com.indusbit.candorsdk;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class ApiClient {

    private static String BASE_URL;
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (BuildConfig.DEBUG)
            BASE_URL = "https://candorapp.herokuapp.com/api/v1/";
        else
            BASE_URL = "https://candorapp.herokuapp.com/api/v1/";

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
