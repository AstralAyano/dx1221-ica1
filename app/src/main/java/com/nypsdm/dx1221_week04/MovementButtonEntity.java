package com.nypsdm.dx1221_week04;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceView;

public class MovementButtonEntity implements EntityBase
{
    static SmurfEntity player;
    public TileMapEntity tileMap;
    private boolean isDone = false;
    private boolean isInit = false;

    // We have 2 different pause images so that we can create the effect of a button press and there is a change in the images.
    private Bitmap bmpLeft = null;
    private Bitmap bmpRight = null;
    private Bitmap bmpJump = null;

    // Scaled version of the buttons.
    private Bitmap ScaledbmpLeft = null;
    private Bitmap ScaledbmpRight = null;
    private Bitmap ScaledbmpJump = null;

    private int xPos, yPos = 0;
    private boolean hasMoved;

    private int ScreenWidth, ScreenHeight = 0;

    private boolean Paused = false;

    private float buttonDelay = 0;

    static float x;
    static float y;

    private boolean left;

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
        x = MainGameSceneState.camera.GetX();
        // indicate the images to be used.
        // Load the images.
        bmpLeft = ResourceManager.Instance.GetBitmap(R.drawable.arrowleft);
        bmpRight = ResourceManager.Instance.GetBitmap(R.drawable.arrowright);
        bmpJump = ResourceManager.Instance.GetBitmap(R.drawable.arrowup);

        DisplayMetrics metrics = _view.getResources().getDisplayMetrics();
        ScreenWidth = metrics.widthPixels;
        ScreenHeight = metrics.heightPixels;

        ScaledbmpLeft = Bitmap.createScaledBitmap(bmpLeft, ScreenWidth / 15, ScreenWidth / 15, true);
        ScaledbmpRight = Bitmap.createScaledBitmap(bmpRight, ScreenWidth / 15, ScreenWidth / 15, true);
        ScaledbmpJump = Bitmap.createScaledBitmap(bmpJump, ScreenWidth / 15, ScreenWidth / 15, true);

        // Position the button. As of now, it is default fix number.
        // You can use the screen width and height as a basis.
        xPos = 200;
        yPos = ScreenHeight - 200;
        player.xVelocity = 0;
        player.yVelocity = 0;

        isInit = true;
    }

    @Override
    public void Update(float _dt) {

        buttonDelay += _dt;  // Button press effect. Not a critical thingy.

        if (TouchManager.Instance.HasTouch()) {
            float leftButtonRadius = ScaledbmpLeft.getHeight() * 0.5f;
            float rightButtonRadius = ScaledbmpRight.getHeight() * 0.5f;
            float jumpButtonRadius = ScaledbmpJump.getHeight() * 0.5f;

            if (Collision.SphereToSphere((TouchManager.Instance.GetPosX()), TouchManager.Instance.GetPosY(), 0.0f, xPos, yPos, leftButtonRadius))
            {
                if (!player.touchingWall)
                {
                    player.xVelocity = 200;
                    x += player.xVelocity * _dt;
                    left = true;
                    hasMoved = true;
                }
            }
            else if (Collision.SphereToSphere((TouchManager.Instance.GetPosX()), TouchManager.Instance.GetPosY(), 0.0f, xPos + 300, yPos, rightButtonRadius))
            {
                if (!player.touchingWall)
                {
                    player.xVelocity = 200;
                    x -= player.xVelocity * _dt;
                    left = false;
                    hasMoved = true;
                }
            }
            if (player.touchingWall && hasMoved)
            {
                if (left)
                {
                    x -= player.xVelocity * _dt;
                }
                else
                {
                    x += player.xVelocity * _dt;
                }
                player.touchingWall = false;
                player.xVelocity = 0;
                Log.d("Debug", "TouchingWall : False");
                hasMoved = false;
            }
            MainGameSceneState.camera.SetPosition(x, MainGameSceneState.camera.GetY());

            if (Collision.SphereToSphere((TouchManager.Instance.GetPosX()), TouchManager.Instance.GetPosY(), 0.0f, ScreenWidth - 200, yPos, jumpButtonRadius))
            {
                // Jump
                player.jump = true;
            }
        }
        else
        {

        }

//        if (MainGameSceneState.camera.collision)
//        {
//            Log.d("Debug", "Inside");
//            player.yPos -= yVelocity * _dt;
//            if (yVelocity != 0)
//            {
//                yVelocity = 0;
//            }
//        }

        y = player.y;

        tileMap.SetPosition(x, y);
    }

    @Override
    public void Render(Canvas _canvas, float x, float y) {
        _canvas.drawBitmap(ScaledbmpLeft, xPos - ScaledbmpLeft.getWidth() * 0.5f, yPos - ScaledbmpLeft.getHeight() * 0.5f, null);
        _canvas.drawBitmap(ScaledbmpRight, (xPos + 300) - ScaledbmpRight.getWidth() * 0.5f, yPos - ScaledbmpRight.getHeight() * 0.5f, null);
        _canvas.drawBitmap(ScaledbmpJump, (ScreenWidth - 200) - ScaledbmpJump.getWidth() * 0.5f, yPos - ScaledbmpJump.getHeight() * 0.5f, null);
    }

    @Override
    public boolean IsInit() {
        return isInit;
    }

    @Override
    public int GetRenderLayer() {
        return LayerConstants.UI_LAYER;
    }

    @Override
    public void SetRenderLayer(int _newLayer) {
        return;
    }

    public static MovementButtonEntity Create() {
        MovementButtonEntity result = new MovementButtonEntity();
        EntityManager.Instance.AddEntity(result, ENTITY_TYPE.ENT_BUTTON);
        return result;
    }

    @Override
    public EntityBase.ENTITY_TYPE GetEntityType() {
        return EntityBase.ENTITY_TYPE.ENT_BUTTON;
    }

    public static void SetEntity(SmurfEntity entity)
    {
        player = entity;
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
