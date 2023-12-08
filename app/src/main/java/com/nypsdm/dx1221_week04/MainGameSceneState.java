package com.nypsdm.dx1221_week04;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceView;

// Created by TanSiewLan2021

public class MainGameSceneState implements StateBase {
    private Camera camera;
    private SurfaceView view;
    private SmurfEntity smurfEntity;

    private float timer = 0.0f;

    @Override
    public String GetName() {
        return "MainGame";
    }

    @Override
    public void OnEnter(SurfaceView _view)
    {
        camera = new Camera();
        view = _view;

        // 3. Create Background
        RenderBackground.Create();

        // Render TileSet and TileMap
        int[][] tileMap = {
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0},
                {0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0},
                {0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0},
                {0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0},
                {0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
        };

        // Assume you have a single image for the tileset
        Bitmap tileSetImage = ResourceManager.Instance.GetBitmap(R.drawable.tileset);

        // Set the width and height of each tile in pixels
        int tileWidth = 64;
        int tileHeight = 64;

        TileMapEntity tileMapEntity = new TileMapEntity(tileMap, tileWidth, tileHeight, tileSetImage);
        EntityManager.Instance.AddEntity(tileMapEntity, EntityBase.ENTITY_TYPE.ENT_DEFAULT);

        // Add more entities
        smurfEntity = SmurfEntity.Create();

        MovementButtonEntity.Create();

        PauseButtonEntity.Create();

        RenderTextEntity.Create();
        // Example to include another Renderview for Pause Button
    }

    @Override
    public void OnExit()
    {
        // 4. Clear any instance instantiated via EntityManager.
        EntityManager.Instance.Clean();

        // 5. Clear or end any instance instantiated via GamePage.
        GamePage.Instance.finish();
    }

    @Override
    public void Render(Canvas _canvas)
    {
        // Calculate the camera offset based on the camera's position
        float cameraOffsetX = camera.GetX();
        float cameraOffsetY = camera.GetY();

        // Render entities with the camera offset
        EntityManager.Instance.Render(_canvas, cameraOffsetX, cameraOffsetY);
    }

    @Override
    public void Update(float _dt)
    {
        if (smurfEntity != null) {
            // Calculate the desired camera position based on the center of the screen
            float targetCameraOffsetX = smurfEntity.GetPosX() - view.getWidth() / 2;
            float targetCameraOffsetY = smurfEntity.GetPosY() - view.getHeight() / 2;

            // Smoothly interpolate the camera position towards the target
            float smoothingFactor = 0.1f;  // Adjust this value for the desired level of smoothness
            float cameraOffsetX = camera.GetX() + (targetCameraOffsetX - camera.GetX()) * smoothingFactor;
            float cameraOffsetY = camera.GetY() + (targetCameraOffsetY - camera.GetY()) * smoothingFactor;

            // Set the camera position
            camera.SetPosition(cameraOffsetX, cameraOffsetY);
        }

        EntityManager.Instance.Update(_dt);

        if (TouchManager.Instance.IsDown())
        {
			//6. Example of touch on screen in the main game to trigger back to Main menu
            //StateManager.Instance.ChangeState("MainMenu");
        }
    }
}



