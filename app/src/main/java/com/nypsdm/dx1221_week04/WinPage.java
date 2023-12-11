package com.nypsdm.dx1221_week04;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

public class WinPage extends Activity implements View.OnClickListener, StateBase
{
    public static WinPage Instance = null;

    private Button btn_menu;

    protected boolean _active = true;
    // Boolean use to check for whether the page is active and running

    protected int _splashTime = 5000;

    @Override
    public String GetName()
    {
        return "WinPage";
    }

    @Override
    public void OnEnter(SurfaceView _view)
    {
        // 3. Create Background

        // Example to include another Renderview for Pause Button
    }

    @Override
    public void OnExit()
    {
        // 4. Clear any instance instantiated via EntityManager.

        // 5. Clear or end any instance instantiated via GamePage.
    }

    @Override
    public void Render(Canvas _canvas)
    {
        EntityManager.Instance.Render(_canvas, 0, 0);
    }

    @Override
    public void Update(float _dt)
    {
        EntityManager.Instance.Update(_dt);

        if (TouchManager.Instance.IsDown())
        {

            //6. Example of touch on screen in the main game to trigger back to Main menu

        }
    }

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.winpage);

        btn_menu = (Button) findViewById(R.id.btn_menu);
        btn_menu.setOnClickListener((View.OnClickListener) this);

        Thread gameThread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;

                    while (_active && (waited < _splashTime)) {
                        sleep(200);

                        if (_active) {
                            waited += 200;
                        }
                    }
                } catch (InterruptedException e) {
                    // Do Nothing
                } finally {
                    finish();

                    // Create new activity based on an intent with CurrentActivity
                    Intent intent = new Intent(WinPage.this, MainMenu.class);
                    startActivity(intent);
                }
            }
        };

        gameThread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if (event.getAction() == MotionEvent.ACTION_DOWN)
        {
            _active = false;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();

        if (v == btn_menu)
        {
            intent.setClass(this, MainMenu.class);
            StateManager.Instance.ChangeState("MainMenu");
        }

        startActivity(intent);
    }
}
