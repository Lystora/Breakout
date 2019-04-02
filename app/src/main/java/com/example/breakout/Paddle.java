package com.example.breakout;

import android.graphics.RectF;
import android.view.SurfaceView;

public class Paddle {
    private RectF rect;
    private float length;
    private float height;
    private float x;
    private float y;

    private float paddleSpeed;

    // Which ways can the paddle move
    public final int STOPPED = 0;
    public final int LEFT = 1;
    public final int RIGHT = 2;

    private int paddleMoving = STOPPED;

    public Paddle(int screenX, int screenY){
        this.length = 230;
        this.height = 30;

        // Start paddle in roughly the screen centre
        this.x = screenX /2;
        this.y = screenY - 200;

        this.rect = new RectF(this.x, this.y, this.x + this.length, this.y + this.height);

        this.paddleSpeed = 25;
    }

    public RectF getRect(){
        return this.rect;
    }

    // This method will be used to change/set if the paddle is going left, right or nowhere
    public void setMovementState(int state){
        this.paddleMoving = state;
    }

    // This method determines if the paddle needs to move and changes the coordinates
    // contained in rect if necessary
    public void update(SurfaceView p){
        if(this.paddleMoving == this.LEFT && this.x > 0){
            this.x = this.x - paddleSpeed;
        }

        if(this.paddleMoving == this.RIGHT && this.x + this.length < p.getWidth()){
            this.x = this.x + this.paddleSpeed;
        }

        this.rect.left = this.x;
        this.rect.right = this.x + this.length;
    }

}
