package com.mc1.dev.goapp;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

// ----------------------------------------------------------------------
// class SGFParser
// This class provides functionality to either convert a .sgf file to
// the internal representation of a game as well as the other way around
// ----------------------------------------------------------------------
public class SGFParser {
    private static final String LOG_TAG = SGFParser.class.getSimpleName();

    private File sgfFile;


    public SGFParser(File sgfFileInput) {
        this.sgfFile = sgfFileInput;
    }

    public RunningGame parse() {
        String content = openFile();


        GameMetaInformation gmi = new GameMetaInformation();
        GameTree gt = new GameTree(null);

        // TODO fill the gmi and gt with the data from the file.

        for (int i = 0; i < content.length(); ++i) {
            switch (content.charAt(i)) {
                case '(':

                case ')':

                case ';':
            }
        }

        return new RunningGame(gmi, gt);
    }

    public String openFile() {
        if (sgfFile.exists()) {
            // StringBuilder is not threadsafe!
            StringBuilder content = new StringBuilder(2000);

            try {
                BufferedReader br = new BufferedReader(new FileReader(sgfFile));
                String line;

                while ((line = br.readLine()) != null) {
                    content.append(line);
                }
                return content.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Could not open File");
            }
        } else {
            Log.w(LOG_TAG, "SGF file does not exist");
        }
        //TODO the null case needs to be caught upwards. Maybe use custom exception instead.
        return null;
    }

}
