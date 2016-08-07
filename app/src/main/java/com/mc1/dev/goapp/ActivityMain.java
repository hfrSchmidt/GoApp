package com.mc1.dev.goapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;

public class ActivityMain extends AppCompatActivity {
    private static final String LOG_TAG = ActivityMain.class.getSimpleName();
    private static final int PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE = 730;

    private RunningGame rg = null;


    AlertDialog.Builder dialogBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN );

        setContentView(R.layout.activity_main);

        RelativeLayout layout = (RelativeLayout) findViewById(R.id.mainBoardBackground);
        if (layout != null) {
            layout.setBackgroundResource(R.drawable.dull_boardbackground);
        }


        dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    try {
                        SGFParser sgfParser = new SGFParser();
                        String fileName = "testFile.sgf";
                        sgfParser.save(rg, fileName);
                        Log.i("ActivityMain ", "test file has been saved");
                    } catch (IOException e) {
                        Log.e(LOG_TAG, "Could not create testFile.sgf");
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void startNewGameView(View view) {
        Intent intent = new Intent(this, ActivityNewGame.class);
        intent.putExtra("record", false);
        startActivity(intent);
    }
    public void startRecordView(View view) {
        Intent intent = new Intent(this, ActivityNewGame.class);
        intent.putExtra("record", true);
        startActivity(intent);
    }

    public void startLoadGameView(View view) {
        Intent intent = new Intent(this, ActivityLoadGame.class);
        startActivity(intent);
    }

    public void tutorialDialog(View view) {
        final SpannableString s = new SpannableString(getText(R.string.dialog_tutorial_content));
        Linkify.addLinks(s, Linkify.WEB_URLS);
        final TextView message = new TextView(this);
        message.setText(s);
        message.setMovementMethod(LinkMovementMethod.getInstance());
        message.setPadding(30, 5, 30, 5);
        dialogBuilder.setTitle(R.string.dialog_tutorial_title).setView(message);
        dialogBuilder.show();
    }

}
