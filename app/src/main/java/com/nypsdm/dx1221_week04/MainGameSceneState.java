package com.nypsdm.dx1221_week04;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceView;

import java.util.Arrays;
import java.util.List;

// Created by TanSiewLan2021

public class MainGameSceneState implements StateBase {
    public static Camera camera;
    private SurfaceView view;
    private SmurfEntity smurfEntity;

    private float timer = 0.0f;

    private List<int[]> map;
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
        try
        {
            List<int[]> tileMapList = CSVReader.readCSVFile(view.getContext(), "level1.csv");

            // Check if the list is not empty
            if (!tileMapList.isEmpty())
            {
                // Determine the number of columns based on the first row
                int colCount = tileMapList.get(0).length;

                // Convert the list to a 2D array
                int[][] tileMap = new int[tileMapList.size()][colCount];

                for (int i = 0; i < tileMapList.size(); i++)
                {
                    tileMap[i] = tileMapList.get(i);
                }

                map = tileMapList;

                // Log tileMap values for debugging
                for (int i = 0; i < tileMap.length; i++)
                {
                    Log.d("TileMapRow", Arrays.toString(tileMap[i]));
                }

                Bitmap tileSetImage = ResourceManager.Instance.GetBitmap(R.drawable.tileset);

                // Set the width and height of each tile in pixels
                int tileWidth = 64;
                int tileHeight = 64;

                TileMapEntity tileMapEntity = new TileMapEntity(tileMap, tileWidth, tileHeight, tileSetImage);
                EntityManager.Instance.AddEntity(tileMapEntity, EntityBase.ENTITY_TYPE.ENT_DEFAULT);
            } else
            {
                Log.e("Debug", "TileMapList : Empty");
            }

        } catch (Exception e)
        {
            // Log any exception during initialization
            e.printStackTrace();
        }



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

    private void CheckCollisions(List<int[]> map)
    {
        if (map == null) {
            // Handle the case where map is null
            Log.e("CheckCollisions", "map is null");
            return;
        }

        int[] col;
        camera.verticalCollision = false;
        camera.horizontalCollision = false;

        for (int r = 0; r < map.size(); r++)
        {
            for (int c = 0; c < map.get(r).length; c++)
            {
                if (map.get(r)[c] >= 0)
                {
                    col = Collision.AABBCollision((c * tileWidth - tileWidth / 2) - camera.GetX(), (r * tileHeight - tileHeight / 2) - camera.GetY(), tileWidth, tileHeight, smurfEntity.xPos, smurfEntity.yPos, smurfEntity.imgWidth, smurfEntity.imgHeight);
                    if (col[0] == 1 || col[1] == 1)
                    {
                        camera.verticalCollision = true;
                    }
                    if (col[2] == 1 || col[3] == 1)
                    {
                        camera.horizontalCollision = true;
                    }
                }
            }
        }
    }
}



