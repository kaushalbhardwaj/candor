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
    @POST("experiments/{experiment_key}/activate")
    Call<Void> activateExperiment(@Header("Account-Token") String accountToken,
                                  @Header("User-Token") String userToken,
                                  @Path("experiment_key") String experimentKey,
                                  @Field("variant") String variantKey);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @POST("experiments/{experiment_key}/events")
    Call<Void> sendEvents(@Header("Account-Token") String accountToken,
                          @Header("User-Token") String userToken,
                          @Path("experiment_key") String experimentKey,
                          @Body EventBody eventBody);


}
