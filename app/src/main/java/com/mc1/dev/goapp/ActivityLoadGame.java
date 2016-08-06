package com.mc1.dev.goapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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
    private RunningGame rgs[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN );

        // TODO for every saved game, generate a board view
        // get the bitmap of it and place it in the list
        // use a list view
        setContentView(R.layout.activity_activity_load_game);

        // background image
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.loadGameBoardBackground);
        if (layout != null) {
            layout.setBackgroundResource(R.drawable.dull_boardbackground);
        }

        TextView title = (TextView) findViewById(R.id.loadGameTitleView);
        if (title != null) {
            title.setText(R.string.load_game);
        }


        // TODO henrik:
        SGFParser parser = new SGFParser();
        ArrayList<String> metaInfo = new ArrayList<>();
        ArrayList<String> date = new ArrayList<>();
        final ListView listView = (ListView) findViewById(R.id.list_view);

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File directory = new File(Environment.getExternalStorageDirectory() + "/SGF_files/");
            if (directory.exists()) {
                File files[] = directory.listFiles();
                Bitmap images[] = new Bitmap[files.length];
                this.rgs = new RunningGame[files.length];
                for (int i = 0; i < files.length; i++) {
                    try {
                        InputStream is = new FileInputStream(files[i]);

                        rgs[i] = parser.parse(is);
                        metaInfo.add(rgs[i].getGameMetaInformation().getBlackName()
                                + " (" + rgs[i].getGameMetaInformation().getBlackRank() + ")"
                                + " vs. " + rgs[i].getGameMetaInformation().getWhiteName()
                                + " (" + rgs[i].getGameMetaInformation().getWhiteRank() + ")");
                        for (int j = 0; j < rgs[i].getGameMetaInformation().getDates().length; j++) {
                            if (j == 0) {
                                date.add(rgs[i].getGameMetaInformation().getDates()[j]);
                            } else {
                                date.add(" ; " + rgs[i].getGameMetaInformation().getDates()[j]);
                            }
                        }

                        BoardView board = new BoardView(this, null);
                        board.setBoardSize(rgs[i].getGameMetaInformation().getBoardSize());
                        rgs[i].updateMainTreeIndices();
                        board.refresh(rgs[i].getMainTreeIndices(), rgs[i]);
                        images[i] = board.getBitmap();
                        if (images[i] == null) Log.i(LOG_TAG, "IMAGE RESOURCE IS NULL");
                        Log.i(LOG_TAG, files[i].getAbsolutePath());
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
                    Intent intent = new Intent(getApplicationContext(), ActivityRecordGame.class);
                    intent.putExtra("game", rgs[position]);
                    startActivity(intent);
                }
            });
        }

        //  foreach sgf-file in storage:
        //      ActivityNewGame game = parser.parse(file);
        //        BoardView board = new BoardView(this, null);
        //        board.refresh(game.getMainTreeIndices(), game);
        //        board.invalidate(); // calls on draw
        //        Bitmap image = board.getBitmap();
   }

    private class GameListArrayAdapter extends ArrayAdapter<String> {
        private final Context context;
        private final ArrayList<String> values1stLine;
        private final ArrayList<String> values2ndLine;
        private final Bitmap[] images;

        public GameListArrayAdapter(Context context, ArrayList<String> values1stLine, ArrayList<String> values2ndLine, Bitmap[] img) {
            super(context, R.layout.listview_layout, values1stLine);
            this.context = context;
            this.values1stLine = values1stLine;
            this.values2ndLine = values2ndLine;
            this.images = img;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.listview_layout, parent, false);
            TextView textView = (TextView) rowView.findViewById(R.id.firstLine);
            TextView textView2 = (TextView) rowView.findViewById(R.id.secondLine);
            ImageView imageView = (ImageView) rowView.findViewById(R.id.gameImage);
            textView.setText(values1stLine.get(position));
            textView2.setText(values2ndLine.get(position));

            imageView.setImageBitmap(images[position]);

            return rowView;
        }
    }
}
