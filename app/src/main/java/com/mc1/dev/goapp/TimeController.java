package com.mc1.dev.goapp;

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
    private boolean isHCGame;
    private boolean isFirstMove = true;
    private boolean blacksTurn = true;
    private String timeMode;
    private long mainTime;
    private long overTime;
    private byte otPeriods;

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

    public void configure(boolean hcGame, String timeMode, long mainTime, long overTime, byte otPeriods, long countDownInterval, TextView tv1, TextView tv2) {
        this.isHCGame = hcGame;
        this.timeMode = timeMode;
        this.mainTime = mainTime;
        this.overTime = overTime;
        this.otPeriods = otPeriods;
        this.tvBlack = tv1;
        this.tvWhite = tv2;
        this.blackTimeLeft = mainTime;
        this.whiteTimeLeft = mainTime;
        this.countDownInterval = countDownInterval;
        this.blackPeriodsLeft = otPeriods;
        this.whitePeriodsLeft = otPeriods;
    }

    public long swapTimePeriods(boolean isBlacksMove) {

        // TODO

        if (isBlacksMove) {
            if (whiteIsInOvertime && whiteOverTime != null) {
                whiteOverTime.cancel();
            } else if (!whiteIsInOvertime && whiteMainTime != null) {
                whiteMainTime.cancel();
            }

            if (blackIsInOvertime) {
                createNewOTTimer(true);
            } else {
                blackMainTime = new CountDownTimer(blackTimeLeft, countDownInterval) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        blackTimeLeft = millisUntilFinished;
                        tvBlack.setText("" + blackTimeLeft / 1000);
                    }

                    @Override
                    public void onFinish() {
                        blackIsInOvertime = true;
                        blackTimeLeft = overTime;
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
                whiteMainTime = new CountDownTimer(whiteTimeLeft, countDownInterval) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        whiteTimeLeft = millisUntilFinished;
                        tvWhite.setText("" + whiteTimeLeft / 1000);
                    }

                    @Override
                    public void onFinish() {
                        whiteIsInOvertime = true;
                        whiteTimeLeft = overTime;
                        createNewOTTimer(false);
                        cancel();
                    }
                }.start();
            }
        }
        blacksTurn = !blacksTurn;
        // TODO return long time left
        return 0;
    }

    private void createNewOTTimer(boolean isBlacksTurn) {
        if (isBlacksTurn) {
            blackOverTime = new CountDownTimer(blackTimeLeft, countDownInterval) {
                @Override
                public void onTick(long millisUntilFinished) {
                    blackTimeLeft = millisUntilFinished;
                    tvBlack.setText("(" + blackPeriodsLeft + ") " + blackTimeLeft / 1000);
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
                    whiteTimeLeft = millisUntilFinished;
                    tvWhite.setText("(" + whitePeriodsLeft + ") " + whiteTimeLeft / 1000);
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

    public long getBlackTimeLeft() {
        return this.blackTimeLeft;
    }

    public long getWhiteTimeLeft() {
        return this.whiteTimeLeft;
    }

    public long getMainTime() {
        return mainTime;
    }

    public long getOverTime() {
        return overTime;
    }
}
