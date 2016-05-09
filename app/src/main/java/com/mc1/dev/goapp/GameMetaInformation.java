package com.mc1.dev.goapp;

// ----------------------------------------------------------------------
// class GameMetaInformation
// gmi is a data container to store information of a single game
// stored are only option parameters like komi and time mode
// no information on current board state is stored
// ----------------------------------------------------------------------
public class GameMetaInformation {
    private float komi;
    private byte handicap;
    private String timeMode;
    private String gameMode;

    public GameMetaInformation(float komi, byte handicap, String timeMode, String gameMode) {
        this.komi = komi;
        this.handicap = handicap;
        this.timeMode = timeMode;
        this.gameMode = gameMode;
    }

    public float getKomi() {
        return komi;
    }

    public void setKomi(float komi) {
        this.komi = komi;
    }

    public byte getHandicap() {
        return handicap;
    }

    public String getTimeMode() {
        return timeMode;
    }

    public String getGameMode() {
        return gameMode;
    }
}
