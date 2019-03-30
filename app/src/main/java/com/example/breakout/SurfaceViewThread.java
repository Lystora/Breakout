package com.example.breakout;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

public class SurfaceViewThread extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    private SurfaceHolder surfaceHolder = null;

    private Paint paint = null;

    private Thread thread = null;

    // Record whether the child thread is running or not.
    private boolean threadRunning = false;

    private Canvas canvas = null;

    private int screenWidth = 0;

    private int screenHeight = 0;

    private Cercle cercle;

    private Brique[] bricks = new Brique[200];

    private int nbBricks = 0;

    private int brickWidth;

    private int brickHeight;

    private int distance, cercleDx, cercleDy;

    public SurfaceViewThread(Context context){
        super(context);
        // Get SurfaceHolder object.
        surfaceHolder = this.getHolder();

        // Add current object as the callback listener.
        surfaceHolder.addCallback(this);

        paint = new Paint();
        paint.setColor(Color.GREEN);

        cercle = new Cercle(screenWidth+100,screenHeight+400,55,25);
        //cercle.setSpeed(20);
        // Set the SurfaceView object at the top of View object.
        setZOrderOnTop(true);

        createBricksAndRestart();

    }

    @Override
    public void run() {
        createBricksAndRestart();
        while(threadRunning){

            long startTime = System.currentTimeMillis();



            cercle.move(this);
            update();
            draw();

            try {
                Thread.sleep(1);
            }catch (InterruptedException ex){}
        }
    }

    public boolean intersect(Cercle c, Brique b){
        cercleDx = c.getX() - b.getX() - b.getWidth()/2;
        cercleDy = c.getY() - b.getY() - b.getHeight()/2;

        if(cercleDx > (b.getWidth()/2 + c.getDiametre())){return false;}
        if(cercleDy > (b.getHeight()/2 + c.getDiametre())){return false;}

        if(cercleDx <= (b.getWidth()/2 )){return false;}
        if(cercleDy <= (b.getHeight()/2 )){return false;}

        distance = (cercleDx - b.getWidth()/2)^2 + (cercleDy - b.getHeight()/2)^2;
        return(distance <= (c.getDiametre()^2));
    }

    public void update(){
        // Check for ball colliding with a brick
        for (int i = 0; i < nbBricks; i++) {
            if (bricks[i].getVisibility()) {
                if (intersect(cercle, bricks[i])) {
                    bricks[i].setInvisible();
                    System.out.print("invisible");
                }
            }
        }
    }
    public void createBricksAndRestart() {

        brickWidth = screenWidth / 8;

        brickHeight = screenHeight / 15;

        // Build a wall of bricks
        nbBricks = 0;
        for (int column = 0; column < 8; column++) {
            for (int row = 0; row < 3; row++) {
                bricks[nbBricks] = new Brique(column, row, brickWidth, brickHeight);
                nbBricks++;
            }
        }
    }

    private void draw(){
        int margin = 10000;
        int left = 0;
        int top = 0;
        int right = screenWidth;
        int bottom = screenHeight;
        Rect fond = new Rect(left, top, right, bottom);

        canvas = surfaceHolder.lockCanvas();

        // Draw the specify canvas background color.
        Paint background = new Paint();
        background.setColor(Color.BLACK);

        canvas.drawRect(fond, background);


        for (int i = 0; i < nbBricks; i++) {
            if (bricks[i].getVisibility()) {
                paint.setColor(Color.WHITE);
                canvas.drawRect(bricks[i].getRect(), paint);
            }
        }
        cercle.draw(canvas);

        // Send message to main UI thread to update the drawing to the main view special area.
        surfaceHolder.unlockCanvasAndPost(canvas);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // Create the child thread when SurfaceView is created.
        thread = new Thread(this);
        // Start to run the child thread.
        thread.start();
        // Set thread running flag to true.
        threadRunning = true;

        // Get screen width and height.
        screenHeight = getHeight();
        screenWidth = getWidth();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // Set thread running flag to false when Surface is destroyed.
        // Then the thread will jump out the while loop and complete.
        threadRunning = false;
    }
}