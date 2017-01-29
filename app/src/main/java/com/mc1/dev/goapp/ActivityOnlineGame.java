package com.mc1.dev.goapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

public class ActivityOnlineGame extends AppCompatActivity {

    private static final String LOG_TAG = ActivityOnlineGame.class.getSimpleName();
    private static final int INTERNET_PERMISSION = 89;

    private Button findOpponentButton;
    private Spinner boardSizeSpinner;
    private NumberPicker rankPicker;
    private NumberPicker rankLevelPicker;
    private OptionElementHandler oeHandler;
    private NetworkController networkController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_online_game);

        // background image
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.onlineGameBackground);
        if (layout != null) {
            layout.setBackgroundResource(R.drawable.dull_boardbackground);
        }

        findOpponentButton = (Button) findViewById(R.id.searchForOpponentButton);

        // get the instance of the singleton class OptionElementHandler
        oeHandler = OptionElementHandler.getInstance();

        //board size
        boardSizeSpinner = (Spinner) findViewById(R.id.boardSizeSpinner);
        if (boardSizeSpinner != null) {
            oeHandler.fillBoardSizeSpinner(boardSizeSpinner, this.getApplicationContext());
        }

        //rank
        rankPicker = (NumberPicker) findViewById(R.id.rankPicker);

        //rank level
        rankLevelPicker = (NumberPicker) findViewById(R.id.rankLevelPicker);
        if (rankLevelPicker != null) {
            oeHandler.fillRankPickers(rankLevelPicker, rankPicker, this.getApplicationContext());
        }

        networkController = new NetworkController(this.getApplicationContext());
        networkController.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        networkController.stop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        networkController.stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        networkController.start();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        networkController.start();
    }

    public void searchForOpponent(View view) {

        JSONObject object = new JSONObject();

        TextView nickNameView = (TextView) findViewById(R.id.nickName);
        String nickName = "";

        if (nickNameView != null) {
            nickName = nickNameView.getText().toString();
        }

        Spinner boardSizeSpinner = (Spinner) findViewById(R.id.boardSizeSpinner);
        int boardSize = 0;

        if (boardSizeSpinner != null) {
            String boardSizeStr = boardSizeSpinner.getSelectedItem().toString();
            boardSize = Integer.parseInt(boardSizeStr.split(" ")[0]);
        }


        int rank = 0;
        if (rankPicker != null) {
            rank = rankPicker.getValue();
        }

        try {
            object.put("nickname", nickName);
            object.put("boardsize", boardSize);
            object.put("rank", rank);

            String jsonString = object.toString(4);
            String token = FirebaseInstanceId.getInstance().getToken();

            String perm = "android.permission.INTERNET";
            int res = getApplicationContext().checkCallingOrSelfPermission(perm);
            if (res != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.INTERNET},
                        INTERNET_PERMISSION);
                Log.i(LOG_TAG, "Permission for writing to external storage requested");
            } else {
                networkController.postMatch(token, jsonString);

                Log.d(LOG_TAG, "\t" + token);
            }
        } catch (JSONException e) {
            // TODO handle exception
            e.printStackTrace();
        }

        // do stuff until match is found

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case INTERNET_PERMISSION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // if the permission has been granted restart the activity
                    Intent intent = new Intent(this, this.getClass());
                    startActivity(intent);
                }
            }
        }
    }
}