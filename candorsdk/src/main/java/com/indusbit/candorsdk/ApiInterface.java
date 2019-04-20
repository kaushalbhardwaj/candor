package com.indusbit.candorsdk;

import retrofit2.Call;
import retrofit2.http.*;

public interface ApiInterface {

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @GET("config")
    Call<Experiments> getExperiments(@Header("Account-Token") String accountToken,
                                     @Header("User-Token") String userToken);

    @FormUrlEncoded
    @POST("activate")
    void activateExperiment(@Header("User-Token") String userToken,
                            @Header("Account-Token") String accountToken);


}
