package com.example.breakout;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceView;

public class Cercle{
    int diametre,x,y;
    int speed = 0;
    private Color couleur;
    boolean backX = false;
    boolean backY = false;

    public Cercle(int x, int y, int r, int s) {
        super();
        this.x = x;
        this.y = y;
        this.diametre = r/2;
        this.speed = s;
    }
    public int getSpeed(){return speed;}
    public int getX() {return x;}
    public int getY() {return y;}

    public void setSpeed(int s){this.speed +=s;}
    public void setX(int x) {this.x = x;}
    public void setY(int y) {this.y = y;}

    public int getDiametre() {return diametre;}
    public Color getColor() {return couleur;}

    public void setDiametre(int r) {diametre = r;}
    public void setColor(Color c) {couleur = c;}

    public void draw(Canvas c) {
        Paint p = new Paint();
        p.setColor(Color.RED);
        c.drawCircle(x, y, diametre, p);
    }
    public void reverseYVelocity(){
        int y = getY();int x = getX();
        speed = -speed;
    }
    public void  move(SurfaceView pan) {
        int x = getX(); int y = getY();
        // Si la coordonnée x est inférieure à 1, on avance.
        // Si la coordonnée x est supérieure à la taille du Panneau moins la taille du rond, on recule
        if (x < 1+getDiametre()) backX = false;
        if (x > pan.getWidth() - getDiametre()) backX = true;

        // Idem pour l'axe y
        if (y < 1+getDiametre()) backY = false;
        if (y > pan.getHeight() - getDiametre()) backY = true;

        // Si on avance, on incrémente la coordonnée
        if (!backX) {setX(x+=speed);}
        else setX(x-=speed);

        // Idem pour l'axe Y
        if (!backY) {setY(y+=speed);}
        else setY(y-=speed);
    }
}