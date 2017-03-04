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
        JSONObject jsonObject = new JSONObject(remoteMessage.getData());
        Log.d(LOG_TAG, "RemoteMsg: " + jsonObject.toString());

        if (jsonObject != null) {
            try {
                if (jsonObject.get("type").equals("match_timeout")) {
                    int id = 1;

                    Intent noGame;
                    TaskStackBuilder stackBuilder = TaskStackBuilder
                            .create(this.getApplicationContext());

                    noGame = new Intent(this, ActivityMain.class);
                    stackBuilder.addParentStack(ActivityMain.class);
                    stackBuilder.addNextIntent(noGame);

                    PendingIntent openGamePendingIntent =
                            stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

                    NotificationCompat.Builder playNotification =
                            new NotificationCompat.Builder(this)
                                    .setContentTitle("GoApp")
                                    .setContentText("Sorry! We couldn't find a partner. " +
                                            "\nPlease try again later.")
                                    .setSmallIcon(R.drawable.white_stone)
                                    .setAutoCancel(true)
                                    .setContentIntent(openGamePendingIntent);

                    NotificationManager notificationManager =
                            (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.notify(id, playNotification.build());
                }

                if (jsonObject.get("type").equals("play_timeout")) {
                    // TODO notify user that he won
                }

                if (jsonObject.get("type").equals("move_played")) {
                    int id = 1;

                    Intent openGame;
                    TaskStackBuilder stackBuilder = TaskStackBuilder
                            .create(this.getApplicationContext());

                    openGame = new Intent(this, ActivityPlayOnline.class);
                    stackBuilder.addParentStack(ActivityPlayOnline.class);

                    openGame.putExtra("type", "matched");

                    stackBuilder.addNextIntent(openGame);

                    PendingIntent openGamePendingIntent =
                            stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

                    NotificationCompat.Builder playNotification =
                            new NotificationCompat.Builder(this)
                                    .setContentTitle("GoApp")
                                    .setContentText("It is your turn!")
                                    .setSmallIcon(R.drawable.white_stone)
                                    .setAutoCancel(true)
                                    .setContentIntent(openGamePendingIntent);

                    NotificationManager notificationManager =
                            (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.notify(id, playNotification.build());
                }


                if (jsonObject.get("type").equals("matched")) {
                    int id = 1;

                    // it's important to keep both starting and opponentIsBlack, for the case that
                    // handicap is implemented
                    boolean starting = jsonObject.get("start").equals("true");
                    Log.d(LOG_TAG, "starting: " + starting);
                    boolean opponentIsBlack = jsonObject.has("blackName");
                    Log.d(LOG_TAG, "opponent is black? : " + opponentIsBlack);
                    String opponentName;
                    String opponentRank;
                    if (opponentIsBlack) {
                        opponentName = jsonObject.getString("blackName");
                        opponentRank = jsonObject.getString("blackRank");
                    } else {
                        opponentName = jsonObject.getString("whiteName");
                        opponentRank = jsonObject.getString("whiteRank");
                    }
                    Intent openGame;
                    TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
                    Log.d(LOG_TAG, "OppName: " + opponentName + " OppRank: " + opponentRank);

                    openGame = new Intent(this, ActivityPlayOnline.class);
                    stackBuilder.addParentStack(ActivityPlayOnline.class);

                    openGame.putExtra("type", "matched")
                            .putExtra("opponentIsBlack", opponentIsBlack)
                            .putExtra("opponentName", opponentName)
                            .putExtra("opponentRank", opponentRank);
                    stackBuilder.addNextIntent(openGame);

                    PendingIntent openGamePendingIntent =
                            stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

                    NotificationCompat.Builder matchedNotification =
                            new NotificationCompat.Builder(this)
                                    .setContentTitle("GoApp")
                                    .setContentText("We found a playing partner for you!")
                                    .setSmallIcon(R.drawable.white_stone)
                                    .setAutoCancel(true)
                                    .setContentIntent(openGamePendingIntent);

                    NotificationManager notificationManager =
                            (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.notify(id, matchedNotification.build());
                }
            } catch (JSONException je) {
                Log.e(LOG_TAG, "Could not parse JSON String: " + je.getMessage());
            }
        } else {
            Log.e(LOG_TAG, "JSON String in remote message could not be read");
        }
    }
}
