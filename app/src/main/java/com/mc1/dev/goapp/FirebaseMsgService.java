package com.mc1.dev.goapp;

// ----------------------------------------------------------------------
// class FirebaseMsgService
// author Hendrik Schmidt
//
// this class contains the handling of notifications from Firebase
// ----------------------------------------------------------------------

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

public class FirebaseMsgService extends FirebaseMessagingService {

    private static final String LOG_TAG = FirebaseMsgService.class.getSimpleName();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d(LOG_TAG, "From: " + remoteMessage.getFrom());
        Log.d(LOG_TAG, "Content " + remoteMessage.getData().toString());
        Log.d(LOG_TAG, "message type: " + remoteMessage.getMessageType());
        JSONObject jsonObject = null;

        jsonObject = new JSONObject(remoteMessage.getData());

        if (jsonObject != null) {
            try {
                if (jsonObject.get("type").equals("match_timeout")) {
                    // TODO notify user that the server could not find a match
                }

                if (jsonObject.get("type").equals("play_timeout")) {
                    // TODO notify user that he won
                }

                if (jsonObject.get("type").equals("move_played")) {
                    // TODO notify user its his turn
                }


                if (jsonObject.get("type").equals("matched")) {
                    int id = 1;

                    boolean starting = (boolean) jsonObject.get("start");
                    Intent openGame;
                    TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

                    openGame = new Intent(this, ActivityPlay.class);
                    stackBuilder.addParentStack(ActivityPlay.class);

                    if (starting) {
                        openGame = new Intent(this, ActivityPlay.class);
                        stackBuilder.addParentStack(ActivityPlay.class);
                    } else {
                        openGame = new Intent(this, ActivityGetMove.class);
                        stackBuilder.addParentStack(ActivityGetMove.class);
                    }
                    stackBuilder.addNextIntent(openGame);

                    PendingIntent openGamePendingIntent =
                            stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

                    NotificationCompat.Builder matchedNotification =
                            new NotificationCompat.Builder(this)
                                    .setContentTitle("GoApp")
                                    .setContentText("We found a playing partner for you!")
                                    .setContentIntent(openGamePendingIntent);

                    NotificationManager notificationManager =
                            (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.notify(id, matchedNotification.build());
                }
            } catch (JSONException je) {
                Log.i(LOG_TAG, "Could not parse JSON String: " + je.getMessage());
            }
        } else {
            Log.e(LOG_TAG, "JSON String in remote message could not be read");
        }
    }
}
