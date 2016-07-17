package com.mc1.dev.goapp;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Date;
import java.util.EmptyStackException;
import java.util.Stack;

// ----------------------------------------------------------------------
// class SGFParser
// This class provides functionality to either convert a .sgf file to
// the internal representation of a game as well as the other way around
// ----------------------------------------------------------------------
public class SGFParser {
    private static final String LOG_TAG = SGFParser.class.getSimpleName();
    // the variable is used to handle multiline properties.
    private boolean isInsidePropertyVal = false;

    public SGFParser() {
    }

    // ----------------------------------------------------------------------
    // function parse(InputStream input)
    //
    // parses the input file and returns a corresponding RunningGame object.
    // In case the file could not be opened or the read process fails an
    // IOException is thrown.
    // ----------------------------------------------------------------------
    public RunningGame parse(InputStream input) throws IOException, InvalidParameterException {
        /*                  Variations:
        * A variation main line of play always starts with a '(' character. However the sgf format
        * is designed in the way, that the actual moves of the variation are stored at the
        * end of the file. Therefore the main variation continues after a '(' has been encountered
        * until a ')' is reached. From there the line of play jumps to the position of the last '('.
        * Therefore a stack has been used as a data structure to represent variations to the main
        * line of play, as the last element of the stack is always the parent node to return to.
        */
        Stack<ArrayList<Integer>> stack = new Stack<>();
        int position[] = new int[2];
        int noOfChildren;

        // the list of indices characterising the respective parent of the currently regarded node
        ArrayList<Integer> parentNode = new ArrayList<>();

        // initialize the BufferedReader with null so that in case of an error it can be checked
        // whether the reader needs to be closed. See finally block.
        BufferedReader br = null;

        GameMetaInformation gmi = new GameMetaInformation();
        RunningGame rg = new RunningGame(gmi);

        try {
            br = new BufferedReader(new InputStreamReader(input));
            String line;

            // in case of multiline properties both propertyValue and propertyId need to be
            // accessible outside of the currently read line.
            StringBuilder propertyValue = new StringBuilder();
            StringBuilder propertyId = new StringBuilder();

            // the BufferedReader reads the input line by line.
            while ((line = br.readLine()) != null) {
                // split the current line in case there are multiple nodes in one line.
                // Theoretically the whole sgf file could be written into a single line.
                String lineSplit[] = line.split(";");

                for (String ls : lineSplit) {

                    android.support.v4.util.ArrayMap<String, String> nodeList = readProperties(ls, propertyValue, propertyId);

                    for (android.support.v4.util.ArrayMap.Entry<String, String> entry : nodeList.entrySet()) {
                        switch (entry.getKey()) {
                            case "B":
                                // convert letter describing the position of a stone to the more
                                // intuitive integer
                                position[0] = (entry.getValue().charAt(0) - 'a');
                                position[1] = (entry.getValue().charAt(1) - 'a');

                                // recordMove returns the index of the newly inserted MoveNode in
                                // relation to its parent, which is also the number of children of
                                // this node
                                noOfChildren = rg.recordMove(GameMetaInformation.actionType.MOVE, position, parentNode);

                                // new parent node is the newly inserted MoveNode
                                parentNode.add(noOfChildren);

                                //Log.i(LOG_TAG, "\tB[" + position[0] + " " + position[1] + "]\t" + parentNode.toString());
                                break;
                            case "W":
                                position[0] = (entry.getValue().charAt(0) - 'a');
                                position[1] = (entry.getValue().charAt(1) - 'a');
                                noOfChildren = rg.recordMove(GameMetaInformation.actionType.MOVE, position, parentNode);

                                parentNode.add(noOfChildren);

                                //Log.i(LOG_TAG, "\tW[" + position[0] + " " + position[1] + "]\t" + parentNode.toString());
                                break;
                            case "BL":
                                try {
                                    float t = Float.parseFloat(entry.getValue());
                                    // convert the time from seconds to milliseconds
                                    // the current node is in this case the defined by the parentNode
                                    // index Array, because the BL property is always evaluated after
                                    // the corresponding B property. From this it follows, that the
                                    // current node has already been set as the parent node.
                                    rg.getSpecificNode(parentNode).setTime((long) (t * 1000.0f));
                                } catch (NumberFormatException e) {
                                    Log.w(LOG_TAG, "Could not parse time value" + e.getMessage());
                                }
                                //Log.i(LOG_TAG, "\t TimeLeft(b): " + rg.getSpecificNode(parentNode).getTime());
                                break;
                            case "WL":
                                try {
                                    float t = Float.parseFloat(entry.getValue());
                                    rg.getSpecificNode(parentNode).setTime((long) (t * 1000.0f));
                                } catch (NumberFormatException e) {
                                    Log.w(LOG_TAG, "Could not parse time value" + e.getMessage());
                                }
                                //Log.i(LOG_TAG, "\t TimeLeft(w): " + rg.getSpecificNode(parentNode).getTime());
                                break;
                            case "OB":
                                try {
                                    byte ot = Byte.parseByte(entry.getValue());
                                    rg.getSpecificNode(parentNode).setOtPeriods(ot);
                                } catch (NumberFormatException e) {
                                    Log.w(LOG_TAG, "Could not parse over time period value" + e.getMessage());
                                }
                                //Log.i(LOG_TAG, "\t OT Periods(b): " + rg.getSpecificNode(parentNode).getOtPeriods());
                                break;
                            case "OW":
                                try {
                                    byte ot = Byte.parseByte(entry.getValue());
                                    rg.getSpecificNode(parentNode).setOtPeriods(ot);
                                } catch (NumberFormatException e) {
                                    Log.w(LOG_TAG, "Could not parse over time period value" + e.getMessage());
                                }
                                //Log.i(LOG_TAG, "\t OT Periods(w): " + rg.getSpecificNode(parentNode).getOtPeriods());
                                break;
                            case "GM":
                                // In the sgf specification a GM value of 1 has been specified for
                                // the game of go. If a different value is found in the file, it
                                // certainly does not contain the desired information.
                                if (!entry.getValue().equals("1"))
                                    throw new InvalidParameterException("Wrong Game Type!");
                                break;
                            case "SZ":
                                try {
                                    rg.getGameMetaInformation().setBoardSize(Integer.parseInt(entry.getValue()));
                                } catch (NumberFormatException e) {
                                    Log.w(LOG_TAG, "Could not parse board size value" + e.getMessage());
                                }
                                break;
                            case "KM":
                                try {
                                    rg.getGameMetaInformation().setKomi(Float.parseFloat(entry.getValue()));
                                } catch (NumberFormatException e) {
                                    Log.w(LOG_TAG, "Could not parse komi value" + e.getMessage());
                                }
                                break;
                            case "RU":
                                break;
                            // TODO store time settings in the gmi?
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
                                try {
                                    Date dates[] = GameMetaInformation.convertStringToDates(entry.getValue());
                                    rg.getGameMetaInformation().setDates(dates);
                                    Log.i(LOG_TAG, "Date: " + rg.getGameMetaInformation().getDates()[0].toString());
                                } catch (Exception e) {
                                    Log.w(LOG_TAG, "Could not parse dates: " + e.getMessage());
                                }
                                break;
                            case "C":
                                rg.getSpecificNode(parentNode).setComment(entry.getValue());
                                Log.i(LOG_TAG, "C: " + rg.getSpecificNode(parentNode).getComment());
                                break;
                            case "RE":
                                // TODO if the game was won by res, set a MoveNode with actionType RESIGN as the last move in the rg
                                rg.getGameMetaInformation().setResult(entry.getValue());
                                Log.i(LOG_TAG, "RES: " + rg.getGameMetaInformation().getResult());
                                break;
                        }
                    }

                    // the following block handles the branching out of variations
                    for (int j = 0; j < ls.length(); ++j) {
                        switch (ls.charAt(j)) {
                            case '(':
                                stack.push(new ArrayList<>(parentNode));
                                //Log.i(LOG_TAG, "Stack Push: " + stack.toString() + "\n");
                                break;
                            case ')':
                                try {
                                    parentNode = stack.pop();
                                    //Log.i(LOG_TAG, "Stack Pop: " + stack.toString() + "\n");
                                } catch (EmptyStackException e) {
                                    Log.e(LOG_TAG, "Something went wrong in the sgf File. ");
                                    e.printStackTrace();
                                }
                                break;
                        }
                    }
                }
            }
            if (BuildConfig.DEBUG && !stack.isEmpty()) {
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

    // ----------------------------------------------------------------------
    // function ArrayMap<String, String> readProperties(String linePart,
    //      StringBuilder propertyVal, StringBuilder propertyId)
    //
    // reads and separates all properties and their values from the given
    // string. Writes the results to an ArrayMap.
    // propertyVal and propertyId are modified in the process.
    // ----------------------------------------------------------------------
    private android.support.v4.util.ArrayMap<String, String> readProperties(String linePart, StringBuilder propertyVal, StringBuilder propertyId) {
        android.support.v4.util.ArrayMap<String, String> res = new android.support.v4.util.ArrayMap<>();

        // if the passed propertyVal is not empty the reason needs to be a newline character inside
        // a game comment. Thus preserving the newline character:
        if (propertyVal.length() != 0) {
            propertyVal.append('\n');
        }

        for (int strlen = 0; strlen < linePart.length(); ++strlen) {

            // if one or two capital letters directly followed by a [ are encountered this must be
            // a property identifier.
            // everything inside the following pair of [] must be the corresponding property value

            if (Character.isUpperCase(linePart.charAt(strlen)) && !isInsidePropertyVal) {
                propertyId.append(linePart.charAt(strlen));
            }

            switch (linePart.charAt(strlen)) {
                case '[':
                    // if the parser is already in a pair of [] every following [ needs to be
                    // conserved in the string.
                    if (isInsidePropertyVal) propertyVal.append(linePart.charAt(strlen));
                    isInsidePropertyVal = true;
                    break;

                case ']':
                    // the current property is only supposed to be written to res if the property
                    // is finished by a ] character not by an escaped (\]) character.
                    if (strlen - 1 >= 0 && linePart.charAt(strlen - 1) != '\\') {
                        res.put(propertyId.toString(), propertyVal.toString());
                        isInsidePropertyVal = false;
                        // reset both StringBuilder for use with the next property
                        propertyId.setLength(0);
                        propertyVal.setLength(0);
                    }
                    break;
            }
            // every character inside the brackets is taken as a property value. Concerning the [
            // character see above case '[' statement.
            if (isInsidePropertyVal && linePart.charAt(strlen) != '[') {
                propertyVal.append(linePart.charAt(strlen));
            }
        }

        return res;
    }
}
