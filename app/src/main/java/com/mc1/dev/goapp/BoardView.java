package com.mc1.dev.goapp;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

public class BoardView extends View {

    private int boardSize;
    private float lineOffset;
    private float points[];   // the actual coordinates of the points on the screen, given as x/y
    private int setPoints[]; // the indices of the points, that are filled with stones, given as x-index / y-index / color
    private Paint linePaint;

    public BoardView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        linePaint = new Paint();
        linePaint.setStyle(Paint.Style.FILL);
        linePaint.setColor(Color.BLACK);
        linePaint.setStrokeWidth(3);
        this.setWillNotDraw(false);

        this.setDrawingCacheEnabled(true);

        setPoints = null;
    }

    // ----------------------------------------------------------------------
    // function onDraw()
    //
    // draws a single frame into the view
    //
    // call BoardView.invalidate() to trigger
    // ----------------------------------------------------------------------
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        float middle = getHeight()/2;
        calcLineOffset(width); // the width of the screen is the size of the board, as it is quadratic

        // set the bitmap, on which is drawn the same as on the canvas
        //canvas.setBitmap(canvasBitmap);

        // draw background
        Resources res = getResources();
        Drawable backgroundImg = res.getDrawable(R.drawable.dull_boardbackground);
        if (backgroundImg != null) {
            backgroundImg.setBounds(0, Math.round(middle - ((boardSize + 1) * lineOffset)/2)-3, width, Math.round(middle + ((boardSize-1) * lineOffset)/2)+3);
            backgroundImg.draw(canvas);
        }

        // initialize board with lines
        constructPoints((float)(getHeight() - (middle + (0.5*width))));
        drawLines(canvas);

        if (setPoints != null) {
            drawStones(canvas);
        }
    }

    // ----------------------------------------------------------------------
    // function refresh()
    //
    // reads the data of the given RunningGame, according to the given tree
    // index list and paints itself accordingly
    // ----------------------------------------------------------------------
    public void refresh(ArrayList<Integer> treeIndices, RunningGame game) {
        setPoints = new int[treeIndices.size()*3]; // x/y index values of the points
        int counter = 0;
        ArrayList<Integer> tempList = new ArrayList<Integer>();

        for (int i = 0; i < treeIndices.size(); i++) {
            tempList.add(treeIndices.get(i));
            MoveNode move        = game.getSpecificNode(tempList);
            if (move.isPrisoner() /* || move.getActionType == pass */) {
                int[] position = {boardSize, boardSize};
                move.setPosition(position);
            }

            setPoints[counter] = move.getPosition()[0]; // x-index
            setPoints[counter + 1] = move.getPosition()[1]; // y-index

            if (move.isBlacksMove()) {
                setPoints[counter + 2] = 1;
            }
            else {
                setPoints[counter + 2] = 0;
            }
            counter = counter+3;
        }

        this.invalidate();
    }

    // ----------------------------------------------------------------------
    // function calcLineOffset()
    //
    // the line offset is the space between each line in both x and y direction
    // ----------------------------------------------------------------------
    private void calcLineOffset(int boardSizePixels) {
        lineOffset = boardSizePixels / boardSize; // add 1 offset line for the border
    }

    // ----------------------------------------------------------------------
    // function constructPoints()
    //
    // calculates the coordinates of the intersections of the board lines
    // stores coordinates in the points member as x1,y1,x2,y2....
    // ----------------------------------------------------------------------
    private void constructPoints(float upperOffset) {
        points = new float[boardSize*boardSize*2];
        int count = 0;

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++ ) {
                points[count] = (lineOffset/2) + i * lineOffset; // x
                count++;
                points[count] = upperOffset + j * lineOffset; // y
                count++;
            }
        }
    }

    // ----------------------------------------------------------------------
    // function drawLines()
    //
    // uses the calculated points to draw all lines into the canvas
    // ----------------------------------------------------------------------
    private void drawLines(Canvas canvas) {
        int count = 0;

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (j < boardSize-1) { // if not in the bottom row of points
                    canvas.drawLine(points[count], points[count+1], points[count+2], points[count+3], linePaint); // line straight down
                }
                if (i < boardSize-1) { // if not in the right column of points
                    canvas.drawLine(points[count], points[count+1], points[count+(boardSize*2)], points[count+(boardSize*2)+1], linePaint); // line to the left
                }
                count = count +2; // index to next point
            }
        }
    }


    // ----------------------------------------------------------------------
    // function drawStones()
    //
    // draws the images of the stones into the board
    // ----------------------------------------------------------------------
    private void drawStones(Canvas canvas) {

        for (int  i = 0; i < setPoints.length; i=i+3) {
            // if given stone has an intentional invalid position eg. prisoners
            if (setPoints[i] == boardSize && setPoints[i+1] == boardSize) {
                continue;
            }

            // calculate the index a which the stone is given in the array
            // array[n] => x; array[n+1] => y; array[n+2] => color;
            int pointIndex = (setPoints[i]*boardSize + setPoints[i+1])*2;

            Resources res = getResources();
            Drawable stoneImg;
            int xVal = Math.round(points[pointIndex]);
            int yVal = Math.round(points[pointIndex+1]);

            int stoneDimension = Math.round(lineOffset/2) - 3;
            if (setPoints[i+2] == 1) { // if is black stone
                stoneImg = res.getDrawable(R.drawable.black_stone);
            }
            else {
                stoneImg = res.getDrawable(R.drawable.white_stone);
            }
            if (stoneImg != null) {
                stoneImg.setBounds(xVal - stoneDimension, yVal - stoneDimension, xVal + stoneDimension, yVal + stoneDimension);
                stoneImg.draw(canvas);
            }
        }
    }

    public Bitmap getBitmap() {
        return this.getDrawingCache();
    }

    public void setBoardSize(int size) {
        this.boardSize = size;
    }

    public int getBoardSize() {
        return boardSize;
    }

    public float[] getPoints() {
        return points;
    }

    public float getLineOffset() {
        return lineOffset;
    }
}
