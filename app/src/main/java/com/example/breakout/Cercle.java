package com.example.breakout;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceView;

import java.util.Random;

public class Cercle{
    int diametre,x,y;
    int speed = 0;
    int Xspeed = 0;
    int Yspeed = 0;
    private Color couleur;
    boolean backX = false;
    boolean backY = false;

    public Cercle(int x, int y, int r, int s) {
        super();
        this.x = x/2;
        this.y = y-200;
        this.diametre = r/2;
        this.Xspeed = s;
        this.Yspeed = s;
    }
    public int getSpeed(){return this.speed;}
    public int getXSpeed(){return this.Xspeed;}
    public int getYSpeed(){return this.Yspeed;}
    public int getX() {return this.x;}
    public int getY() {return this.y;}

    public void setSpeed(int s){this.speed +=s;}
    public void setXSpeed(int s){this.Xspeed +=s;}
    public void setYSpeed(int s){this.Yspeed +=s;}
    public void setX(int x) {this.x = x;}
    public void setY(int y) {this.y = y;}

    public int getDiametre() {return this.diametre;}
    public Color getColor() {return this.couleur;}

    public void setDiametre(int r) {this.diametre = r;}
    public void setColor(Color c) {this.couleur = c;}

    public void draw(Canvas c) {
        Paint p = new Paint();
        p.setColor(Color.RED);
        c.drawCircle(x, y, diametre, p);
    }
    public void reverseYVelocity(){ this.Yspeed = -this.Yspeed;}
    public void reverseXVelocity(){
        this.Xspeed = -this.Xspeed;
    }

    public void setRandomXVelocity(){
        Random generator = new Random();
        int answer = generator.nextInt(2);

        if(answer == 0){
            reverseXVelocity();
        }
    }

    public void clearObstacleY(int y){this.y = (y - getDiametre()*2);}

    public void clearObstacleX(int x){this.x = (x + getDiametre()*2);}
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
        if (!backX) {setX(x+=Xspeed);}
        else setX(x-=Xspeed);

        // Idem pour l'axe Y
        if (!backY) {setY(y+=Yspeed);}
        else setY(y-=Yspeed);
    }
}