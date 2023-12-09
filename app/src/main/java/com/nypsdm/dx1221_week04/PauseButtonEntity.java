package com.nypsdm.dx1221_week04;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.view.SurfaceView;

public class PauseButtonEntity implements EntityBase
{
    private boolean isDone = false;
    private boolean isInit = false;

    // We have 2 different pause images so that we can create the effect of a button press and there is a change in the images.
    private Bitmap bmpP = null;
    private Bitmap bmpP1 = null;

    // Scaled version of the buttons.
    private Bitmap ScaledbmpP = null;
    private Bitmap ScaledbmpP1 = null;

    private int xPos, yPos = 0;

    private int ScreenWidth, ScreenHeight = 0;

    private boolean Paused = false;

    private float buttonDelay = 0;


    @Override
    public boolean IsDone() {
        return isDone;
    }

    @Override
    public void SetIsDone(boolean _isDone) {
        isDone = _isDone;
    }

    @Override
    public void Init(SurfaceView _view) {
        // indicate the images to be used.
        // Load the images.
        bmpP = ResourceManager.Instance.GetBitmap(R.drawable.pause);
        bmpP1 = ResourceManager.Instance.GetBitmap(R.drawable.pause1);

        DisplayMetrics metrics = _view.getResources().getDisplayMetrics();
        ScreenWidth = metrics.widthPixels;
        ScreenHeight = metrics.heightPixels;

        ScaledbmpP = Bitmap.createScaledBitmap(bmpP, ScreenWidth / 20, ScreenWidth / 20, true);
        ScaledbmpP1 = Bitmap.createScaledBitmap(bmpP, ScreenWidth / 20, ScreenWidth / 20, true);

        // Position the button. As of now, it is default fix number.
        // You can use the screen width and height as a basis.
        xPos = ScreenWidth - 175;
        yPos = 175;

        isInit = true;
    }

    @Override
    public void Update(float _dt) {

        buttonDelay += _dt;  // Button press effect. Not a critical thingy.

        if (TouchManager.Instance.HasTouch()) {
            if (TouchManager.Instance.IsDown() && !Paused) {

                // Check Collision of the button here!!
                float imgRadius = ScaledbmpP.getHeight() * 0.5f;

                if (Collision.SphereToSphere((TouchManager.Instance.GetPosX()), TouchManager.Instance.GetPosY(), 0.0f, xPos, yPos, imgRadius) && buttonDelay >= 0.25) {
                    Paused = true;

                    if (PauseConfirmDialogFragment.IsShown) {
                        return;
                    }

                    PauseConfirmDialogFragment newPause = new PauseConfirmDialogFragment();
                    newPause.show(GamePage.Instance.getSupportFragmentManager(), "PauseConfirm");
                }
                buttonDelay = 0;
            }
        } else
            Paused = false;

    }

    @Override
    public void Render(Canvas _canvas, float x, float y) {
        if (Paused == false)
            _canvas.drawBitmap(ScaledbmpP, xPos - ScaledbmpP.getWidth() * 0.5f, yPos - ScaledbmpP.getHeight() * 0.5f, null);
        else
            _canvas.drawBitmap(ScaledbmpP1, xPos - ScaledbmpP.getWidth() * 0.5f, yPos - ScaledbmpP.getHeight() * 0.5f, null);
    }

    @Override
    public boolean IsInit() {
        return isInit;
    }

    @Override
    public int GetRenderLayer() {
        return LayerConstants.PAUSE_LAYER;
    }

    @Override
    public void SetRenderLayer(int _newLayer) {
        return;
    }

    public static PauseButtonEntity Create() {
        PauseButtonEntity result = new PauseButtonEntity();
        EntityManager.Instance.AddEntity(result, ENTITY_TYPE.ENT_PAUSE);
        return result;
    }

    @Override
    public ENTITY_TYPE GetEntityType() {
        return ENTITY_TYPE.ENT_PAUSE;
    }

    @Override
    public float GetPosX() {
        return 0;
    }

    @Override
    public float GetPosY() {
        return 0;
    }
}
