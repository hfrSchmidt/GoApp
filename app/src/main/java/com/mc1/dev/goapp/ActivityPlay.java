package com.mc1.dev.goapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;


public class ActivityPlay extends AppCompatActivity {

    private RunningGame game;
    private BoardView board;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN );

        setContentView(R.layout.activity_play);

        Intent intent = getIntent();
        game = (RunningGame) intent.getSerializableExtra("game");

        board = (BoardView) findViewById(R.id.mainBoardView);
        board.setBoardSize(game.getGameMetaInformation().getBoardSize());

        TextView turnedTimeView = (TextView) findViewById(R.id.playTimeViewTurned);
        TextView timeView = (TextView) findViewById(R.id.labelTimeMode);

        // TODO listeners for pass/resign buttons
        //      passbutton::    game.playMove(GameMetaInformation.PASS)
        //      resignbutton::  game.end

        if (turnedTimeView != null && timeView != null) {
            byte sth = 4;
            TimeController.getInstance().configure(game.getGameMetaInformation().getTimeMode(), 100000, 500, sth, 4, timeView, turnedTimeView);
        }

    }

    public void onResume() {
        super.onResume();
    }

    public void onPause() {
        super.onPause();
    }

    public void onStop() {
        super.onStop();
    }

    public void onRestart() {
        super.onRestart();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int counter = 0;
            float x = event.getX();
            float y = event.getY();
            float lineOffset = board.getLineOffset();
            float points[] = board.getPoints();

            LinearLayout turnedActionBar = (LinearLayout) findViewById(R.id.actionBarTurned);
            if (turnedActionBar != null) {
                y = y - turnedActionBar.getHeight();
            }

            for (int i = 0; i < board.getBoardSize(); i++) {
                for (int j = 0; j < board.getBoardSize(); j++) {
                    if (pointDistance(x,y, points[counter], points[counter+1]) <= lineOffset/2) {
                        int position[] = {i,j}; // the index-position for the stone to be set
                        // TODO gamecontroller -> check
                        byte perLeft;
                        if (game.getCurrentNode().isBlacksMove()) {
                            perLeft = TimeController.getInstance().getBlackPeriodsLeft();
                        } else {
                            perLeft = TimeController.getInstance().getWhitePeriodsLeft();
                        }
                        game.playMove(GameMetaInformation.actionType.MOVE, position, TimeController.getInstance().swapTimePeriods(game.getCurrentNode().isBlacksMove()), perLeft);

                        board.refresh(game.getMainTreeIndices(), game);
                        return super.onTouchEvent(event);
                    }
                    else {
                        counter = counter + 2;
                    }
                }
            }
            return super.onTouchEvent(event);
        }
        // TODO if zoom
        return super.onTouchEvent(event);
    }

    private float pointDistance(float x1, float y1, float x2, float y2) {
        return (float) Math.sqrt(Math.pow((x1 - x2),2) + Math.pow((y1 - y2),2) );
    }
}
