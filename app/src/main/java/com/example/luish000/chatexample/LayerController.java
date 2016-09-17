package com.example.luish000.chatexample;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.layer.sdk.LayerClient;
import com.layer.sdk.exceptions.LayerException;
import com.layer.sdk.listeners.LayerAuthenticationListener;
import com.layer.sdk.listeners.LayerConnectionListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by luish000 on 9/16/16.
 */
public class LayerController implements
        LayerAuthenticationListener, LayerConnectionListener {

    private Context context;
    private String TAG = LayerController.class.getName();
    private Retrofit retrofit;

    public LayerController(Context context) {
        this.context = context;
        initRetrofit();
    }

    private void initRetrofit() {
        Gson gson = new GsonBuilder().create();
        this.retrofit = new Retrofit.Builder()
                .baseUrl(LayerIdentityResources.ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    // Connection Listeners

    @Override
    public void onConnectionConnected(LayerClient layerClient) {
        Log.i(TAG, "The layer client is now connected to the layer server");
        layerClient.authenticate();
    }

    @Override
    public void onConnectionDisconnected(LayerClient layerClient) {

    }

    @Override
    public void onConnectionError(LayerClient layerClient, LayerException e) {

    }

    // Authentication Listeners


    @Override
    public void onAuthenticationChallenge(final LayerClient layerClient, String nonce) {
        Log.i(TAG, "The authentication challenge has been init with the nonce: " + nonce);
        LayerIdentityResources resources = retrofit.create(LayerIdentityResources.class);
        String bearerToken = "Bearer d5f3cc4f616d85ea78fb7d866b98573e";
        Call<Identity> call = resources.getIdentityToken(bearerToken, new Nonce(nonce));
        call.enqueue(new Callback<Identity>() {
            @Override
            public void onResponse(Call<Identity> call, Response<Identity> response) {
                if (response.isSuccessful()) {
                    Identity identityToken = response.body();
                    Log.i(TAG, "An identity token is receive from the spark server: " + identityToken);
                    layerClient.answerAuthenticationChallenge(identityToken.getIdentityToken());
                } else {
                    Log.e(TAG, "An error ocurred trying to get the identity token from the spark server: " + response.code());
                    Log.e(TAG, "sended headers " + call.request().headers());
                }
            }
            @Override
            public void onFailure(Call<Identity> call, Throwable t) {
                Log.e(TAG, "An error ocurred trying to get the identity token from the spark");
            }
        });
    }

    @Override
    public void onAuthenticated(LayerClient layerClient, String s) {
        Log.i(TAG, "Layer user authenticated :)");
    }

    @Override
    public void onAuthenticationError(LayerClient layerClient, LayerException e) {

    }

    @Override
    public void onDeauthenticated(LayerClient layerClient) {

    }

}
