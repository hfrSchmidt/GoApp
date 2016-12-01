package com.mc1.dev.goapp;

// ----------------------------------------------------------------------
// class FirebaseMsgService
// author Hendrik Schmidt
//
// this class contains the handling of notifications from Firebase
// ----------------------------------------------------------------------

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FirebaseMsgService extends FirebaseMessagingService {

    private static final String LOG_TAG = "FirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d(LOG_TAG, "From: " + remoteMessage.getFrom());

        // TODO handle received notification
    }
}
