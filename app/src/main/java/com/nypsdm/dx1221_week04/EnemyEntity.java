package com.nypsdm.dx1221_week04;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceView;

import java.util.Random;

public class EnemyEntity implements EntityBase, Collidable {
    public Bitmap bmp = null; // Usual method of loading a bmp/image
    public Sprite spritesheet = null; // Define.
    private SurfaceView view;

    private boolean isDone = false;
    private boolean isInit = false;

    private float xPos, yPos; // Current position of the enemy
    private float speed = 200.0f; // Movement speed
    private float direction = 1.0f; // Movement direction (1.0f for right, -1.0f for left)
    private float width, height; // Dimensions of the enemy bounding box


    public float yVelocity;
    public boolean onGround, touchingWall;

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
        spritesheet = new Sprite(ResourceManager.Instance.GetBitmap(R.drawable.enemy1), 5, 4, 20);

        // 3. Get some random position of x and y
        Random ranGen = new Random(); // Random generator under the java utility library

        xPos = (int)MovementButtonEntity.x + _view.getWidth() / 2 - 150;
        yPos = _view.getHeight() / 4;

        isInit = true;

        // To Set the Animation Frames
        spritesheet.SetAnimationFrames(16,19);
    }

    @Override
    public void Update(float _dt) {
        // Update spritesheet
        spritesheet.Update(_dt);

        // Update logic for enemy movement

        // Check for collisions with tiles
        if (CheckTileCollisions()) {
            // Bounces the enemy back if it collides with tiles
            direction *= -1.0f;
        }

        // Check for collisions with SmurfEntity
        if (CheckSmurfEntityCollision()) {
            // Trigger combat scene or any desired action
            // CombatManager.Instance.TriggerCombat(); ??
        }

        // Update enemy position based on speed and direction
        //xPos += speed * direction * _dt;

        // gravity
        if (onGround)
        {
            yPos -= yVelocity * _dt;
            yVelocity = 0;
        }

        if (yVelocity < 200 && !onGround)
        {
            yVelocity += 200 * _dt;
        }

        yPos += yVelocity * _dt;
    }

    @Override
    public void Render(Canvas _canvas, float x, float y) {
        // This is for our sprite animation!
        spritesheet.Render(_canvas, (int)MovementButtonEntity.x + (int)xPos, (int)yPos);
    }

    @Override
    public boolean IsInit() { return isInit; }

    @Override
    public int GetRenderLayer() { return LayerConstants.ENEMY_LAYER; }

    @Override
    public void SetRenderLayer(int _newLayer) { return; }

    public static EnemyEntity Create() {
        EnemyEntity result = new EnemyEntity();
        EntityManager.Instance.AddEntity(result, ENTITY_TYPE.ENT_ENEMY);
        return result;
    }

    @Override
    public ENTITY_TYPE GetEntityType() { return ENTITY_TYPE.ENT_ENEMY; }

    @Override
    public String GetType() {
        return "EnemyEntity";
    }

    @Override
    public float GetPosX() { return (int)MovementButtonEntity.x + xPos; }

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
    public void OnHit(Collidable _other) {

        if (_other.GetType() == "SmurfEntity")
        {
            Log.d("Collision", "EnemyEntity collided with SmurfEntity");
        }

        TileEntity tileEntity = _other instanceof TileEntity ? ((TileEntity ) _other) : null;

        if (_other.GetType() == "TileEntity") //Another Entity
        {
            //collide with ground
            //Log.d("Debug", "Collided with TileEntity");
            if (!tileEntity._isEmpty && getAngle(tileEntity.xPos, tileEntity.yPos) <= 180)
            {
                onGround = true;
            }
            else
            {
                onGround = false;
            }

            // collide with wall
            if (!tileEntity._isEmpty && getAngle(tileEntity.xPos, tileEntity.yPos) > 180)
            {
                touchingWall = true;
            }
        }
    }

    private boolean CheckTileCollisions() {
        // Implement collision check with tiles
        // Return true if there is a collision, false otherwise
        return false;
    }

    private boolean CheckSmurfEntityCollision() {
        // Implement collision check with SmurfEntity
        // Return true if there is a collision, false otherwise
        return false;
    }

    public float getAngle(float x, float y)
    {
        float angle = (float) Math.toDegrees(Math.atan2(y - yPos, x - xPos));

        if(angle < 0)
        {
            angle += 360;
        }

        return angle;
    }
}