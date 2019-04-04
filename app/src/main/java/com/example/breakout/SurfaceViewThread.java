package com.example.breakout;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.TextView;

public class SurfaceViewThread extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    private SurfaceHolder surfaceHolder = null;

    private Paint paint = new Paint();

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

    private Paddle paddle;

    private int score = 0;
    private int CdistX,CdistY,nearestX,nearestY,CornerDist1,CornerDist2;

    public SurfaceViewThread(Context context) {
        super(context);

        // Get SurfaceHolder object.
        surfaceHolder = this.getHolder();

        // Add current object as the callback listener.
        surfaceHolder.addCallback(this);

        //Get width and height screen
        Display ecran = ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        screenWidth= ecran.getWidth();
        screenHeight= ecran.getHeight();

        //Initialisation paddle, balle, briques
        paddle = new Paddle(screenWidth, screenHeight);
        cercle = new Cercle(screenWidth, screenHeight, 55, 15);
        createBricksAndRestart();

        // Set the SurfaceView object at the top of View object.
        setZOrderOnTop(true);

    }
    public boolean inter(Cercle c,Brique b){
        nearestX = (int)Math.max(b.getRect().left,Math.min(c.getX(),b.getRect().left + b.getWidth() - b.getPadding()));
        nearestY = (int)Math.max(b.getRect().top,Math.min(c.getY(),b.getRect().top + b.getHeight() - b.getPadding()));

        CdistX = c.getX() - nearestX;
        CdistY = c.getY() - nearestY;

        return(CdistX * CdistX + CdistY * CdistY) < (c.getDiametre() * c.getDiametre());
    }
    public boolean intersects(Cercle c, Brique b) {
        boolean intersects = false;
        if (c.getX() + c.getDiametre() > b.getRect().left&&
                c.getX() - c.getDiametre() < b.getRect().right&&
                c.getY() - c.getDiametre() < b.getRect().bottom &&
                c.getY() + c.getDiametre() > b.getRect().top ) {
            intersects = true;
        }
        return intersects;

    }

    public boolean intersectsP(Cercle c, Paddle P) {
        boolean intersects = false;
        if (c.getX() + c.getDiametre() > P.getRect().left &&
                c.getX() - c.getDiametre() < P.getRect().right &&
                c.getY() - c.getDiametre() < P.getRect().bottom &&
                c.getY() + c.getDiametre() > P.getRect().top ) {
            intersects = true;
        }
        return intersects;
    }

    public void update() {

        //Initialisation de la raquette et de la balle
        paddle.update(this);
        cercle.move(this);

        // Check for ball colliding with a brick
       for (int i = 0; i < nbBricks; i++) {
            if (bricks[i].getVisibility()) {
                if (/*intersects(cercle, bricks[i])*/inter(cercle,bricks[i])) {
                    if(bricks[i].getRes() > 0) {
                        bricks[i].setRes();
                        cercle.reverseYVelocity();
                    }else{
                        bricks[i].setInvisible();
                        bricks[i].setRes();
                        cercle.reverseYVelocity();
                        score+=1;
                    }
                }
            }
        }
        // Check for ball colliding with paddle
        if (intersectsP(cercle, paddle)) {
            cercle.setRandomXVelocity();
            cercle.reverseYVelocity();
            cercle.clearObstacleY((int)paddle.getRect().top - 2);
        }

        if (cercle.getX() < 1 ) {
            cercle.reverseXVelocity();
        }
        if (cercle.getY() < 1 ) {
            cercle.reverseYVelocity();
        }
        if (cercle.getX() > screenWidth) {
            cercle.reverseXVelocity();
        }
        if (cercle.getY() > screenHeight) {
            cercle.reverseYVelocity();
        }
    }

    public void createBricksAndRestart() {
        brickWidth = screenWidth / 8;
        brickHeight = screenHeight / 15;

        // Build a wall of bricks
        nbBricks = 0;
        for (int column = 0; column < 8; column++) {
            for (int row = 0; row < 6; row++) {
                bricks[nbBricks] = new Brique(column, row, brickWidth, brickHeight);
                nbBricks++;
            }
        }
    }

    @Override
    public void run() {

        createBricksAndRestart();

        while(threadRunning) {
            update();
            draw();
            try {
                Thread.sleep(1);
            }catch (InterruptedException ex) {}
        }
    }

    private void draw() {
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

        // Choose the brush color for drawing
        paint.setColor(Color.argb(255, 255, 255, 255));

        // Draw the paddle
        canvas.drawRect(paddle.getRect(), paint);

        // Draw the bricks
        for (int i = 0; i < nbBricks; i++) {
            if (bricks[i].getVisibility()) {
                bricks[i].draw(canvas);
            }
        }

        // Draw the ball
        cercle.draw(canvas);

        // Send message to main UI thread to update the drawing to the main view special area.
        surfaceHolder.unlockCanvasAndPost(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {

        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            // Player has touched the screen
            case MotionEvent.ACTION_DOWN:
                if (motionEvent.getX() > screenWidth / 2) {
                    paddle.setMovementState(paddle.RIGHT);
                }else{
                    paddle.setMovementState(paddle.LEFT);
                }
                break;
            // Player has removed finger from screen
            case MotionEvent.ACTION_UP:
                paddle.setMovementState(paddle.STOPPED);
                break;
        }
        return true;
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