package com.nypsdm.dx1221_week04;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.DisplayMetrics;
import android.view.SurfaceView;

// Whole script done by Bernard Ng
public class ButtonUltimateEntity implements EntityBase
{
    public MainCombatSceneState combatScene;
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

    private boolean Pressed = false;

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
        bmpP = ResourceManager.Instance.GetBitmap(R.drawable.button_ultimate);
        bmpP1 = ResourceManager.Instance.GetBitmap(R.drawable.button_ultimate_pressed);

        DisplayMetrics metrics = _view.getResources().getDisplayMetrics();
        ScreenWidth = metrics.widthPixels;
        ScreenHeight = metrics.heightPixels;

        ScaledbmpP = Bitmap.createScaledBitmap(bmpP, 235, 85, true);
        ScaledbmpP1 = Bitmap.createScaledBitmap(bmpP1, 235, 85, true);

        // Position the button. As of now, it is default fix number.
        // You can use the screen width and height as a basis.
        xPos = ScreenWidth / 2 + 500;
        yPos = (ScreenHeight / 4) * 3 + 50;

        isInit = true;
    }

    @Override
    public void Update(float _dt) {

        buttonDelay += _dt;  // Button press effect. Not a critical thingy.

        if (TouchManager.Instance.HasTouch()) {
            if (TouchManager.Instance.IsDown() && !Pressed) {

                // Check Collision of the button here!!
                float imgRadius = ScaledbmpP.getHeight() * 0.5f;

                if (Collision.AABBCollision(TouchManager.Instance.GetPosX(), TouchManager.Instance.GetPosY(), 0, 0, xPos, yPos, 235, 85) && buttonDelay >= 0.25) {
                    Pressed = true;

                    // Functionality here (idk if you need "Pressed" boolean
                    // find player
                    int i = combatScene.EntityInArray(combatScene.currPlace);
                    //ultimate is charged
                    if (combatScene.p[i].Energy == 100)
                    {
                        combatScene.DoUltimate(i);
                    }
                }
                buttonDelay = 0;
            }
        } else
            Pressed = false;

    }

    @Override
    public void Render(Canvas _canvas, float x, float y) {
        if (Pressed == false)
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
        return LayerConstants.BUTTON_LAYER;
    }

    @Override
    public void SetRenderLayer(int _newLayer) {
        return;
    }

    public static ButtonUltimateEntity Create() {
        ButtonUltimateEntity result = new ButtonUltimateEntity();
        EntityManager.Instance.AddEntity(result, ENTITY_TYPE.ENT_BUTTON);
        return result;
    }

    @Override
    public ENTITY_TYPE GetEntityType() {
        return ENTITY_TYPE.ENT_BUTTON;
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
