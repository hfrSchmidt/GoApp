package com.mc1.dev.goapp;

import java.io.Serializable;
import java.util.ArrayList;

// ----------------------------------------------------------------------
// class GameMetaInformation
// gmi is a data container to store information of a single game
// stored are only option parameters like komi and time mode
// no information on current board state is stored
// ----------------------------------------------------------------------
@SuppressWarnings("serial")
public class GameMetaInformation implements Serializable {

    public static int INVALID_INT = Integer.MAX_VALUE;
    public static float INVALID_FLOAT = Float.MAX_VALUE;
    public static long INVALID_LONG = Long.MAX_VALUE;
    public static byte INVALID_BYTE = Byte.MAX_VALUE;

    private float komi;
    private int handicap;
    private int boardSize;
    private int blackPrisoners;
    private int whitePrisoners;
    private String timeMode;
    private String whiteName;
    private String blackName;
    private String whiteRank;
    private String blackRank;
    private String result;
    private String[] dates;

    // in accordance with MoveNode's actionType
    public enum actionType {
        MOVE,
        PASS,
        RESIGN
    }

    public GameMetaInformation() {
        this.timeMode = null;
        this.whiteName = null;
        this.blackName = null;
        this.whiteRank = null;
        this.blackRank = null;
        this.result = "Void";
        this.boardSize = INVALID_INT;
        this.handicap = INVALID_INT;
        this.komi = INVALID_FLOAT;
        this.dates = new String[0];
    }

    // function converts a .sgf compatible string to an array of java.util.date objects
    public static String[] convertSgfStringToArray(String inputDates) throws Exception {
        ArrayList<String> tmp = new ArrayList<>(0);

        String result[] = new String[inputDates.split(",").length];

        int predecessorIdx = 0;

        for (int i = 0; i < inputDates.split(",").length; ++i) {
            tmp.add(inputDates.split(",")[i]);
            String[] splitByDash = tmp.get(i).split("-");

            if (splitByDash.length > 3 || splitByDash.length <= 0) {
                throw new Exception("Illegal date format encountered in \"convertSgfStringToArray\"!");
            }
            if (splitByDash[0].length() == 4) predecessorIdx = i;
            if ((splitByDash.length == 1 || splitByDash.length == 2 || splitByDash.length == 3)
                    && predecessorIdx == i) {
                result[i] = tmp.get(i);
            } else if (splitByDash.length == 2) {
                result[i] = result[predecessorIdx].split("-")[0] + "-" + tmp.get(i);
                predecessorIdx = i;
            } else if (splitByDash.length == 1) {
                if (result[predecessorIdx].split("-").length == 3) {
                    result[i] = result[predecessorIdx].split("-")[0] + "-";
                    result[i] += result[predecessorIdx].split("-")[1];
                    result[i] += "-" + tmp.get(i);
                    predecessorIdx = i;
                } else {
                    result[i] = result[predecessorIdx].split("-")[0] + "-";
                    result[i] += tmp.get(i);
                    predecessorIdx = i;
                }
            }
        }
        return result;
    }

    private String convertDatesToString(String[] input) {
        String res = "";

        for (int i = 0; i < input.length; i++) {
            if (i == 0) {
                res += input[i];
                if (input.length > 1) res += ",";
            } else {
                if (!input[i].split("-")[0].equals(input[i - 1].split("-")[0])
                        || input[i].split("-").length < input[i - 1].split("-").length) {
                    if (!res.endsWith(",")) res += ",";
                    res += input[i];
                } else if (!getMonth(input[i]).equals(getMonth(input[i - 1]))) {
                    res += "," + getMonth(input[i]);
                    if (!getDay(input[i]).equals("INVALID")) res += "-" + getDay(input[i]);
                } else if (!getDay(input[i]).equals(getDay(input[i - 1]))) {
                    res += "," + getDay(input[i]);
                }
            }
        }

        return res;
    }

    private String getMonth(String input) {
        int noOfElements = input.split("-").length;

        switch (noOfElements) {
            case 1:
                return "INVALID";
            default:
                return input.split("-")[1];
        }
    }

    private String getDay(String input) {
        int noOfElements = input.split("-").length;

        switch (noOfElements) {
            case 3:
                return input.split("-")[2];
            default:
                return "INVALID";
        }
    }

    public String toString() {
        // the type of game for go is always 1
        String res = "GM[1]";

        if (komi != INVALID_FLOAT) {
            res += "KM[" + komi + "]";
        }
        if (boardSize != INVALID_INT) {
            res += "SZ[" + boardSize + "]";
        }
        if (handicap != INVALID_INT) {
            res += "HA[" + handicap + "]";
        }
        if (whiteName != null) {
            res += "PW[" + whiteName + "]";
        }
        if (blackName != null) {
            res += "PB[" + blackName + "]";
        }
        if (whiteRank != null) {
            res += "WR[" + whiteRank + "]";
        }
        if (blackRank != null) {
            res += "BR[" + blackRank + "]";
        }
        if (dates.length != 0) {
            res += "DT[" + convertDatesToString(this.dates) + "]";
        }

        if (result != null) {
            res += "RE[" + result + "]";
        }

        return res;
    }

    public void setDates(String[] inputDates) {
        this.dates = inputDates;
    }

    public String[] getDates() {
        return this.dates;
    }

    public String getResult() {
        return this.result;
    }

    public void setResult(String res) {
        this.result = res;
    }

    public float getKomi() {
        return komi;
    }

    public void setKomi(float komi) {
        this.komi = komi;
    }

    public int getHandicap() {
        return handicap;
    }

    public void setHandicap(int handicap) {
        this.handicap = handicap;
    }

    public int getBoardSize() {
        return boardSize;
    }

    public void setBoardSize(int boardSize) {
        this.boardSize = boardSize;
    }

    public int getBlackPrisoners() {
        return blackPrisoners;
    }

    public void setBlackPrisoners(int prisoners) {
        blackPrisoners = prisoners;
    }

    public int getWhitePrisoners() {
        return whitePrisoners;
    }

    public void setWhitePrisoners(int prisoners) {
        whitePrisoners = prisoners;
    }

    public String getTimeMode() {
        return timeMode;
    }

    public void setTimeMode(String timeMode) {
        this.timeMode = timeMode;
    }

    public String getWhiteName() {
        return whiteName;
    }

    public void setWhiteName(String whiteName) {
        this.whiteName = whiteName;
    }

    public String getBlackName() {
        return blackName;
    }

    public void setBlackName(String blackName) {
        this.blackName = blackName;
    }

    public void setWhiteRank(String whiteRank) {
        this.whiteRank = whiteRank;
    }

    public String getWhiteRank() {
        return this.whiteRank;
    }

    public void setBlackRank(String blackRank) {
        this.blackRank = blackRank;
    }

    public String getBlackRank() {
        return this.blackRank;
    }

    public int getBlackPoints() {
        return 5;
    }

    public void setBlackPoints(int points) {
        return;
    }

    public int getWhitePoints() {
        return 7;
    }

    public void setWhitePoints(int points) {
        return;
    }
 }
