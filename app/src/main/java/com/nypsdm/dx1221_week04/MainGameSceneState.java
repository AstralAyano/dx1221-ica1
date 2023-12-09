package com.nypsdm.dx1221_week04;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceView;

// Created by TanSiewLan2021

public class MainGameSceneState implements StateBase {
    public static Camera camera;
    private SurfaceView view;
    private SmurfEntity smurfEntity;

    private float timer = 0.0f;

    private int[][] map;
    private int tileWidth;
    private int tileHeight;

    @Override
    public String GetName() {
        return "MainGame";
    }

    @Override
    public void OnEnter(SurfaceView _view)
    {
        camera = EntityManager.Instance.GetCamera();
        view = _view;

        //camera.SetPosition(-_view.getWidth() / 2, -_view.getHeight() / 2);
        camera.SetPosition(0, 0);

        // 3. Create Background
        RenderBackground.Create();

        // Render TileSet and TileMap
        int[][] tileMap = {
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0},
                {0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0},
                {0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0},
                {0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0},
                {0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0},
                {0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0},
                {0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0},
                {0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0},
                {0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0},
                {0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0},
                {0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0},
                {0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0},
                {0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0},
                {0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0},
                {0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0},
                {0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0},
                {0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0},
                {0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0},
                {0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0, -1, -1, -1, -1, -1, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
        };
        map = tileMap;

        // Assume you have a single image for the tileset
        Bitmap tileSetImage = ResourceManager.Instance.GetBitmap(R.drawable.tileset);

        // Set the width and height of each tile in pixels
        tileWidth = 64;
        tileHeight = 64;

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


        EntityManager.Instance.Update(_dt);

        if (TouchManager.Instance.IsDown())
        {
			//6. Example of touch on screen in the main game to trigger back to Main menu
            //StateManager.Instance.ChangeState("MainMenu");
        }
        CheckCollisions(map);
    }

    private void CheckCollisions(int[][] map)
    {
        int[] col;
        camera.verticalCollision = false;
        camera.horizontalCollision = false;

        for (int r = 0; r < map.length; r++)
        {
            for (int c = 0; c < map[r].length; c++)
            {
                if (map[r][c] >= 0)
                {
                    col = Collision.AABBCollision((c * tileWidth - tileWidth / 2) - camera.GetX(), (r * tileHeight - tileHeight / 2) - camera.GetY(), tileWidth, tileHeight, smurfEntity.xPos, smurfEntity.yPos, smurfEntity.imgWidth, smurfEntity.imgHeight);
                    if (col[0] == 1 || col[1] == 1)
                    {
                        camera.verticalCollision = true;
                    }if (col[2] == 1 || col[3] == 1)
                    {
                        camera.horizontalCollision = true;
                    }
                }
            }
        }
    }
}



