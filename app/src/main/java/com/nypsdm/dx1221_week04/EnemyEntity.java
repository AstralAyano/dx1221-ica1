package com.nypsdm.dx1221_week04;

import android.graphics.Canvas;
import android.view.SurfaceView;

public class EnemyEntity implements EntityBase {

    private boolean isDone = false;
    private boolean isInit = false;

    private float xPos, yPos; // Current position of the enemy
    private float speed = 200.0f; // Movement speed
    private float direction = 1.0f; // Movement direction (1.0f for right, -1.0f for left)
    private float width, height; // Dimensions of the enemy bounding box

    @Override
    public boolean IsDone() { return isDone; }

    @Override
    public void SetIsDone(boolean _isDone) {
        isDone = _isDone;
    }

    @Override
    public void Init(SurfaceView _view) {
        // Initialize enemy scale and position
        width = 50.0f;
        height = 50.0f;
        xPos = 100.0f;
        yPos = 300.0f;
    }

    @Override
    public void Update(float _dt) {
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
        xPos += speed * direction * _dt;
    }

    @Override
    public void Render(Canvas _canvas, float x, float y) {
        // This is for our sprite animation!

    }

    @Override
    public boolean IsInit() { return isInit; }

    @Override
    public int GetRenderLayer() { return LayerConstants.ENEMY_LAYER; }

    @Override
    public void SetRenderLayer(int _newLayer) { return; }

    @Override
    public ENTITY_TYPE GetEntityType() { return ENTITY_TYPE.ENT_ENEMY; }

    @Override
    public float GetPosX() { return xPos; }

    @Override
    public float GetPosY() { return yPos; }

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

    public static EnemyEntity Create() {
        EnemyEntity result = new EnemyEntity();
        EntityManager.Instance.AddEntity(result, ENTITY_TYPE.ENT_ENEMY);
        return result;
    }
}