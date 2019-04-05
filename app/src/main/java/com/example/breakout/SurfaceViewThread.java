package com.example.breakout;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

public class SurfaceViewThread extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    private SurfaceHolder surfaceHolder = null;

    private Thread thread = null;

    private Paint paint = new Paint();

    private Canvas canvas = null;

    private Cercle cercle;

    private Paddle paddle;

    private Brique[] bricks = new Brique[200];

    private int nbBricks, brickWidth, brickHeight, screenWidth, screenHeight;

    private int CdistX, CdistY, nearestX, nearestY;

    private int fps = 1;

    private boolean threadRunning = false;

    boolean paused = true;

    volatile boolean playing;

    int score = 0;

    int lives = 3;

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

    public boolean collisionBrique(Cercle c,Brique b){
        nearestX = (int)Math.max(b.getRect().left,Math.min(c.getX(),b.getRect().right));
        nearestY = (int)Math.max(b.getRect().top,Math.min(c.getY(),b.getRect().bottom));

        CdistX = c.getX() - nearestX;
        CdistY = c.getY() - nearestY;

        return(CdistX * CdistX + CdistY * CdistY) < (c.getRayon() * c.getRayon());
    }

    public boolean collisionPaddle(Cercle c,Paddle p){
        nearestX = (int)Math.max(p.getRect().left,Math.min(c.getX(),p.getRect().right));
        nearestY = (int)Math.max(p.getRect().top,Math.min(c.getY(),p.getRect().bottom));

        CdistX = c.getX() - nearestX;
        CdistY = c.getY() - nearestY;

        return(CdistX * CdistX + CdistY * CdistY) < (c.getRayon() * c.getRayon());
    }

    public void update() {
        //Initialisation de la raquette et de la balle
        paddle.update(screenWidth);
        cercle.move(fps);

        // Check for ball colliding with a brick
        for (int i = 0; i < nbBricks; i++) {
            if (bricks[i].getVisibility()) {
                if (collisionBrique(cercle,bricks[i])) {
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
        if (collisionPaddle(cercle, paddle)) {
            cercle.setRandomXVelocity();
            cercle.reverseYVelocity();
            cercle.clearObstacleY((int)paddle.getRect().top - 2);
        }

        //Check for ball colliding with screen
        if (cercle.getX() + cercle.getXSpeed() < cercle.getRayon() ) {
            cercle.reverseXVelocity();
        }
        if (cercle.getY() + cercle.getYSpeed() < brickHeight*2) {
            cercle.reverseYVelocity();
        }
        if (cercle.getX() + cercle.getXSpeed() > (screenWidth - cercle.getRayon())) {
            cercle.reverseXVelocity();
        }
        if (cercle.getY() + cercle.getYSpeed() > (screenHeight- cercle.getRayon())) {
            cercle.reverseYVelocity();
            lives--;
            if (lives == 0) {
                paused = true;
                createBricksAndRestart();
            }
        }
    }

    public void createBricksAndRestart() {
        brickWidth = screenWidth / 8;
        brickHeight = screenHeight / 15;

        cercle.reset(screenWidth, screenHeight);
        // Build a wall of bricks
        nbBricks = 0;
        for (int column = 0; column < 8; column++) {
            for (int row = 2; row < 6; row++) {
                bricks[nbBricks] = new Brique(column, row, brickWidth, brickHeight);
                nbBricks++;
            }
        }
        // Restart game
        if (lives == 0) {
            score = 0;
            lives = 3;
        }
    }

    @Override
    public void run() {
        //Initialisation des briques
        createBricksAndRestart();

        while(threadRunning) {
            if (!paused) {
                update();
            }
            draw();
            try {
                Thread.sleep(fps);
            }catch (InterruptedException ex) {}
        }
    }

    private void draw() {
        int left = 0;
        int top = 0;
        int right = screenWidth;
        int bottom = screenHeight;
        Rect fond = new Rect(left, top, right, bottom);

        if(surfaceHolder.getSurface().isValid()) {
            canvas = surfaceHolder.lockCanvas();

            // Draw the specify canvas background color.
            Paint background = new Paint();
            background.setColor(Color.BLACK);
            canvas.drawRect(fond, background);

            // Choose the brush color for drawing
            paint.setColor(Color.argb(255, 255, 255, 255));
            paint.setTextSize(160);

            // Draw the score
            if(score < 10){
                canvas.drawText(String.valueOf("0"+score), (int)(screenWidth / 2.4), screenHeight / 10, paint);
            }else canvas.drawText(String.valueOf(score), (int)(screenWidth / 2.4), screenHeight / 10, paint);

            // Draw life
            if(lives == 1){
                paint.setTextSize(90);
                paint.setColor(Color.RED);
                canvas.drawText(String.valueOf(lives), screenWidth - 150, screenHeight / 11, paint);
            }else{
                paint.setTextSize(90);
                canvas.drawText(String.valueOf(lives), screenWidth - 150, screenHeight / 11, paint);
            }

            // Draw the paddle
            paint.setColor(Color.WHITE);
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
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {

        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            // Player has touched the screen
            case MotionEvent.ACTION_DOWN:
                paused = false;
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

    public void pause() {
        threadRunning = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            Log.e("Error:", "joining thread");
        }
    }

    public void start() {
        // Create the child thread when SurfaceView is created.
        thread = new Thread(this);

        // Start to run the child thread.
        thread.start();

        // Set thread running flag to true.
        threadRunning = true;

        screenHeight = getHeight();
        screenWidth = getWidth();
    }
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        start();
        // Get screen width and height.

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // Set thread running flag to false when Surface is destroyed.
        // Then the thread will jump out the while loop and complete.
        pause();
    }
}