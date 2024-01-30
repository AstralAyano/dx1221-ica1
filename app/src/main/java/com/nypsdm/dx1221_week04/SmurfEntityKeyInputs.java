package com.nypsdm.dx1221_week04;

import android.view.KeyEvent;

// Whole script done by Bernard Ng
public class SmurfEntityKeyInputs
{
    private static final float MOVEMENT_SPEED = 10.0f;

    public static void handleKeyEvent(int keyCode, SmurfEntity smurfEntity)
    {
        if (smurfEntity == null)
        {
            return;
        }

        switch (keyCode)
        {
            case KeyEvent.KEYCODE_A:
                smurfEntity.xPos -= MOVEMENT_SPEED;
                break;
            case KeyEvent.KEYCODE_D:
                smurfEntity.xPos += MOVEMENT_SPEED;
                break;
        }
    }
}
