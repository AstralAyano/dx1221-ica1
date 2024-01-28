package com.nypsdm.dx1221_week04;

import android.graphics.Canvas;
import android.view.SurfaceView;

public class CombatEnemyEntity implements EntityBase, Collidable {
    public Sprite spritesheet = null; // Define.
    private SurfaceView view;

    private boolean isDone = false;
    private boolean isInit = false;

    private float xPos, yPos; // Current position of the enemy

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
        spritesheet = new Sprite(ResourceManager.Instance.GetBitmap(R.drawable.physical), 1, 4, 30);

        xPos = _view.getWidth() / 4;
        yPos = _view.getHeight() / 2 + 150;

        isInit = true;

        // To Set the Animation Frames
        spritesheet.SetAnimationFrames(0,3);
    }

    public void SetPos(int x, int y)
    {
        xPos = view.getWidth() / 4 * 3 + x;
        yPos = view.getHeight() / 2 + y;
    }

    @Override
    public void Update(float _dt) {
        // Update spritesheet
        spritesheet.Update(_dt);
    }

    @Override
    public void Render(Canvas _canvas, float x, float y) {
        // This is for our sprite animation!
        spritesheet.Render(_canvas, (int)xPos, (int)yPos);
    }

    @Override
    public boolean IsInit() { return isInit; }

    @Override
    public int GetRenderLayer() { return LayerConstants.ENEMY_LAYER; }

    @Override
    public void SetRenderLayer(int _newLayer) { return; }

    public static CombatEnemyEntity Create() {
        CombatEnemyEntity result = new CombatEnemyEntity();
        EntityManager.Instance.AddEntity(result, ENTITY_TYPE.ENT_ENEMY);
        return result;
    }

    @Override
    public ENTITY_TYPE GetEntityType() { return ENTITY_TYPE.ENT_ENEMY; }

    @Override
    public String GetType() {
        return "CombatEnemyEntity";
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