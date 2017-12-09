package com.javaparser.model;

public class DragContext {

    double posX;
    double posY;

    public DragContext() {
        posX = 0;
        posY = 0;
    }

    public double getPosX() {
        return posX;
    }

    public double getPosY() {
        return posY;

    }

    public void setPosX(double posX) {
        this.posX = posX;
    }

    public void setPosY(double posY) {
        this.posY = posY;
    }
}
