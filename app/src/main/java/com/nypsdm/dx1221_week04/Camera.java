package com.nypsdm.dx1221_week04;

// Whole script done by Bernard Ng
public class Camera {
    private float x;
    private float y;

    public Camera() {
        this.x = 0;
        this.y = 0;
    }

    public void SetPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float GetX() {
        return x;
    }

    public float GetY() {
        return y;
    }
}
