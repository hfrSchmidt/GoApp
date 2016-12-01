package com.mc1.dev.goapp;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

// ----------------------------------------------------------------------
// class FirebaseTokenService
// author Hendrik Schmidt
//
// this class contains functionality to handle Firebase tokens
// ----------------------------------------------------------------------

public class FirebaseTokenService extends FirebaseInstanceIdService {

    private static final String LOG_TAG = "FirebaseTokenService";

    @Override
    public void onTokenRefresh() {

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        Log.e(LOG_TAG, refreshedToken);

        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String token) {
        // TODO implement
    }
}
