package com.imadji.ping;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {
    private static final String TAG = MyFirebaseInstanceIdService.class.getSimpleName();

    @Override
    public void onTokenRefresh() {
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "TOKEN: " + token);

        // TODO: Sends this token to the server

        // After token sent to server
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.edit().putString(MainActivity.PREF_FIREBASE_TOKEN, token).apply();

        Intent intent = new Intent(MainActivity.ACTION_FIREBASE_TOKEN_REFRESH);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

}
