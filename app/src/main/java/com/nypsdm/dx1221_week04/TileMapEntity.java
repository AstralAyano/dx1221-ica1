package com.nypsdm.dx1221_week04;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;

public class TileMapEntity implements EntityBase
{
    private int[][] tileMap; // Represents the layout of the tilemap
    private int tileWidth; // Width of each tile in pixels
    private int tileHeight; // Height of each tile in pixels
    private Bitmap tileSet; // Single image containing all tiles
    private int numRows; // Number of rows in the tilemap
    private int numCols; // Number of columns in the tilemap

    private ArrayList<TileEntity> tiles = new ArrayList<TileEntity>();

    private boolean _isInit;
    private Canvas canvas;
    public float x, y;

    DisplayMetrics metrics;

    public TileMapEntity(int[][] tileMap, int tileWidth, int tileHeight, Bitmap tileset)
    {
        this.tileMap = tileMap;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.tileSet = tileset;

        // Add checks to ensure tileMap dimensions are valid
        if (tileMap == null || tileMap.length == 0 || tileMap[0].length == 0)
        {
            throw new IllegalArgumentException("Invalid tileMap dimensions");
        }

        this.numRows = tileMap.length;
        this.numCols = tileMap[0].length;
    }

    @Override
    public void Render(Canvas _canvas, float _x, float _y)
    {
        for (int row = 0; row < numRows; row++)
        {
            for (int col = 0; col < numCols; col++)
            {
                int tileIndex = tileMap[row][col];

                // Calculate the position to render the tile
                int xPos = col * tileWidth;
                int yPos = row * tileHeight;

                int index = (numCols * row + col);

                tiles.get(index).xPos = xPos;
                tiles.get(index).yPos = yPos;

                if (xPos > metrics.widthPixels || yPos > metrics.heightPixels || xPos < 0 || yPos < 0)
                {
                    continue;
                }

                // Calculate the source rectangle to extract the tile from the tileset image
                int srcX = (tileIndex % (tileSet.getWidth() / 64)) * 64;
                int srcY = (tileIndex / (tileSet.getWidth() / 64)) * 64;
                Rect srcRect = new Rect(srcX, srcY, srcX + 64, srcY + 64);

                // Draw the tile on the canvas
                tiles.get(index).RenderTile(canvas, tileSet, srcRect, new Rect((int)x + xPos, (int)y + yPos, (int)x + xPos + tileWidth, (int)y + yPos + tileHeight));
            }
        }
    }

    @Override
    public void Update(float dt)
    {
        // Update logic for the tilemap, if needed
    }

    @Override
    public boolean IsDone() { return false; }

    @Override
    public void SetIsDone(boolean _isDone) {  }

    @Override
    public void Init(SurfaceView _view)
    {
        metrics = _view.getResources().getDisplayMetrics();

        for (int row = 0; row < numRows; row++)
        {
            for (int col = 0; col < numCols; col++)
            {
                int tileIndex = tileMap[row][col];

                // Skip rendering if the tile index is invalid
                if (tileIndex >= 0)
                {
                    TileEntity newTile = TileEntity.Create(false, this);

                    // Calculate the position to render the tile
                    int xPos = col * tileWidth;
                    int yPos = row * tileHeight;

                    newTile.xPos = xPos;
                    newTile.yPos = yPos;

                    // Calculate the source rectangle to extract the tile from the tileset image
                    int srcX = (tileIndex % (tileSet.getWidth() / 64)) * 64;
                    int srcY = (tileIndex / (tileSet.getWidth() / 64)) * 64;
                    Rect srcRect = new Rect(srcX, srcY, srcX + 64, srcY + 64);

                    tiles.add(newTile);

                    // Draw the tile on the canvas
                    newTile.RenderTile(canvas, tileSet, srcRect, new Rect((int)x + xPos, (int)y + yPos, (int)x + xPos + tileWidth, (int)y + yPos + tileHeight));
                }
                else
                {
                    TileEntity newTile = TileEntity.Create(true, this);

                    tiles.add(newTile);
                }
            }
        }

        _isInit = true;
    }

    public void SetPosition(float _x, float _y)
    {
        x = _x;
        y = _y;
    }

    @Override
    public boolean IsInit() { return _isInit; }

    @Override
    public int GetRenderLayer() { return 0; }

    @Override
    public void SetRenderLayer(int _newLayer) {   }

    @Override
    public ENTITY_TYPE GetEntityType() { return ENTITY_TYPE.ENT_DEFAULT; }

    @Override
    public float GetPosX() { return x; }

    @Override
    public float GetPosY() {
        return y;
    }
}