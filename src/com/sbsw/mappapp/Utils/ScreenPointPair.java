package com.sbsw.mappapp.Utils;

/**
 * Created by reed on 12/12/14.
 * Represents a set of screen coordinates
 */
public class ScreenPointPair {
    private float x;
    private float y;

    public  ScreenPointPair(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
