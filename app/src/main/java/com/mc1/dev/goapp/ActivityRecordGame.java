package com.mc1.dev.goapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;


public class ActivityRecordGame extends AppCompatActivity {

    private ArrayList<Integer> indices;
    private RunningGame game;
    private BoardView board;
    private AlertDialog.Builder dialogBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN );

        setContentView(R.layout.activity_activity_record_game);


        // TODO check if a game is given in the input parameters
        // could be, that a saved game is opened
        Intent intent = getIntent();
        game = (RunningGame) intent.getSerializableExtra("game");
        indices = new ArrayList<>();

        board = (BoardView) findViewById(R.id.recordBoardView);
        board.setBoardSize(game.getGameMetaInformation().getBoardSize());

        dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });
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
                    if (pointDistance(x, y, points[counter], points[counter + 1]) <= lineOffset / 2) {
                        int position[] = {i, j}; // the index-position for the stone to be set
                        switch (GameController.getInstance().checkAction(GameMetaInformation.actionType.MOVE, game, position, !game.getCurrentNode().isBlacksMove())) {
                            case OCCUPIED:
                                return super.onTouchEvent(event);
                            case SUICIDE:
                                dialogBuilder.setMessage(R.string.dialog_suicide_content).setTitle(R.string.dialog_suicide_title);
                                dialogBuilder.show();
                                return super.onTouchEvent(event);
                        }

                        // play the move with all attributes
                        indices.add(game.recordMove(GameMetaInformation.actionType.MOVE, position, indices));

                        // remove all prisoners from the board
                        // ! currentNode now has the color of the move played, e.g. a black stone was set, check if
                        // there are prisoners on white side
                        GameController.getInstance().calcPrisoners(game, game.getCurrentNode().isBlacksMove());
                        updatePrisonerViews();

                        board.refresh(indices, game);
                        return super.onTouchEvent(event);
                    } else {
                        counter = counter + 2;
                    }
                }
            }
            return super.onTouchEvent(event);
        }
        // TODO if zoom
        return super.onTouchEvent(event);
    }

    private void updatePrisonerViews() {

        TextView blackPrisonerView = (TextView) findViewById(R.id.blackPrisonersView);
        TextView whitePrisonerView = (TextView) findViewById(R.id.whitePrisonersView);

        if (blackPrisonerView != null && whitePrisonerView != null) {
            String labelBlack = getResources().getString(R.string.label_prisoners_black);
            String labelWhite = getResources().getString(R.string.label_prisoners_white);
            String blackContent = labelBlack + "\r\n" + game.getGameMetaInformation().getBlackPrisoners();
            String whiteContent = labelWhite + "\r\n" + game.getGameMetaInformation().getWhitePrisoners();

            blackPrisonerView.setText(blackContent);
            whitePrisonerView.setText(whiteContent);
        }
    }

    // ----------------------------------------------------------------------
    // function passMove()
    //
    // is called when the save-Button is clicked
    //
    // saves the current game status to a sgf file
    // ----------------------------------------------------------------------
    public void save(View view) {
        dialogBuilder.setMessage(R.string.dialog_suicide_content).setTitle(R.string.dialog_suicide_title);
        dialogBuilder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                Intent intent = new Intent(getApplication(), ActivityMain.class);
                startActivity(intent);
            }
        });
        dialogBuilder.show();

    }

    // ----------------------------------------------------------------------
    // creates a new move with stone positions outside the board and the
    // action type set to "pass"
    // ----------------------------------------------------------------------
    public void passMove(View view) {

        int[] position = {board.getBoardSize(), board.getBoardSize()};
        byte perLeft;

        if (game.getCurrentNode().isBlacksMove()) {
            perLeft = TimeController.getInstance().getBlackPeriodsLeft();
        } else {
            perLeft = TimeController.getInstance().getWhitePeriodsLeft();
        }


        // play the move with all attributes
        game.playMove(GameMetaInformation.actionType.PASS, position, /*TimeController.getInstance().swapTimePeriods(game.getCurrentNode().isBlacksMove()) */ 1, perLeft);

        board.refresh(game.getMainTreeIndices(), game);
    }

    private float pointDistance(float x1, float y1, float x2, float y2) {
        return (float) Math.sqrt(Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2));
    }
}
