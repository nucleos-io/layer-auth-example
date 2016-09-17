package com.example.luish000.chatexample;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by luish000 on 9/16/16.
 */
public interface LayerIdentityResources {

    String ENDPOINT = "http://persona-mono.herokuapp.com";

    @POST("/layer-sessions")
    Call<Identity> getIdentityToken(@Header("Authorization") String authorization, @Body Nonce nonce);



}
