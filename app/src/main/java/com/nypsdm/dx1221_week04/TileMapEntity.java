package com.nypsdm.dx1221_week04;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.SurfaceView;

public class TileMapEntity implements EntityBase
{
    private int[][] tileMap; // Represents the layout of the tilemap
    private int tileWidth; // Width of each tile in pixels
    private int tileHeight; // Height of each tile in pixels
    private Bitmap tileSet; // Single image containing all tiles
    private int numRows; // Number of rows in the tilemap
    private int numCols; // Number of columns in the tilemap

    public TileMapEntity(int[][] tileMap, int tileWidth, int tileHeight, Bitmap tileset)
    {
        this.tileMap = tileMap;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.tileSet = tileset;
        this.numRows = tileMap.length;
        this.numCols = tileMap[0].length;
    }

    @Override
    public void Render(Canvas canvas, float x, float y)
    {
        for (int row = 0; row < numRows; row++)
        {
            for (int col = 0; col < numCols; col++)
            {
                int tileIndex = tileMap[row][col];

                // Skip rendering if the tile index is invalid
                if (tileIndex >= 0)
                {
                    // Calculate the position to render the tile
                    int xPos = col * tileWidth;
                    int yPos = row * tileHeight;

                    // Calculate the source rectangle to extract the tile from the tileset image
                    int srcX = (tileIndex % (tileSet.getWidth() / tileWidth)) * tileWidth;
                    int srcY = (tileIndex / (tileSet.getWidth() / tileWidth)) * tileHeight;
                    Rect srcRect = new Rect(srcX, srcY, srcX + tileWidth, srcY + tileHeight);

                    // Draw the tile on the canvas
                    canvas.drawBitmap(tileSet, srcRect, new Rect((int)x + xPos, (int)y + yPos, (int)x + xPos + tileWidth, (int)y + yPos + tileHeight), null);
                }
            }
        }
    }

    @Override
    public void Update(float dt) {
        // Update logic for the tilemap, if needed
    }

    // Implement methods from BaseEntity
    @Override
    public boolean IsDone() {
        // Provide your implementation here
        return false;
    }

    @Override
    public void SetIsDone(boolean _isDone) {
        // Provide your implementation here
    }

    @Override
    public void Init(SurfaceView _view) {
        // Provide your implementation here
    }

    @Override
    public boolean IsInit() {
        // Provide your implementation here
        return false;
    }

    @Override
    public int GetRenderLayer() {
        // Provide your implementation here
        return 0;
    }

    @Override
    public void SetRenderLayer(int _newLayer) {
        // Provide your implementation here
    }

    @Override
    public ENTITY_TYPE GetEntityType() {
        // Provide your implementation here
        return ENTITY_TYPE.ENT_DEFAULT;
    }

    @Override
    public float GetPosX() {
        return 0;
    }

    @Override
    public float GetPosY() {
        return 0;
    }
}