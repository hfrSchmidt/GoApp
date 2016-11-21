package com.mc1.dev.goapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;

public class ActivityOnlineGame extends AppCompatActivity {

    private Button findOpponentButton;
    private Spinner boardSizeSpinner;
    private Spinner rankLevelSpinner;
    private OptionElementHandler oeHandler;

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

        //rank level
        rankLevelSpinner = (Spinner) findViewById(R.id.rankLevelSpinner);
        if (rankLevelSpinner != null) {
            oeHandler.fillRankLevelSpinner(rankLevelSpinner, this.getApplicationContext());
        }

    }

    public void searchForOpponent(View view) {

    }


}
