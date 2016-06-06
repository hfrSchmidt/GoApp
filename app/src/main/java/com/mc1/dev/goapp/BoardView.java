package com.mc1.dev.goapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class BoardView extends View {

    private int boardSize;
    private float lineOffset;
    private float points[];
    private Paint linePaint;

    public BoardView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        linePaint = new Paint();
        linePaint.setStyle(Paint.Style.FILL);
        linePaint.setColor(Color.BLACK);
        linePaint.setStrokeWidth(3); // TODO configurable
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

        // initialize board with lines
        int width = getWidth();
        float middle = getHeight()/2;
        calcLineOffset(width); // the width of the screen is the size of the board, as it is quadratic
        constructPoints((float)(getHeight() - (middle + (0.5*width))));
        drawLines(canvas);

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
