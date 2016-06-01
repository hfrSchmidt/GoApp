package com.mc1.dev.goapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;


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

    }


}
