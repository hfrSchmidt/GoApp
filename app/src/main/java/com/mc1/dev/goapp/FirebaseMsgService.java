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

public class FirebaseMsgService extends FirebaseMessagingService {

    private static final String LOG_TAG = FirebaseMsgService.class.getSimpleName();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d(LOG_TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage.getMessageType().equals("match_timeout")) {
            // TODO notify user that the server could not find a match
        }

        if (remoteMessage.getMessageType().equals("play_timeout")) {
            // TODO notify user that he won
        }

        if (remoteMessage.getMessageType().equals("move_played")) {
            // TODO notify user its his turn
        }

        if (remoteMessage.getMessageType().equals("matched")) {
            int id = 1;

            /* TODO create an activity which pulls the new move from the server start this
             * activity instead of the ActivityPlay. ActivityPlay is called from this activity.
             */
            Intent openGame = new Intent(this, ActivityPlay.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addParentStack(ActivityPlay.class);
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
    }
}
