package com.nypsdm.dx1221_week04;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;

public class SplashPage extends Activity
{
    protected boolean _active = true;
    // Boolean use to check for whether the page is active and running

    protected int _splashTime = 5000;
    // meant for wait so after something the splashpage will auto transit to the main menu

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashpage);
        // Create the view from splashpage.xml

        Thread splashThread = new Thread()
        {
            @Override
            public void run()
            {
                try
                {
                    int waited = 0;

                    while (_active && (waited < _splashTime))
                    {
                        sleep(200);

                        if (_active)
                        {
                            waited += 200;
                        }
                    }
                }
                catch (InterruptedException e)
                {
                    // Do Nothing
                }
                finally
                {
                    finish();

                    // Create new activity based on an intent with CurrentActivity
                    Intent intent = new Intent(SplashPage.this, MainMenu.class);
                    startActivity(intent);
                }
            }
        };

        splashThread.start();
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
    // Boolean active = false = DON'T want this view anymore = Go to next screen
}
