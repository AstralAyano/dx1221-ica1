package com.nypsdm.dx1221_week04;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.service.quicksettings.Tile;
import android.util.Log;
import android.view.SurfaceView;

import androidx.activity.result.contract.ActivityResultContracts;

import java.util.Random;

public class SmurfEntity implements EntityBase, Collidable{

    // 1. Declare the use of spritesheet using Sprite class.
    public Bitmap bmp = null; // Usual method of loading a bmp/image
    public Sprite spritesheet = null; // Define.
    private SurfaceView view;

    public static Player[] p;

    private boolean isDone = false;
    private boolean isInit = false;

    // Variables to be used or can be used.
    public float xPos, yPos, xDir, yDir, lifeTime;
    public float xVelocity, yVelocity;
    public boolean onGround, touchingWall;
    public boolean jump, jumping;
    public boolean touchingEnemy;
    public float imgWidth, imgHeight;

    // For use with the TouchManager.class
    private boolean hasTouched = false;

    int ScreenWidth, ScreenHeight;

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
        view = _view;

        // New method using our own resource manager : Returns pre-loaded one if exists
        // 2. Loading spritesheet
        spritesheet = new Sprite(ResourceManager.Instance.GetBitmap(R.drawable.playerspritesheet), 7, 4, 28);

        // 3. Get some random position of x and y
        Random ranGen = new Random(); // Random generator under the java utility library

        xPos = _view.getWidth() / 2;
        yPos = _view.getHeight() / 2;

        isInit = true;

        // To Set the Animation Frames
        spritesheet.SetAnimationFrames(0,4);

        MovementButtonEntity.SetEntity(this);

        // create players
        p = new Player[3];
        p[0] = new Player("Physical", 20, 2, 6, 1);
        p[1] = new Player("Mental", 10, 4, 3, 2);
        p[2] = new Player("Emotional", 15, 2, 4, 2);
        // sort based on spd
        for (int k = 0; k < p.length; k++)
        {
            for (int i = 0; i < p.length; i++)
            {
                if (i + 1 < p.length) {
                    if (p[i].GetSPD() < p[i + 1].GetSPD())
                    {
                        Player temp = p[i];
                        p[i] = p[i + 1];
                        p[i + 1] = temp;
                    }
                }
            }
        }
    }

    @Override
    public void Update(float _dt)
    {
        // Pause
        if (GameSystem.Instance.GetIsPaused())
            return;

        // 4. Update spritesheet
        spritesheet.Update(_dt);

        if (KeyboardManager.getInstance().HasInput())
        {
            int keyCode = KeyboardManager.getInstance().GetKeyCode();
            SmurfEntityKeyInputs.handleKeyEvent(keyCode, this);
        }

        // gravity
        if (onGround && !jump)
        {
            jumping = false;
            yPos -= yVelocity * _dt;
            yVelocity = 0;
        }

        if (yVelocity < 200 && !onGround)
        {
            yVelocity += 200 * _dt;
            jump = false;
        }

        // jump
        if (jump && !jumping)
        {
            jumping = true;
            yVelocity = -400;
        }

        yPos += yVelocity * _dt;
    }

    @Override
    public void Render(Canvas _canvas, float x, float y) {

        // This is for our sprite animation!
        spritesheet.Render(_canvas, (int)xPos, (int)yPos);
    }

    @Override
    public boolean IsInit() {
        return isInit;
    }

    @Override
    public int GetRenderLayer(){
        return LayerConstants.SMURF_LAYER;
    }

    @Override
    public void SetRenderLayer(int _newLayer)
    {
        return;
    }

    public static SmurfEntity Create()
    {
        SmurfEntity result = new SmurfEntity();
        EntityManager.Instance.AddEntity(result, ENTITY_TYPE.ENT_SMURF);
        return result;
    }

    @Override
    public ENTITY_TYPE GetEntityType(){return ENTITY_TYPE.ENT_SMURF;}

    @Override
    public String GetType() {
        return "SmurfEntity";
    }

    @Override
    public float GetPosX() {
        return xPos;
    }

    @Override
    public float GetPosY() {
        return yPos;
    }

    public float GetXDir() {
        return xDir;
    }

    public float GetYDir() {
        return yDir;
    }

    public float GetRadius() {
        return 64;
    }

    @Override
    public float GetWidth() {
        return 64;
    }

    @Override
    public float GetHeight() {
        return 64;
    }

    @Override
    public void OnHit(Collidable _other) {
        // This allows you to check collision between 2 entities.
        // Star Entity can cause harm to the player when hit.
        // If hit by star, you can play an audio, or have a visual feedback or
        // physical feedback.
        // SetIsDone(true) --> allows you to delete the entity from the screen.

        if (_other.GetType() == "TileEntity") //Another Entity
        {
            //collide with ground
            TileEntity tileEntity = _other instanceof TileEntity ? ((TileEntity ) _other) : null;
            //Log.d("Debug", "Collided with TileEntity");
            if (!tileEntity._isEmpty && getAngle(tileEntity.xPos, tileEntity.yPos) <= 180)
            {
                onGround = true;
            }
            else
            {
                onGround = false;
                //Log.d("Debug", "OnGround : False");
            }

            // collide with wall
            if (!tileEntity._isEmpty && getAngle(tileEntity.xPos, tileEntity.yPos) > 180)
            {
                touchingWall = true;
                Log.d("Debug", "TouchingWall : True");
            }
        }

        if (_other.GetType() == "EnemyEntity" && !touchingEnemy)
        {
            touchingEnemy = true;
            Log.d("Collision", "SmurfEntity collided with EnemyEntity");
            Vibrator.VibrateOneShot(150, 10);
            GamePage.Instance.ChangeToCombat();
        }
        else
        {
            touchingEnemy = false;
        }
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
