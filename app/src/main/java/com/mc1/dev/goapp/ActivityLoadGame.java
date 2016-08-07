package com.mc1.dev.goapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class ActivityLoadGame extends AppCompatActivity {
    private static final String LOG_TAG = ActivityLoadGame.class.getSimpleName();
    private static final int PERMISSION_REQUEST_READ_EXTERNAL_STORAGE = 83;

    private RunningGame rgs[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Disable the default title as a customized one is used
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN );

        // TODO for every saved game, generate a board view
        // get the bitmap of it and place it in the list
        setContentView(R.layout.activity_load_game);

        // background image
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.loadGameBoardBackground);
        if (layout != null) {
            layout.setBackgroundResource(R.drawable.dull_boardbackground);
        }

        // Customized title for the activity
        TextView title = (TextView) findViewById(R.id.loadGameTitleView);
        if (title != null) {
            title.setText(R.string.load_game);
        }

        // the permission to read the external storage public directory needs to be present to
        // populate the listview.
        String perm = "android.permission.READ_EXTERNAL_STORAGE";
        int res = getApplicationContext().checkCallingOrSelfPermission(perm);
        if (res != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_READ_EXTERNAL_STORAGE);
            Log.i(LOG_TAG, "Permission for writing to external storage requested");
        } else {
            SGFParser parser = new SGFParser();
            ArrayList<String> metaInfo = new ArrayList<>();
            ArrayList<String> date = new ArrayList<>();
            final ListView listView = (ListView) findViewById(R.id.list_view);

            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                File directory = new File(Environment.getExternalStorageDirectory() + "/SGF_files/");
                if (directory.exists()) {
                    File files[] = directory.listFiles();
                    // images[] is used for a future feature
                    // see doc.pdf in doc directory
                    Bitmap images[] = new Bitmap[files.length];
                    this.rgs = new RunningGame[files.length];
                    for (int i = 0; i < files.length; i++) {
                        try {
                            InputStream is = new FileInputStream(files[i]);

                            rgs[i] = parser.parse(is);
                            metaInfo.add(rgs[i].getGameMetaInformation().getBlackName()
                                    + " vs. " + rgs[i].getGameMetaInformation().getWhiteName());
                            // iterate over the dates on which the current game is played.
                            // the dates are displayed colon separated in the first row of the list view
                            for (int j = 0; j < rgs[i].getGameMetaInformation().getDates().length; j++) {
                                if (j == 0) {
                                    date.add(rgs[i].getGameMetaInformation().getDates()[j]);
                                } else {
                                    date.add(" ; " + rgs[i].getGameMetaInformation().getDates()[j]);
                                }
                            }

                            // the following lines are needed for displaying images of the final position
                            // of the games stored. getBitmap() currently returns null. Therefore no
                            // images are shown.
                            BoardView board = new BoardView(this, null);
                            board.setBoardSize(rgs[i].getGameMetaInformation().getBoardSize());
                            rgs[i].updateMainTreeIndices();
                            board.refresh(rgs[i].getMainTreeIndices(), rgs[i]);
                            images[i] = board.getBitmap();
                        } catch (FileNotFoundException e) {
                            Log.i(LOG_TAG, "File not found. " + e.getMessage());
                        } catch (IOException ee) {
                            Log.i(LOG_TAG, "Parsing failed. " + ee.getMessage());
                        }
                    }
                    GameListArrayAdapter adapter = new GameListArrayAdapter(this, date, metaInfo, images);

                    if (listView != null) {
                        listView.setAdapter(adapter);
                    }

                }
            }
            if (listView != null) {
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // if an item in the list is clicked the corresponding game is opened in
                        // ActivityRecordGame
                        Intent intent = new Intent(getApplicationContext(), ActivityRecordGame.class);
                        intent.putExtra("game", rgs[position]);
                        startActivity(intent);
                    }
                });
            }
        }
   }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_READ_EXTERNAL_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // if the permission has been granted restart the activity
                    Intent intent = new Intent(this, this.getClass());
                    startActivity(intent);
                }
            }
        }
    }
}
