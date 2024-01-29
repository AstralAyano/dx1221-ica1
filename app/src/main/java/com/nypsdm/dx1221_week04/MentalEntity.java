package com.nypsdm.dx1221_week04;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceView;

public class MentalEntity implements EntityBase, Collidable {
    public Sprite spritesheet = null; // Define.
    private SurfaceView view;

    private boolean isDone = false;
    private boolean isInit = false;

    private float xPos, yPos; // Current position of the enemy
    private float originXPos, originYPos;
    private float enemyXPos, enemyYPos;

    private boolean startMovement = false, returnMovement = false;
    private float timer = 0;

    @Override
    public boolean IsDone() { return isDone; }

    @Override
    public void SetIsDone(boolean _isDone) {
        isDone = _isDone;
    }

    @Override
    public void Init(SurfaceView _view) {
        view = _view;

        // New method using our own resource manager : Returns pre-loaded one if exists
        // 2. Loading spritesheet
        spritesheet = new Sprite(ResourceManager.Instance.GetBitmap(R.drawable.mental), 1, 4, 30);

        xPos = _view.getWidth() / 4 + 200;
        yPos = _view.getHeight() / 2 + 150;

        originXPos = xPos;
        originYPos = yPos;

        isInit = true;

        // To Set the Animation Frames
        spritesheet.SetAnimationFrames(0,3);
    }

    public void AttackEnemy(float enemyX, float enemyY)
    {
        startMovement = true;
        enemyXPos = enemyX;
        enemyYPos = enemyY;
    }

    @Override
    public void Update(float _dt) {
        // Update spritesheet
        spritesheet.Update(_dt);

        if (startMovement)
        {
            if (xPos <= enemyXPos && !returnMovement)
            {
                xPos += 800 * _dt;
            }
            else
            {
                returnMovement = true;
            }

            if (returnMovement)
            {
                timer += _dt;

                if (timer > 0.5f)
                {
                    if (xPos >= originXPos)
                    {
                        xPos -= 800 * _dt;
                    }
                    else
                    {
                        xPos = originXPos;
                        timer = 0;
                        startMovement = false;
                        returnMovement = false;
                    }
                }
            }
        }
    }

    @Override
    public void Render(Canvas _canvas, float x, float y) {
        // This is for our sprite animation!
        spritesheet.Render(_canvas, (int)xPos, (int)yPos);
    }

    @Override
    public boolean IsInit() { return isInit; }

    @Override
    public int GetRenderLayer() { return LayerConstants.PLAYER_LAYER; }

    @Override
    public void SetRenderLayer(int _newLayer) { return; }

    public static MentalEntity Create() {
        MentalEntity result = new MentalEntity();
        EntityManager.Instance.AddEntity(result, ENTITY_TYPE.ENT_HEALTH);
        return result;
    }

    @Override
    public ENTITY_TYPE GetEntityType() { return ENTITY_TYPE.ENT_HEALTH; }

    @Override
    public String GetType() {
        return "MentalEntity";
    }

    @Override
    public float GetPosX() { return xPos; }

    @Override
    public float GetPosY() { return yPos; }

    @Override
    public float GetRadius() {
        return 64;
    }

    @Override
    public float GetWidth() {
        return 128;
    }

    @Override
    public float GetHeight() {
        return 128;
    }

    @Override
    public void OnHit(Collidable _other) { }
}