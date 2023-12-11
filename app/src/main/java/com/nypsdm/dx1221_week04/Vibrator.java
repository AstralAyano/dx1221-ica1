package com.nypsdm.dx1221_week04;

import android.os.VibrationEffect;
import android.os.VibratorManager;
import android.util.Log;

public class Vibrator
{
    private static android.os.Vibrator _vibrator;

    private static boolean _hasVibrator;

    public static void Initialize(android.os.Vibrator vibrator)
    {
        _vibrator = vibrator;
        _hasVibrator = vibrator.hasVibrator();

        if (!_hasVibrator)
        {
            Log.d("Debug", "No vibrator detected");
        }
    }


    public static void VibrateOneShot(long milliseconds, int amplitude)
    {
        if (_hasVibrator)
        {
            _vibrator.vibrate(VibrationEffect.createOneShot(milliseconds, amplitude));
        }
    }
}
