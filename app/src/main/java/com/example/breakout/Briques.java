package com.example.breakout;

import android.graphics.RectF;

public class Briques {
    private RectF rect;

    private boolean isVisible;

    public Briques(int ligne, int colonne, int width, int height){

        isVisible = true;

        int padding = 5;

        rect = new RectF(colonne * width + padding,
                ligne * height + padding,
                colonne * width + width - padding,
                ligne * height + height - padding);
    }
    public RectF getRect(){
        return this.rect;
    }

    public void setInvisible(){
        isVisible = false;
    }

    public boolean getVisibility(){
        return isVisible;
    }
}