package com.nypsdm.dx1221_week04;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.SurfaceView;

// Whole script done by Bernard Ng
public class RenderEnemyHPTextEntity implements EntityBase
{
    // Paint Object
    Canvas canvas;
    Paint paint = new Paint();

    // Variable to be used to set colors
    private int red = 255, green = 255, blue = 255; // Colors range from 0 to 255

    // We want to use our own font type,
    protected Typeface myfont;

    public int enemy = 0;
    public int hp = 0;
    public float xPos, yPos;

    private boolean isDone = false;
    private boolean isInit = false;

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
        // Load font
        myfont = Typeface.createFromAsset(_view.getContext().getAssets(), "ChalkPaint.otf");

        isInit = true;
    }

    @Override
    public void Update(float _dt) {
        // Get the FPS
        /*long currenttime = System.currentTimeMillis();
        long lastTime = currenttime;
        if (currenttime - lastFPSTime > 1000){
            fps = (frameCount * 1000) / (currenttime - lastFPSTime);
            lastFPSTime = currenttime;
            frameCount = 0;
        }
        frameCount++;*/
    }

    @Override
    public void Render(Canvas _canvas, float x, float y) {
        paint.setARGB(255, red, green, blue); // U can put direct numbers here ranging from 0 to 255. If red = 255, it is red, 0 = black.
        // 255, 255 ,0, 0 -- red color
        paint.setTypeface(myfont);  // load the font we want using the font type.
        paint.setTextSize(60); // Font size we want.

        _canvas.drawText(Integer.toString(hp), xPos, yPos, paint);
    }

    public void SetPos(float x, float y)
    {
        xPos = x;
        yPos = y;
    }

    public void SetHealthText(int _hp)
    {
        hp = _hp;
    }

    @Override
    public boolean IsInit() {
        return isInit;
    }

    @Override
    public int GetRenderLayer(){
        return LayerConstants.TEXT_LAYER;
    }

    @Override
    public void SetRenderLayer(int _newLayer)
    {
        return;
    }

    public static RenderEnemyHPTextEntity Create()
    {
        RenderEnemyHPTextEntity result = new RenderEnemyHPTextEntity();
        EntityManager.Instance.AddEntity(result, ENTITY_TYPE.ENT_TEXT);
        return result;
    }

    @Override
    public ENTITY_TYPE GetEntityType(){return ENTITY_TYPE.ENT_TEXT;}

    @Override
    public float GetPosX() {
        return 0;
    }

    @Override
    public float GetPosY() {
        return 0;
    }
}
