package com.mc1.dev.goapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

public class ActivityLoadGame extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN );

        // TODO for every saved game, generate a board view
        // get the bitmap of it and place it in the list
        // use a list view
        setContentView(R.layout.activity_activity_load_game);

        // TODO henrik:
        //  SGFParser parser = new SGFParser();
        //  foreach sgf-file in storage:
        //      NewGame game = parser.parse(file);
        //        BoardView board = new BoardView(this, null);
        //        board.refresh(game.getMainTreeIndices(), game);
        //        board.invalidate(); // calls on draw
        //        Bitmap image = board.getBitmap();
   }

}
