package com.nypsdm.dx1221_week04;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.app.Activity;
import android.view.ViewDebug;
import android.widget.Button;

import androidx.fragment.app.FragmentActivity;

import java.util.Arrays;
import java.util.Random;

public class NextPage extends FragmentActivity
{
    public static NextPage Instance = null;

    private Button btn_back;
    private Button btn_create;
    private Button btn_basic;
    private Button btn_skill;
    private Button btn_ultimate;
    private Button btn_cycle;

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
}
