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

        TileMapEntity tileMapEntity = null;

        // Render TileSet and TileMap
        try
        {
            List<int[]> tileMapList = CSVReader.ReadCSVFile(view.getContext(), "level1.csv");

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
                tileWidth = 256;
                tileHeight = 256;

                tileMapEntity = new TileMapEntity(tileMap, tileWidth, tileHeight, tileSetImage);
                EntityManager.Instance.AddEntity(tileMapEntity, EntityBase.ENTITY_TYPE.ENT_DEFAULT);
            }
            else
            {
                Log.e("Debug", "TileMapList : Empty");
            }

        } catch (Exception e)
        {
            // Log any exception during initialization
            e.printStackTrace();
        }

        // Add more entities
        SmurfEntity.Create();
        EnemyEntity.Create();

        MovementButtonEntity moveEntity = MovementButtonEntity.Create();
        moveEntity.tileMap = tileMapEntity;

        PauseButtonEntity.Create();

        RenderTextEntity.Create();

        Vibrator.Initialize((android.os.Vibrator)_view.getContext().getSystemService(_view.getContext().VIBRATOR_SERVICE));
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
    }
}



