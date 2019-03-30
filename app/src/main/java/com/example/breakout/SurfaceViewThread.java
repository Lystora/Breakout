package com.example.breakout;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class SurfaceViewThread extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    private SurfaceHolder surfaceHolder = null;

    private Paint paint = null;
    private Paint pBriques = new Paint();

    private Thread thread = null;

    // Record whether the child thread is running or not.
    private boolean threadRunning = false;

    private Canvas canvas = null;

    private int screenWidth = 0;

    private int screenHeight = 0;

    private Cercle cercle;

    private Briques[] bricks = new Briques[200];

    private int brickWidth;
    private int brickHeight;
    private int nbBricks = 0;
    public SurfaceViewThread(Context context){
        super(context);

        //setFocusable(true);
        setBackgroundColor(Color.BLACK);
        // Get SurfaceHolder object.
        surfaceHolder = this.getHolder();
        // Add current object as the callback listener.
        surfaceHolder.addCallback(this);
        // Create the paint object which will draw the text.
        paint = new Paint();
        paint.setColor(Color.GREEN);

        cercle = new Cercle(screenWidth+100,screenHeight+400,55,25);
        //cercle.setSpeed(20);
        pBriques.setColor(Color.BLACK);
        createBricksAndRestart();

        // Set the SurfaceView object at the top of View object.
        setZOrderOnTop(true);
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

    @Override
    public void run() {
        while(threadRunning){
            long startTime = System.currentTimeMillis();

            cercle.move(this);
            draw();
            //long endTime = System.currentTimeMillis();

            //long deltaTime = endTime - startTime;
            try {
                Thread.sleep(1/10000);
            }catch (InterruptedException ex){
            }
            /*if(deltaTime < 1){
                try {
                    Thread.sleep(1 - deltaTime);
                }catch (InterruptedException ex){
                }
            }*/
        }
    }
    public void createBricksAndRestart() {

        brickWidth = screenWidth / 8;
        brickHeight = screenHeight / 10;

        // Build a wall of bricks
        nbBricks = 0;
        for (int column = 0; column < 8; column++) {
            for (int row = 0; row < 3; row++) {
                bricks[nbBricks] = new Briques(row, column, brickWidth, brickHeight);
                nbBricks++;
            }
        }
    }

    public void collision(){
        for(int i=0;i<bricks.length;i++){
            if( (cercle.getX() >= bricks[i].getRect().left && cercle.getX() <= bricks[i].getRect().right) &&
                    ( cercle.getY() >= bricks[i].getRect().top && cercle.getY() <= bricks[i].getRect().bottom)){
                bricks[i].setInvisible();
            }
        }
    }
    private void draw(){
        int margin = 10000;

        int left = 0;

        int top = 0;

        int right = screenWidth;

        int bottom = screenHeight;

        Rect rect = new Rect(left, top, right, bottom);


        // Only draw text on the specified rectangle area.
        canvas = surfaceHolder.lockCanvas();

        // Draw the specify canvas background color.
        Paint backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.WHITE);
        canvas.drawRect(rect, backgroundPaint);

        paint.setColor(Color.BLUE);
        createBricksAndRestart();

        //canvas.drawRect(100, screenHeight/8, screenWidth-100, screenHeight/2, paint );
        for (int i = 0; i < nbBricks; i++) {
            if (bricks[i].getVisibility()) {
                canvas.drawRect(bricks[i].getRect(), pBriques);
            }
        }


        cercle.draw(canvas);

        // Send message to main UI thread to update the drawing to the main view special area.
        surfaceHolder.unlockCanvasAndPost(canvas);
    }
}