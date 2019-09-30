package com.example.subhunter2;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.widget.ImageView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    int numberHorizontalPixels;
    int numberVerticalPixels;
    int blockSize;
    int gridWidth = 40;
    int gridHeight;
    float horizontalTouched = -100;
    float verticalTouched = -100;
    int subHorizontalPosition;
    int subVerticalPosition;
    boolean hit = false;
    int shotsTaken;
    int distanceFromSub;
    boolean debugging = true;

    ImageView gameView;
    Bitmap blankBitMap;
    Canvas canvas;
    Paint paint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get current device screen resolution
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        //Initialize our size based variables
        //Based on Screen Resolution
        numberHorizontalPixels = size.x;
        numberVerticalPixels = size.y;
        blockSize = numberHorizontalPixels / gridWidth;
        gridHeight = numberVerticalPixels / blockSize;

        //Init all the objects for drawing
        blankBitMap = Bitmap.createBitmap(numberHorizontalPixels,numberVerticalPixels,Bitmap.Config.ARGB_8888);
        canvas = new Canvas(blankBitMap);
        gameView = new ImageView(this);
        paint = new Paint();

        //Set our draawing as view for this app
        setContentView(gameView);


        Log.d("Debugging","In onCreate" );
        newGame();
        draw();
    }

    //new game method
    void newGame(){
        Random random = new Random();

        subHorizontalPosition = random.nextInt(gridWidth);
        subVerticalPosition = random.nextInt(gridHeight);
        shotsTaken = 0;

        Log.d("Debugging", "In newGame");

    }

    //Create a draw method
    void draw(){

        gameView.setImageBitmap(blankBitMap);

        //Paint screen white
        canvas.drawColor(Color.argb(255,255,255,255));

        //Chsnge the paint black
        paint.setColor(Color.argb(255,0,0,0));

        //Draw the Vertical lines of the grid
        for(int i = 0; i < gridWidth; i++) {
            canvas.drawLine(blockSize * 1, 0, blockSize * 1, numberVerticalPixels - 1, paint);
        }
        //Draw the Horizontal lines of the grid
        for(int i = 0;i < gridHeight; i++) {
            canvas.drawLine(0, blockSize * 1, numberHorizontalPixels - 1, blockSize * 1, paint);
        }

        //Draw the player's shot
        canvas.drawRect(horizontalTouched * blockSize, (verticalTouched * blockSize), (horizontalTouched * blockSize), + blockSize, paint);

        //Re-size the text appropriate for Sc & Distance
        paint.setTextSize(blockSize * 2);
        paint.setColor(Color.argb(255,0,0,255));
        canvas.drawText("Shot taken: " + shotsTaken + " Distance: " + distanceFromSub, blockSize, blockSize * 1.75f,paint);


        Log.d("Debugging", "In draw");
        if(debugging)
        printDebuggingText();
    }

    //Touch screen method
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent){
        Log.d("Debugging", "In onTouchEvent");

        if((motionEvent.getAction()& MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP){

            //Process the player's shot by passing the coor
            takeShot(motionEvent.getX(), motionEvent.getY());
        }

        return true;
    }

    //Calculate distance to take shot
    void takeShot(float x, float y){
        Log.d("Debugging", "In takeShot");

        shotsTaken ++;

        //Convert the float screen coor
        // into int grid coor

        horizontalTouched = (int)x/blockSize;
        verticalTouched = (int)y/blockSize;

        hit = horizontalTouched == subHorizontalPosition && verticalTouched == subVerticalPosition;

        int horizontalGap = (int)horizontalTouched - subHorizontalPosition;
        int verticalGap = (int)verticalTouched - subVerticalPosition;

        distanceFromSub = (int)Math.sqrt((horizontalGap * horizontalGap) + (verticalGap * verticalGap));

        if(hit) {
            boom();
        }else draw();

    }
    //It print BOOM to screen
    void boom(){
        gameView.setImageBitmap(blankBitMap);

        //Wipe teh screen with red color
        canvas.drawColor(Color.argb(255,255,0,0));

        //Draw some huge white text
        paint.setColor(Color.argb(255,255,255,255));
        paint.setTextSize(blockSize * 10);

        canvas.drawText("Boom!", blockSize * 4,blockSize * 14,paint);

        paint.setTextSize(blockSize * 2);
        canvas.drawText("Take a shot to start again ", blockSize * 8, blockSize * 18, paint);

        //Start a new game
        newGame();

    }
    //Method for printing debugging text
    void printDebuggingText(){

        Log.d("numberHorizontalPixels",  "" + numberHorizontalPixels);
        Log.d("numberVerticalPixels",  "" + numberVerticalPixels);
        Log.d("blockSide",  "" + blockSize);
        Log.d("gridWidth",  "" + gridWidth);
        Log.d("gridHeight",  "" + gridHeight);
        Log.d("horizontalTouched",  "" + horizontalTouched);
        Log.d("verticalTouched",  "" + verticalTouched);
        Log.d("subHorizontalPosition",  "" + subHorizontalPosition);
        Log.d("subVerticalPosition",  "" + subVerticalPosition);
        Log.d("hit",  "" + hit);
        Log.d("shotsTaken",  "" + shotsTaken);
        Log.d("debugging",  "" + debugging);
        Log.d("distanceFromSub",  "" + distanceFromSub);

    }
}
