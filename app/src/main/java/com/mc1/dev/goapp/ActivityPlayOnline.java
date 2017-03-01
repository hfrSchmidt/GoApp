package com.mc1.dev.goapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ActivityPlayOnline extends AppCompatActivity implements NetworkController.OnMoveResponseListener {
    private static final String LOG_TAG = ActivityPlayOnline.class.getSimpleName();

    private RunningGame game;
    private BoardView board;
    private Button submitMoveButton;
    private AlertDialog.Builder dialogBuilder;
    private NetworkController nc;
    private SGFParser sgfParser = new SGFParser();
    private boolean opponentIsBlack;
    private boolean moveWasPlayed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_play_online);

        nc = new NetworkController(this.getApplicationContext());
        nc.start();
        moveWasPlayed = false;

        final Intent intent = getIntent();

        // if type == move_played : game already exists
        String firebaseMsgType = intent.getExtras().getString("type");

        if (firebaseMsgType != null) {
            switch (firebaseMsgType) {
                case "matched":
                    opponentIsBlack = intent.getExtras().getBoolean("opponentIsBlack");
                    String opponentName = intent.getExtras().getString("opponentName");
                    String opponentRank = intent.getExtras().getString("opponentRank");
                    Log.d(LOG_TAG, "Opp is black: " + opponentIsBlack);

                    SharedPreferences sharedPrefs = getSharedPreferences(
                            getString(R.string.PREFERENCE_KEY), Context.MODE_PRIVATE);

                    String nickname = sharedPrefs.getString("nickname", null);
                    int boardsize = sharedPrefs.getInt("boardSize", -1);
                    Log.i(LOG_TAG, "SharedPrefs Boardsize: " + boardsize);
                    int rank = sharedPrefs.getInt("rank", -1);
                    String rankString;
                    if (rank > 30) rankString = Integer.toString(rank) + "d";
                    else rankString = Integer.toString(rank) + "k";

                    Log.i(LOG_TAG, "SharedPrefs Player: " + nickname + " " + rankString);

                    GameMetaInformation gmi = new GameMetaInformation();
                    if (opponentIsBlack) {
                        gmi.setBlackName(opponentName);
                        gmi.setBlackRank(opponentRank);
                        gmi.setBoardSize(boardsize);
                        gmi.setWhiteName(nickname);
                        gmi.setWhiteRank(rankString);
                    } else {
                        gmi.setWhiteName(opponentName);
                        gmi.setWhiteRank(opponentRank);
                        gmi.setBoardSize(boardsize);
                        gmi.setBlackName(nickname);
                        gmi.setBlackRank(rankString);
                    }
                    game = new RunningGame(gmi);

                    try {
                        sgfParser.save(game, "onlineGame");
                    } catch (IOException ioe) {
                        Log.i(LOG_TAG, "Parsing failed: " + ioe.getMessage());
                    }
                    Log.i(LOG_TAG, "Success!! ");
                    break;

                case "move_played":
                    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                        File savedFile = new File(Environment.getExternalStorageDirectory()
                                + "/SGF_files/onlineGame.sgf");
                        if (savedFile.canWrite()) {
                            try {
                                InputStream is = new FileInputStream(savedFile);
                                game = sgfParser.parse(is);
                            } catch (FileNotFoundException finfe) {
                                Log.i(LOG_TAG, "File not found: " + finfe.getMessage());
                            } catch (IOException ioe) {
                                Log.i(LOG_TAG, "Parsing failed: " + ioe.getMessage());
                            }
                        }
                    } else {
                        Log.e(LOG_TAG, "External storage not mounted.");
                    }
                    nc.getMove(FirebaseInstanceId.getInstance().getToken());
                    break;

                default:
                    Log.e(LOG_TAG, "Firebase message type not recognised");
            }
        }

        board = (BoardView) findViewById(R.id.mainOnlineBoardView);
        board.setBoardSize(game.getGameMetaInformation().getBoardSize());

        TextView timeView = (TextView) findViewById(R.id.playOnlineTimeView);

        dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setCancelable(false);
        dialogBuilder.setPositiveButton("Play again", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent intentPlay = new Intent(getApplicationContext(), ActivityOnlineGame.class);
                startActivity(intentPlay);
            }
        });
        dialogBuilder.setNeutralButton("Review", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intentReview = new Intent(getApplicationContext(), ActivityRecordGame.class);
                intentReview.putExtra("game", game);
                startActivity(intentReview);
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intentCancel = new Intent(getApplicationContext(), ActivityMain.class);
                startActivity(intentCancel);
            }
        });

        // TODO intialise name strings
        // TODO initialise blackIsTurned

        if (timeView != null) {
            // per default the time settings of each game equate to the following:
            // 10 seconds main time + 4 * 5 Seconds (Japanese overtime) or
            // 10 seconds main time + 4 Stones in 5 Seconds (Canadian overtime)
            byte sth = 4;
            //TimeController.getInstance().configure(game.getGameMetaInformation().getTimeMode(), 10000, 5000, sth, 100, timeView, turnedTimeView, getResources().getString(R.string.label_time));
        }

        submitMoveButton = (Button) findViewById(R.id.submitMoveButton);
        if (submitMoveButton != null) {
            submitMoveButton.setEnabled(false);
        }
    }

    public void onResume() {
        super.onResume();
        nc.start();
    }

    public void onPause() {
        super.onPause();
        nc.stop();
    }

    public void onStop() {
        super.onStop();
        nc.stop();
    }

    public void onRestart() {
        super.onRestart();
        nc.start();
    }

    @Override
    public void onMoveResponse(MoveNode mn) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File savedFile = new File(Environment.getExternalStorageDirectory()
                    + "/SGF_files/onlineGame.sgf");
            if (savedFile.canWrite()) {
                try {
                    InputStream is = new FileInputStream(savedFile);

                    game = sgfParser.parse(is);
                    game.addIndexToMainTree(game.getCurrentNode().addChild(mn));
                    sgfParser.save(game, "onlineGame");

                    board.refresh(game.getMainTreeIndices(), game);
                } catch (FileNotFoundException finfe) {
                    Log.i(LOG_TAG, "File not found: " + finfe.getMessage());
                } catch (IOException ioe) {
                    Log.i(LOG_TAG, "Parsing failed: " + ioe.getMessage());
                }
            }
        } else {
            Log.e(LOG_TAG, "External storage not mounted.");
        }
        submitMoveButton.setEnabled(false);
    }

    // ----------------------------------------------------------------------
    // function resign()
    //
    // is called when the resign-Button is pressed
    // ----------------------------------------------------------------------
    public void resign(View view) {
        String content = "";
        String title = "";


        dialogBuilder.setMessage(content).setTitle(title);
        dialogBuilder.show();
    }

    // ----------------------------------------------------------------------
    // function submitMove()
    //
    // is called when the submitMove-Button is pressed. Posts the current
    // move to the server.
    // ----------------------------------------------------------------------
    public void submitMove(View view) {
        String token = FirebaseInstanceId.getInstance().getToken();
        JSONObject toDeliver = new JSONObject();
        try {
            toDeliver.put("moveNode", game.getCurrentNode().toJSON());
            toDeliver.put("prisonerCount", game.getGameMetaInformation().getBlackPrisoners()
                    + game.getGameMetaInformation().getWhitePrisoners());
            Log.d(LOG_TAG, toDeliver.toString());
        } catch (JSONException je) {
            Log.e(LOG_TAG, je.getMessage());
        }
        nc.postMove(token, toDeliver.toString());
        try {
            sgfParser.save(game, "onlineGame");
        } catch (IOException ioe) {
            Log.i(LOG_TAG, "Parsing failed: " + ioe.getMessage());
        }
        submitMoveButton.setEnabled(false);
    }

    // ----------------------------------------------------------------------
    // function passMove()
    //
    // is called when the pass-Button is pressed
    //
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

        // play the move with all attributes except for the timing
        game.playMove(GameMetaInformation.actionType.PASS, position);

        board.refresh(game.getMainTreeIndices(), game);
        submitMoveButton.setEnabled(true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int counter = 0;
            float x = event.getX();
            float y = event.getY();
            float lineOffset = board.getLineOffset();
            float points[] = board.getPoints();

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
                            case END:
                                endGame();
                                return super.onTouchEvent(event);
                        }

                        /* time
                        byte perLeft;
                        if (game.getCurrentNode().isBlacksMove()) {
                            perLeft = TimeController.getInstance().getBlackPeriodsLeft();
                        } else {
                            perLeft = TimeController.getInstance().getWhitePeriodsLeft();
                        }*/

                        // play the move with all attributes
                        //game.playMove(GameMetaInformation.actionType.MOVE, position, TimeController.getInstance().swapTimePeriods(game.getCurrentNode().isBlacksMove()), perLeft);

                        if (moveWasPlayed) {
                            game.takeLastMoveBack();
                        }
                        game.playMove(GameMetaInformation.actionType.MOVE, position);
                        moveWasPlayed = true;
                        Log.d(LOG_TAG, "Root node is black: " + game.getRootNode().isBlacksMove());
                        Log.d(LOG_TAG, "GMI Handicap: " + game.getGameMetaInformation().getHandicap());

                        // remove all prisoners from the board
                        // call twice to check for white and black stones, if they are prisoner
                        GameController.getInstance().calcPrisoners(game, game.getCurrentNode().isBlacksMove());
                        GameController.getInstance().calcPrisoners(game, !game.getCurrentNode().isBlacksMove());
                        updatePrisonerViews();

                        board.refresh(game.getMainTreeIndices(), game);
                        submitMoveButton.setEnabled(true);
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
        TextView prisonerView = (TextView) findViewById(R.id.playOnlinePrisonersView);

        if (prisonerView != null) {
            String label = getResources().getString(R.string.label_prisoners);
            String content = label + "\r\n";
            if (opponentIsBlack) {
                content += game.getGameMetaInformation().getBlackPrisoners();
            } else {
                content += game.getGameMetaInformation().getWhitePrisoners();
            }
            prisonerView.setText(content);
        }
    }

    private void endGame() {

        // int[] points = GameController.calculateGameEnding();
        boolean blackWon = true;

        String content = "";
        String title = "";
        if (blackWon) {
            content = getString(R.string.end_black_1) + " " + game.getGameMetaInformation().getBlackPrisoners() + " " + getString(R.string.end_part_2);
            if (!game.getGameMetaInformation().getBlackName().equals("")) {
                title = game.getGameMetaInformation().getBlackName() + " " + getString(R.string.end_title);
            } else {
                title = getString(R.string.end_title_black);
            }

        } else {
            content = getString(R.string.end_white_1) + " " + game.getGameMetaInformation().getWhitePrisoners() + " " + getString(R.string.end_part_2);
            if (!game.getGameMetaInformation().getWhiteName().equals("")) {
                title = game.getGameMetaInformation().getWhiteName() + " " + getString(R.string.end_title);
            } else {
                title = getString(R.string.end_title_white);
            }
        }

        dialogBuilder.setMessage(content).setTitle(title);
        dialogBuilder.show();
    }

    private float pointDistance(float x1, float y1, float x2, float y2) {
        return (float) Math.sqrt(Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2));
    }

}
