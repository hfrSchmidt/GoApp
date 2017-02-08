package com.mc1.dev.goapp;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ActivityGetMove extends AppCompatActivity implements NetworkController.OnMoveResponseListener {
    private static final String LOG_TAG = ActivityGetMove.class.getSimpleName();

    private NetworkController nc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_move);

        nc = new NetworkController(this);

        String token = FirebaseInstanceId.getInstance().getToken();

        nc.getMove(token);
    }

    @Override
    public void onMoveResponse(MoveNode mn) {
        // TODO add moveNode to current RunningGame
        // get Running game from .sgf file
        SGFParser sgfParser = new SGFParser();
        RunningGame rg;

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File savedFile = new File(Environment.getExternalStorageDirectory() + "/SGF_files/");
            if (savedFile.canWrite()) {
                try {
                    InputStream is = new FileInputStream(savedFile);

                    rg = sgfParser.parse(is);
                    rg.addIndexToMainTree(rg.getCurrentNode().addChild(mn));

                } catch (FileNotFoundException finfe) {
                    Log.i(LOG_TAG, "File not found: " + finfe.getMessage());
                } catch (IOException ioe) {
                    Log.i(LOG_TAG, "Parsing failed: " + ioe.getMessage());
                }
            }
        }
    }
}
