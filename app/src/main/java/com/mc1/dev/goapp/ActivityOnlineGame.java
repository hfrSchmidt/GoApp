package com.mc1.dev.goapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

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

        Spinner rankLevelSpinner = (Spinner) findViewById(R.id.rankLevelSpinner);
        TextView rankView = (TextView) findViewById(R.id.rank);

        int rank = 0;

        if (rankView != null) {
            rank = Integer.parseInt(rankView.getText().toString());
            if (rank <= 0 && rank >= 31) {

            }
        }

        if (rankLevelSpinner != null) {
            String rankLevel = rankLevelSpinner.getSelectedItem().toString();
            if (rankLevel.equals("Dan")) {
                rank = 30 + rank; // TODO
            }
        }

        try {
            object.put("nickname", nickName);
            object.put("boardsize", boardSize);
            object.put("rank", rank);

            String jsonString = object.toString(4);
        } catch (JSONException e) {
            // TODO handle exception
            e.printStackTrace();
        }



    }


}