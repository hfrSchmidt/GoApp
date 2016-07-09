package com.mc1.dev.goapp;

import android.content.Context;
import android.content.res.Resources;
import android.os.CountDownTimer;
import android.widget.TextView;

// ----------------------------------------------------------------------
// class TimeController
// This class provides functionality to handle time control of games.
// ----------------------------------------------------------------------
public class TimeController {

    private long blackTimeLeft;
    private byte blackPeriodsLeft;
    private boolean blackIsInOvertime = false;
    private long whiteTimeLeft;
    private byte whitePeriodsLeft;
    private boolean whiteIsInOvertime = false;
    private String timeMode;
    private long mainTime;
    private long overTime;
    private byte otPeriods;
    private String label;

    private CountDownTimer blackMainTime;
    private CountDownTimer blackOverTime;
    private CountDownTimer whiteMainTime;
    private CountDownTimer whiteOverTime;
    private long countDownInterval;

    private TextView tvBlack;
    private TextView tvWhite;

    private static TimeController ourInstance = new TimeController();

    public static TimeController getInstance() {
        return ourInstance;
    }

    private TimeController() {
    }

    public void configure(String timeMode, long mainTime, long overTime, byte otPeriods, long countDownInterval, TextView tvBlack, TextView tvWhite, String label) {
        this.timeMode = timeMode;
        this.mainTime = mainTime;
        this.overTime = overTime;
        this.otPeriods = otPeriods;
        this.tvBlack = tvBlack;
        this.tvWhite = tvWhite;
        this.blackTimeLeft = mainTime;
        this.whiteTimeLeft = mainTime;
        this.countDownInterval = countDownInterval;
        this.blackPeriodsLeft = otPeriods;
        this.whitePeriodsLeft = otPeriods;
        this.label = label;
    }

    // function is to be called every time a player makes a move
    // returns the amount of time left for black/white
    public long swapTimePeriods(boolean isBlacksMove) {
        if (isBlacksMove) {
            if (whiteIsInOvertime && whiteOverTime != null) {
                whiteOverTime.cancel();
            } else if (!whiteIsInOvertime && whiteMainTime != null) {
                whiteMainTime.cancel();
            }

            if (blackIsInOvertime) {
                createNewOTTimer(true);
            } else {
                // still have to set the current periods for the construction of MoveNodes
                this.blackPeriodsLeft = 1;
                blackMainTime = new CountDownTimer(blackTimeLeft, countDownInterval) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        String content = label + "" + blackTimeLeft / 1000;
                        blackTimeLeft = millisUntilFinished;
                        tvBlack.setText(content);
                    }

                    @Override
                    public void onFinish() {
                        blackIsInOvertime = true;
                        blackTimeLeft = overTime;
                        blackPeriodsLeft = otPeriods;
                        createNewOTTimer(true);
                        blackMainTime.cancel();
                    }
                }.start();
            }
        } else {
            if (blackIsInOvertime && blackOverTime != null) {
                blackOverTime.cancel();
            } else if (!blackIsInOvertime && blackMainTime != null) {
                blackMainTime.cancel();
            }
            if (whiteIsInOvertime) {
                createNewOTTimer(false);
            } else {
                // still have to set the current periods for the construction of MoveNodes
                this.whitePeriodsLeft = 1;
                whiteMainTime = new CountDownTimer(whiteTimeLeft, countDownInterval) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        String content = label + "" + whiteTimeLeft / 1000;
                        whiteTimeLeft = millisUntilFinished;
                        tvWhite.setText(content);
                    }

                    @Override
                    public void onFinish() {
                        whiteIsInOvertime = true;
                        whiteTimeLeft = overTime;
                        whitePeriodsLeft = otPeriods;
                        createNewOTTimer(false);
                        cancel();
                    }
                }.start();
            }
        }
        if (isBlacksMove) {
            return blackTimeLeft;
        } else {
            return whiteTimeLeft;
        }
    }

    private void createNewOTTimer(boolean isBlacksTurn) {
        if (timeMode.equals("japanese")) {
            if (isBlacksTurn) {
                blackOverTime = new CountDownTimer(blackTimeLeft, countDownInterval) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        String content = label + "(" + blackPeriodsLeft + ") " + blackTimeLeft / 1000;
                        blackTimeLeft = millisUntilFinished;
                        tvBlack.setText(content);
                    }

                    @Override
                    public void onFinish() {
                        if (blackPeriodsLeft <= 1) {
                            // TODO call GameController instead
                            tvBlack.setText("You lost!");
                        } else {
                            blackPeriodsLeft--;
                            blackTimeLeft = overTime;
                            createNewOTTimer(true);
                        }
                        cancel();
                    }
                }.start();
            } else {
                whiteOverTime = new CountDownTimer(whiteTimeLeft, countDownInterval) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        String content = label + "(" + whitePeriodsLeft + ") " + whiteTimeLeft / 1000;
                        whiteTimeLeft = millisUntilFinished;
                        tvWhite.setText(content);
                    }

                    @Override
                    public void onFinish() {
                        if (whitePeriodsLeft <= 1) {
                            // TODO call GameController instead
                            tvWhite.setText("You lost!");
                        } else {
                            whitePeriodsLeft--;
                            whiteTimeLeft = overTime;
                            createNewOTTimer(false);
                        }
                        cancel();
                    }
                }.start();
            }
        }
        if (timeMode.equals("canadian")) {
            if (isBlacksTurn) {
                if (blackPeriodsLeft < 1) {
                    blackTimeLeft = overTime;
                    blackPeriodsLeft = otPeriods;
                }
                blackOverTime = new CountDownTimer(blackTimeLeft, countDownInterval) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        String content = label + blackTimeLeft / 1000 + " ( " + (blackPeriodsLeft + 1) + " )";
                        blackTimeLeft = millisUntilFinished;
                        tvBlack.setText(content);
                    }

                    @Override
                    public void onFinish() {
                        // TODO call GameController instead
                        tvBlack.setText("You lost!");
                        cancel();
                    }
                }.start();
                blackPeriodsLeft--;
            } else {
                if (whitePeriodsLeft < 1) {
                    whiteTimeLeft = overTime;
                    whitePeriodsLeft = otPeriods;
                }
                whiteOverTime = new CountDownTimer(whiteTimeLeft, countDownInterval) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        String content = label + whiteTimeLeft / 1000 + " ( " + (whitePeriodsLeft + 1) + " )";
                        whiteTimeLeft = millisUntilFinished;
                        tvWhite.setText(content);
                    }

                    @Override
                    public void onFinish() {
                        // TODO call GameController instead
                        tvWhite.setText("You lost!");
                        cancel();
                    }
                }.start();
                whitePeriodsLeft--;
            }
        }
    }

    public long getBlackTimeLeft() {
        return this.blackTimeLeft;
    }

    public long getWhiteTimeLeft() {
        return this.whiteTimeLeft;
    }

    public byte getBlackPeriodsLeft() {
        return this.blackPeriodsLeft;
    }

    public byte getWhitePeriodsLeft() {
        return this.whitePeriodsLeft;
    }

    public long getMainTime() {
        return mainTime;
    }

    public long getOverTime() {
        return overTime;
    }
}
