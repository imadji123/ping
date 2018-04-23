package com.imadji.ping;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    public static final String ACTION_FIREBASE_TOKEN_REFRESH = "com.imadji.ping.action.ACTION_FCM_TOKEN_REFRESH";
    public static final String EXTRA_MESSAGE = "com.imadji.ping.extras.EXTRA_MESSAGE";
    public static final String PREF_FIREBASE_TOKEN = "PREF_FIREBASE_TOKEN";

    private TextView textToken;
    private TextView textMessage;

    private BroadcastReceiver broadcastReceiver;
    private boolean isReceiverRegistered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textToken = findViewById(R.id.text_token);
        textMessage = findViewById(R.id.text_message);

        textToken.setText(getFirebaseToken());

        setupReceiver();
        registerReceiver();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver();

        if (getIntent().hasExtra(EXTRA_MESSAGE)) {
            textMessage.setText(getIntent().getStringExtra(EXTRA_MESSAGE));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver();
    }

    private void setupReceiver() {
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(ACTION_FIREBASE_TOKEN_REFRESH)) {
                    textToken.setText(getFirebaseToken());
                }
            }
        };
    }

    private void registerReceiver() {
        if (!isReceiverRegistered) {
            LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(ACTION_FIREBASE_TOKEN_REFRESH);
            localBroadcastManager.registerReceiver(broadcastReceiver, intentFilter);
            isReceiverRegistered = true;
        }
    }

    private void unregisterReceiver() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
        isReceiverRegistered = false;
    }

    private String getFirebaseToken() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String token = sharedPreferences.getString(PREF_FIREBASE_TOKEN, "");
        Log.d(TAG, "Token: " + token);
        return token;
    }

}
