package com.nypsdm.dx1221_week04;

// Created by TanSiewLan2023
// Create a GamePage is an activity class used to hold the GameView which will have a surfaceview

import androidx.fragment.app.FragmentActivity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceView;

// extends FragmentActivity
public class GamePage extends FragmentActivity
{
    public static GamePage Instance = null;

    protected boolean _active = true;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Instance = this;

        setContentView(new GameView(this)); // Surfaceview = GameView
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        // WE are hijacking the touch event into our own system
        int x = (int) event.getX();
        int y = (int) event.getY();

        TouchManager.Instance.Update(x, y, event.getAction());

        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        KeyboardManager.getInstance().handleKeyEvent(event);
        return super.onKeyDown(keyCode, event);
    }

    public void ChangeToCombat()
    {
        Intent intent = new Intent();
        intent.setClass(this, NextPage.class);
        startActivity(intent);
    }

}

