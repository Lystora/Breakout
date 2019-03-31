package com.example.breakout;

import android.graphics.RectF;

public class Brique {

    private int x,y,width,height;
    private RectF rect;
    private boolean isVisible;
    private int padding = 5;

    public Brique(int x, int y, int width, int height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        this.rect = new RectF(this.x * this.width + padding,
                this.y * this.height + padding,
                this.x * this.width + this.width - padding,
                this.y * this.height + this.height - padding);

        this.isVisible = true;
    }

    public int getX(){return this.x;}
    public int getY(){return this.y;}
    public int getWidth(){return this.width;}
    public int getHeight(){return this.height;}
    public int getPadding(){return this.padding;}
    public RectF getRect(){
        return this.rect;
    }
    public boolean getVisibility(){return this.isVisible;}

    public void setX(int x){this.x = x;}
    public void setY(int y){this.y = y;}
    public void setWidth(int width){this.width = width;}
    public void setHeight(int height){this.height = height;}
    public void setPadding(int p){this.padding = p;}
    public void setVisibility(boolean v){this.isVisible = v;}
    public void setInvisible(){
        this.isVisible = false;
    }
}

