package com.example.luish000.chatexample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.layer.sdk.LayerClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private LayerController layerController;
    private LayerClient layerClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initLayerClient();
    }

    private void initLayerClient() {
        String layerAppId = getResources().getString(R.string.layer_app_id);
        this.layerClient = LayerClient.newInstance(getApplicationContext(), layerAppId);
        this.layerController = new LayerController(getApplicationContext());
        this.layerClient.registerAuthenticationListener(this.layerController);
        this.layerClient.registerConnectionListener(this.layerController);
        if (!layerClient.isConnected()) {
            layerClient.connect();
        }
    }

}
