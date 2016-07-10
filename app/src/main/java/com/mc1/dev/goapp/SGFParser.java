package com.mc1.dev.goapp;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Stack;
//import java.util.regex.Pattern;

// ----------------------------------------------------------------------
// class SGFParser
// This class provides functionality to either convert a .sgf file to
// the internal representation of a game as well as the other way around
// ----------------------------------------------------------------------
public class SGFParser {
    private static final String LOG_TAG = SGFParser.class.getSimpleName();

    /*
    private final Pattern blacksMove = Pattern.compile("B\\[([a-t]{0,2})\\]");
    private final Pattern whitesMove = Pattern.compile("W\\[([a-t]{0,2})\\]");
    private final Pattern blacksTimeLeft = Pattern.compile("BL\\[(\\d+(\\.\\d{1,3})?)\\]");
    private final Pattern whitesTimeLeft = Pattern.compile("WL\\[(\\d+(\\.\\d{1,3})?)\\]");
    private final Pattern blacksMovesLeft = Pattern.compile("OB\\[(\\d+)\\]");
    private final Pattern whitesMovesLeft = Pattern.compile("OW\\[(\\d+)\\]");
    private final Pattern gameIdentifier = Pattern.compile("GM\\[(\\d)\\]");
    private final Pattern boardSize = Pattern.compile("SZ\\[(\\d{1,2})\\]");
    private final Pattern komi = Pattern.compile("KM\\[((-)?\\d+(\\.[50]0)?)\\]");
    private final Pattern ruleSet = Pattern.compile("RU\\[([Jj]apanese|[Cc]hinese)\\]");
    private final Pattern mainTime = Pattern.compile("TM\\[(\\d+)\\]");
    private final Pattern overTime = Pattern.compile("OT\\[(\\d+[/x]\\d+\\p{Blank}[a-zA-Z\\p{Punct}]+)\\]");
    private final Pattern playerBlack = Pattern.compile("PB\\[(\\p{Alnum}+)\\]");
    private final Pattern playerWhite = Pattern.compile("PW\\[(\\p{Alnum}+)\\]");
    private final Pattern blackRank = Pattern.compile("BR\\[(\\d{1,2}[kd])\\]");
    private final Pattern whiteRank = Pattern.compile("WR\\[(\\d{1,2}[kd])\\]");
    private final Pattern date = Pattern.compile("DT\\[(((,?(\\d{4}(,\\d{4})*-\\d{2}(?!\\d{2})(,\\d{2}(?!\\d{2}))*-\\d{2}(?!\\d{2})(,\\d{2}(?!\\d{2}))*))*))\\]");
    private final Pattern comment = Pattern.compile("C\\[(\\p{Alnum}+)\\]");
    private final Pattern result = Pattern.compile("RE\\[([WB]\\+(Res)?(R)?(Time)?(T)?(Forfeit)?(F)?(\\d+\\.\\d+)?|Void|\\?)\\]");

    private final Pattern[] allPatterns = {blacksMove, whitesMove, blacksTimeLeft, whitesTimeLeft
            , blacksMovesLeft, whitesMovesLeft, gameIdentifier, boardSize, komi, ruleSet, mainTime
            , overTime, playerBlack, playerWhite, blackRank, whiteRank, date, comment, result};
    */
    private int debug = 0;

    public SGFParser() {
    }

