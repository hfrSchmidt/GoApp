package com.mc1.dev.goapp;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

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
    private Date[] dates;

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
        this.dates = new Date[0];
    }

    // function converts a .sgf compatible string to an array of java.util.date objects
    public static Date[] convertStringToDates(String inputDates) throws Exception {
        ArrayList<String> tmp = new ArrayList<>(0);

        DateFormat fmtYearOnly = new SimpleDateFormat("yyyy", Locale.ENGLISH);
        DateFormat fmtShort = new SimpleDateFormat("yyyy-MM", Locale.ENGLISH);
        DateFormat fmtLong = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

        int predecessorIdx = 0;
        StringBuilder sb = new StringBuilder();
        Date result[] = new Date[inputDates.split(",").length];

        for (int i = 0; i < inputDates.split(",").length; ++i) {
            tmp.add(inputDates.split(",")[i]);
            String[] splitByDash = tmp.get(i).split("-");

            if (splitByDash.length > 3 || splitByDash.length <= 0) {
                throw new Exception("Illegal date format encountered in \"convertStringToDates\"!");
            }
            if (splitByDash[0].length() == 4) predecessorIdx = i;
            if (splitByDash.length == 1 && predecessorIdx == i) {
                result[i] = fmtYearOnly.parse(splitByDash[0]);
            } else if (splitByDash.length == 3) {
                result[i] = fmtLong.parse(tmp.get(i));
            } else if (splitByDash.length == 2) {
                sb.append(tmp.get(predecessorIdx).split("-")[0]);
                sb.append("-");
                sb.append(tmp.get(i));
                result[i] = fmtLong.parse(sb.toString());
            } else if (splitByDash.length == 1 && tmp.get(predecessorIdx).split("-").length == 3) {
                sb.append(tmp.get(predecessorIdx).split("-")[0]);
                sb.append("-");
                sb.append(tmp.get(predecessorIdx).split("-")[1]);
                sb.append("-");
                sb.append(tmp.get(i));
                result[i] = fmtLong.parse(sb.toString());
            } else if (splitByDash.length == 1 && tmp.get(predecessorIdx).split("-").length == 2) {
                sb.append(tmp.get(predecessorIdx).split("-")[0]);
                sb.append("-");
                sb.append(tmp.get(i));
                result[i] = fmtShort.parse(sb.toString());
            }

            sb.setLength(0);
        }
        return result;
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
            // TODO convert multiple dates to sgf compatible date string
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-DD", Locale.ENGLISH);
            res += "DT[" + "]";
        }

        if (result != null) {
            res += "RE[" + result + "]";
        }

        return res;
    }

    public void setDates(Date[] inputDates) {
        this.dates = inputDates;
    }

    public Date[] getDates() {
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
