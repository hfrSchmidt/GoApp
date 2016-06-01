package com.mc1.dev.goapp;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.EmptyStackException;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// ----------------------------------------------------------------------
// class SGFParser
// This class provides functionality to either convert a .sgf file to
// the internal representation of a game as well as the other way around
// ----------------------------------------------------------------------
public class SGFParser {
    private static final String LOG_TAG = SGFParser.class.getSimpleName();

    private final Pattern blacksMove = Pattern.compile("B\\[([a-t]{0,2})\\]");
    private final Pattern whitesMove = Pattern.compile("W\\[([a-t]{0,2})\\]");
    private final Pattern blacksTimeLeft = Pattern.compile("BL\\[(\\d+(\\.\\d{1,3})?)\\]");
    private final Pattern whitesTimeLeft = Pattern.compile("WL\\[(\\d+(\\.\\d{1,3})?)\\]");
    private final Pattern blacksMovesLeft = Pattern.compile("OB\\[(\\d+)\\]");
    private final Pattern whitesMovesLeft = Pattern.compile("OW\\[(\\d+)\\]");
    private final Pattern gameIdentifier = Pattern.compile("GM\\[(\\d)\\]");
    private final Pattern boardSize = Pattern.compile("SZ\\[(\\d{1,2})\\]");
    private final Pattern komi = Pattern.compile("KM\\[((-)?\\d+(\\.[50]0)?)\\]");
    private final Pattern mainTime = Pattern.compile("TM\\[(\\d+)\\]");
    private final Pattern overTime = Pattern.compile("OT\\[(\\d+[/x]\\d+\\p{Blank}[a-zA-Z\\p{Punct}]+)\\]");
    private final Pattern playerBlack = Pattern.compile("PB\\[(\\p{Alnum}+)\\]");
    private final Pattern playerWhite = Pattern.compile("PW\\[(\\p{Alnum}+)\\]");
    private final Pattern blackRank = Pattern.compile("BR\\[(\\d{1,2}[kd])\\]");
    private final Pattern whiteRank = Pattern.compile("WR\\[(\\d{1,2}[kd])\\]");
    private final Pattern date = Pattern.compile("DT\\[(\\d{4}(-\\d{2}){2})\\]");
    private final Pattern comment = Pattern.compile("C\\[(\\p{Alnum}+)\\]");
    private final Pattern result = Pattern.compile("RE\\[([WB][\\+-]\\p{Alnum}+)\\]");


    public SGFParser() {
    }

    public RunningGame parse(File sgfFile) {
        String content = readFile(sgfFile);

        GameMetaInformation gmi = new GameMetaInformation();

        RunningGame rg = new RunningGame(gmi);

        // TODO fill the gmi and gt with the data from the file.

        String[] allNodes = content.split(";");
        Stack<Integer> stack = new Stack<>();

        for (int i = 0; i < allNodes.length; ++i) {
            if (allNodes[i].contains("(")) stack.push(i);
            if (allNodes[i].contains(")")) {
                try {
                    stack.pop();
                } catch (EmptyStackException e) {
                    Log.e(LOG_TAG, "Something is wrong with the SGF File");
                }
            }
            readProperties(allNodes[i], rg);
        }
        return rg;
    }

    private void readProperties(String node, RunningGame rg) {

        MoveNode currentMoveNode = rg.getCurrentNode();
        MoveNode newMoveNode;

        String bTurn = getPropertyValue(node, blacksMove);
        if (!bTurn.equals("")) {
            int[] position = {bTurn.charAt(0) - 'a' + 1, bTurn.charAt(1) - 'a' + 1};
            String bTime = getPropertyValue(node, blacksTimeLeft);
            if (!bTime.equals("")) {
                // TODO handle time
                long timeLeft = Math.round(Float.valueOf(bTime));
                int periodsLeft;
                String bTurnsLeft = getPropertyValue(node, blacksMovesLeft);
                if (!bTurnsLeft.equals("")) {
                    periodsLeft = Integer.parseInt(bTurnsLeft);
                } else {
                    periodsLeft = 1;
                }
                // TODO time manager needs to know timeLeft and periodsLeft
                newMoveNode = new MoveNode(1, true, position, currentMoveNode); // TODO parse action type and set as first parameter
            } else {
                newMoveNode = new MoveNode(1, true, position, currentMoveNode);// TODO parse action type and set as first parameter
            }
        }
        else { // TODO is possible for a node to neither contain a black move nor a white move?
        //if (!wTurn.equals("")) { // TODO use this if upper comment is true
             String wTurn = getPropertyValue(node, whitesMove);
            int[] position = {wTurn.charAt(0) - 'a' + 1, wTurn.charAt(1) - 'a' + 1};
            String wTime = getPropertyValue(node, whitesTimeLeft);
            if (!wTime.equals("")) {
                // TODO handle time
                long timeLeft = Math.round(Float.valueOf(wTime));
                int periodsLeft;
                String wTurnsLeft = getPropertyValue(node, whitesMovesLeft);
                if (!wTurnsLeft.equals("")) {
                    periodsLeft = Integer.parseInt(wTurnsLeft);
                } else {
                    periodsLeft = 1;
                }
                // TODO time manager needs to know timeLeft and periodsLeft
                newMoveNode = new MoveNode(1, true, position, currentMoveNode); // TODO parse action type and set as first parameter
            } else {
                newMoveNode = new MoveNode(1, true, position, currentMoveNode); // TODO parse action type and set as first parameter
            }
        }

        rg.addIndexToMainTree(currentMoveNode.addChild(newMoveNode)); // append child to current node and add the index to the main tree structure

        // TODO add the rest of the properties to the GameMetaInformation
    }

    private String getPropertyValue(String node, Pattern pattern) {
        String retval = "";
        if (pattern.matcher(node).matches()) {
            Matcher matcher = pattern.matcher(node);
            matcher.find();
            retval = matcher.group(1);
        }
        return retval;
    }

    public String readFile(File sgfFile) {
        if (sgfFile.exists()) {
            // StringBuilder is not threadsafe!
            // TODO larger files will cause either fatal errors or strange behaviour, which will eventual lead to a fatal error while parsing
            StringBuilder content = new StringBuilder(2000); // TODO fixed, not configureable size, maybe change to buffer

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