    // function parses the input file and returns a corresponding RunningGame object.
    // In case the file could not be opened or the read process fails an IOException
    // is thrown.
    public RunningGame parse(InputStream input) throws IOException, InvalidParameterException {
        Stack<ArrayList<Integer>> stack = new Stack<>();
        ArrayList<Integer> indices = new ArrayList<>();
        stack.push(indices); // push the root node on the stack
        int position[] = new int[2];
        int noOfChildren;

        ArrayList<Integer> parentNode = new ArrayList<>();


        BufferedReader br = null;

        GameMetaInformation gmi = new GameMetaInformation();
        RunningGame rg = new RunningGame(gmi);

        try {
            br = new BufferedReader(new InputStreamReader(input));
            String line;

            StringBuilder multilinePropBuffer = new StringBuilder();
            boolean nodeContinues;

            while ((line = br.readLine()) != null) {
                nodeContinues = !line.contains(";");

                String lineSplit[] = line.split(";");

                //System.out.println("\n\n\n------- New Line -------");
                //System.out.println("\t LSlen " + lineSplit.length);

                for (int i = 0; i < lineSplit.length; ++i) {
                    //System.out.println("lineSplit["+i+"] " + lineSplit[i]);


                    // TODO multiline properties!

                    android.support.v4.util.ArrayMap<String, String> nodeList = readProperties(lineSplit[i], multilinePropBuffer);

                    for (android.support.v4.util.ArrayMap.Entry<String, String> entry : nodeList.entrySet()) {
                        switch (entry.getKey()) {
                            case "B":
                                position[0] = (entry.getValue().charAt(0) - 'a');
                                position[1] = (entry.getValue().charAt(1) - 'a');
                                noOfChildren = rg.recordMove(GameMetaInformation.actionType.MOVE, position, parentNode);
                                Log.i(LOG_TAG, "\t" + noOfChildren);
                                if (noOfChildren == 0) {
                                    indices.add(noOfChildren);
                                    parentNode = indices;
                                } else {
                                    Log.i(LOG_TAG, "HASDASKDJASDASDASFASD");
                                    indices.set(parentNode.size(), noOfChildren);
                                    parentNode = indices;
                                }
                                //Log.i(LOG_TAG, "\tB["+position[0]+" "+position[1]+"]\t" + parentNode.toString());
                                break;
                            case "W":
                                position[0] = (entry.getValue().charAt(0) - 'a');
                                position[1] = (entry.getValue().charAt(1) - 'a');
                                noOfChildren = rg.recordMove(GameMetaInformation.actionType.MOVE, position, parentNode);
                                Log.i(LOG_TAG, "\t" + noOfChildren);
                                if (noOfChildren == 0) {
                                    indices.add(noOfChildren);
                                    parentNode = indices;
                                } else {
                                    indices.set(parentNode.size(), noOfChildren);
                                    parentNode = indices;
                                }
                                //Log.i(LOG_TAG, "\tB["+position[0]+" "+position[1]+"]\t" + parentNode.toString());
                                //System.out.println(entry.getKey() + " " + (int) (entry.getValue().charAt(0) - 'a') + " " + (int) (entry.getValue().charAt(1) - 'a') + " " + stack.toString());
                                break;
                            case "BL":
                                break;
                            case "WL":
                                break;
                            case "OB":
                                break;
                            case "OW":
                                break;
                            case "GM":
                                if (!entry.getValue().equals("1"))
                                    throw new InvalidParameterException("Wrong Game Type!");
                                break;
                            case "SZ":
                                rg.getGameMetaInformation().setBoardSize(Integer.parseInt(entry.getValue()));
                                break;
                            case "KM":
                                rg.getGameMetaInformation().setKomi(Float.parseFloat(entry.getValue()));
                                break;
                            case "RU":
                                //System.out.println(entry.getKey() + " " + entry.getValue());
                                break;
                            case "TM":
                                break;
                            case "OT":
                                break;
                            case "PB":
                                rg.getGameMetaInformation().setBlackName(entry.getValue());
                                break;
                            case "PW":
                                rg.getGameMetaInformation().setWhiteName(entry.getValue());
                                break;
                            case "BR":
                                rg.getGameMetaInformation().setBlackRank(entry.getKey());
                                break;
                            case "WR":
                                rg.getGameMetaInformation().setWhiteRank(entry.getValue());
                                break;
                            case "DT":
                                //System.out.println(entry.getKey() + " " + entry.getValue());
                                break;
                            case "C":
                                //System.out.println(entry.getKey() + " " + entry.getValue());
                                break;
                            case "RE":
                                rg.getGameMetaInformation().setResult(entry.getValue());
                                break;
                        }
                    }

                    for (int j = 0; j < lineSplit[i].length(); ++j) {
                        switch (lineSplit[i].charAt(j)) {
                            case '(':
                                stack.push(parentNode);
                                //System.out.println("Stack " + stack.toString() + "\n");
                                break;
                            case ')':
                                try {
                                    parentNode = stack.pop();
                                    //System.out.println("Stack: " + stack.toString() + "\n");
                                } catch (EmptyStackException e) {
                                    Log.e(LOG_TAG, "Something went wrong in the sgf File. ");
                                    e.printStackTrace();
                                }
                                break;
                        }
                    }
                }
            }
            if (BuildConfig.DEBUG && stack.size() != 1) {
                throw new AssertionError();
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Could not open inputStram. " + e.getMessage());
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    Log.e(LOG_TAG, "Could not close buffered Reader. " + e.getMessage());
                }
            }
        }

        return rg;
    }

    private android.support.v4.util.ArrayMap<String, String> readProperties(String linePart, StringBuilder sbPrev) {
        android.support.v4.util.ArrayMap<String, String> res = new android.support.v4.util.ArrayMap<>();

        // if you encounter one or two capital letters directly followed by a [ this must be a
        // property identifier
        // everything inside the following pair of [] must be the corresponding property value

        StringBuilder propertyId = new StringBuilder();
        StringBuilder propertyVal = new StringBuilder();

        boolean isInsidePropertyVal = false;

        if (sbPrev.length() != 0) {
            isInsidePropertyVal = true;
            propertyVal.append(sbPrev.toString());
        }

        for (int strlen = 0; strlen < linePart.length(); ++strlen) {

            if (Character.isUpperCase(linePart.charAt(strlen)) && !isInsidePropertyVal) {
                propertyId.append(linePart.charAt(strlen));
            }

            switch (linePart.charAt(strlen)) {
                case '[':
                    isInsidePropertyVal = true;
                    break;

                case ']':
                    if (strlen - 1 >= 0 && linePart.charAt(strlen - 1) != '\\') {
                        res.put(propertyId.toString(), propertyVal.toString());
                        isInsidePropertyVal = false;
                        propertyId.setLength(0);
                        propertyVal.setLength(0);
                        sbPrev.setLength(0);
                    }
                    break;
            }
            if (isInsidePropertyVal && linePart.charAt(strlen) != '[') {
                propertyVal.append(linePart.charAt(strlen));
            }
        }

        if (isInsidePropertyVal) sbPrev.append(propertyVal.toString());

        return res;
    }
}
