package com.nypsdm.dx1221_week04;

import android.view.KeyEvent;

// Whole script done by Bernard Ng
public class KeyboardManager
{
    private static KeyboardManager instance;

    private int lastKeyCode = KeyEvent.KEYCODE_UNKNOWN;
    private boolean hasInput = false;

    private KeyboardManager()
    {
        // Private constructor to enforce singleton pattern
    }

    public static synchronized KeyboardManager getInstance()
    {
        if (instance == null)
        {
            instance = new KeyboardManager();
        }
        return instance;
    }

    public void handleKeyEvent(KeyEvent event) {
        lastKeyCode = event.getKeyCode();
        hasInput = true;
    }

    public boolean HasInput() {
        return hasInput;
    }

    public int GetKeyCode() {
        hasInput = false; // Consume the input
        return lastKeyCode;
    }
}
