package com.mc1.dev.goapp;


import java.util.ArrayList;
import java.util.Stack;

public class GameController {

    // ----------------------------------------------------------------------
    // singleton implementation
    // ----------------------------------------------------------------------
    private static GameController instance;

    private GameController() {
    }

    public static GameController getInstance() {
        if (GameController.instance == null) {
            GameController.instance = new GameController();
        }
        return GameController.instance;
    }

    // ----------------------------------------------------------------------
    // class implementation
    // ----------------------------------------------------------------------
    public enum failureType {
        SUCCESS,
        OCCUPIED,
        KO,
        SUICIDE,
        END,
    }

    public failureType checkAction(GameMetaInformation.actionType actionType, RunningGame game, int[] position, boolean isBlacksMove) {
        if (!checkOccupied(game, position)) {
            return failureType.OCCUPIED;
        }
        if (!checkSuicide(game, position, isBlacksMove)) {
            return failureType.SUICIDE;
        }
        if (!checkKo(position)) {
            return failureType.KO;
        }
        if (!checkGameEnded(position)) {
            return failureType.END;
        }

        return failureType.SUCCESS;
    }

    public void calcPrisoners(RunningGame game, boolean isBlacksMove) {

        int counter = 0;

        ArrayList<Integer> tempList = new ArrayList<Integer>();

        // go through all move nodes
        for (int i = 0; i < game.getMainTreeIndices().size(); i++) {
            tempList.add(game.getMainTreeIndices().get(i));
            MoveNode move = game.getSpecificNode(tempList);

            if (move.isBlacksMove() != isBlacksMove && !move.isPrisoner() && move.getActionType() != GameMetaInformation.actionType.PASS) { // if a black stone is set, check for every white stone, if it is a prisoner
                int stone[] = {move.getPosition()[0], move.getPosition()[1]};

                if (isPrisoner(game, stone, !isBlacksMove)) { // if the found stone is a prisoner
                    game.setAsPrisoner(tempList);
                    counter++;
                }
            }
        }

        if (isBlacksMove) {
            game.getGameMetaInformation().setBlackPrisoners(game.getGameMetaInformation().getBlackPrisoners() +  counter);
        }
        else {
            game.getGameMetaInformation().setWhitePrisoners(game.getGameMetaInformation().getWhitePrisoners() + counter);
        }
    }

    // ----------------------------------------------------------------------
    // function checkOccupied()
    //
    // returns true, if the given position is already occupied by another stone
    // ----------------------------------------------------------------------
    private boolean checkOccupied(RunningGame game, int[] position) {

        boolean success = true;
        ArrayList<Integer> tempList = new ArrayList<Integer>();

        for (int i = 0; i < game.getMainTreeIndices().size(); i++) {
            tempList.add(game.getMainTreeIndices().get(i));
            MoveNode move = game.getSpecificNode(tempList);

            if(position[0] == move.getPosition()[0] && position[1] == move.getPosition()[1]) {
                success = false;
                break;
            }
        }
        return success;
    }

    private boolean checkSuicide(RunningGame game, int[] position, boolean isBlack) {
        return !isPrisoner(game, position, isBlack);
    }

    private boolean checkKo(int[] position) {
        return true;
    }

    private boolean checkGameEnded(int[] position) {
        return true;
    }

    // ----------------------------------------------------------------------
    // function isPrisoner()
    //
    // returns true, if the stone at given position in color isBlack is a prisoner
    // ----------------------------------------------------------------------
    private boolean isPrisoner(RunningGame game, int[] position, boolean isBlack) {

        boolean leftWall = false;
        boolean rightWall = false;
        boolean topWall = false;
        boolean downWall = false;
        int boardSize = game.getGameMetaInformation().getBoardSize();
        int setPoints[][] = new int[boardSize][boardSize];
        ArrayList<Integer> tempList = new ArrayList<Integer>();

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                setPoints[i][j] = 0;
            }
        }

        for (int i = 0; i < game.getMainTreeIndices().size(); i++) {
            tempList.add(game.getMainTreeIndices().get(i));
            MoveNode move = game.getSpecificNode(tempList);

            if (move.isBlacksMove() != isBlack && !move.isPrisoner() && move.getActionType() != GameMetaInformation.actionType.PASS) { // if given stone is black, only look for white stones, that are not prisoners
                setPoints[move.getPosition()[0]][move.getPosition()[1]] = 1;
            }
        }

        Stack<Integer> stack = new Stack<Integer>();
        stack.push(position[0]);
        stack.push(position[1]);

        while (!stack.isEmpty()) {
            int y = stack.pop();
            int x = stack.pop();

            if (x+1 == boardSize) {
                rightWall = true;
            }
            if (x-1 < 0) {
                leftWall = true;
            }
            if (y+1 == boardSize) {
                downWall = true;
            }
            if (y-1 < 0) {
                topWall = true;
            }


           // if (!(y >= boardSize) || !(y < 0)) {
                if (!(x + 1 == boardSize)) {
                    if (setPoints[x+1][y] != 1) {
                        setPoints[x+1][y] = 1;
                        stack.push( x+1 ); stack.push( y );
                    }
                }
                if (!(x - 1 < 0)) {
                    if (setPoints[x-1][y] != 1) {
                        setPoints[x-1][y] = 1;
                        stack.push( x-1 ); stack.push( y );
                    }
                }
            //}
            //if (!(x >= boardSize) || !(x < 0)) {
                if (!(y + 1 == boardSize)) {
                    if (setPoints[x][y + 1] != 1 ) {
                        setPoints[x][y+1] = 1;
                        stack.push(x);stack.push(y + 1);
                    }
                }
                if (!(y - 1 < 0)) {
                    if (setPoints[x][y - 1] != 1) {
                        setPoints[x][y-1] = 1;
                        stack.push(x);stack.push(y - 1);
                    }
                }

            if (rightWall && leftWall && downWall && topWall) {
                return false;
            }
           // }
        } // while stack !empty

        return !(rightWall && leftWall && downWall && topWall);
    }

}
