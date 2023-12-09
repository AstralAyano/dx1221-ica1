package com.nypsdm.dx1221_week04;

// Created by TanSiewLan2021

public interface Collidable {
    String GetType();

    float GetPosX();
    float GetPosY();
    float GetRadius();
    float GetWidth();
    float GetHeight();

    void OnHit(Collidable _other);
}

