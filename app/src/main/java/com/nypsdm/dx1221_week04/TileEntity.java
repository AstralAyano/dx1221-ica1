package com.nypsdm.dx1221_week04;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.SurfaceView;

import androidx.constraintlayout.helper.widget.Layer;

// Whole script done by Bernard Ng
public class TileEntity implements EntityBase, Collidable
{
    public TileMapEntity tileMap;
    public Canvas canvas;
    public Bitmap tileSet;
    public Rect srcRect;
    public Rect newRect;
    public int tileSize = 256;

    private boolean _isDone;
    private boolean _isInit;
    public boolean _isEmpty;

    float xPos, yPos;

    @Override
    public String GetType() {
        return "TileEntity";
    }

    @Override
    public float GetRadius() {
        return tileSize;
    }

    @Override
    public float GetWidth() {
        return tileSize;
    }

    @Override
    public float GetHeight() {
        return tileSize;
    }

    @Override
    public void OnHit(Collidable _other) {

    }

    @Override
    public boolean IsDone() { return _isDone; }

    @Override
    public void SetIsDone(boolean _isDone) {
        this._isDone = _isDone;
    }

    @Override
    public void Init(SurfaceView _view) {

        _isInit = true;
    }

    @Override
    public void Update(float _dt) {
        //Log.d("TileEntity", Float.toString(TileMapEntity.x));
    }

    @Override
    public void Render(Canvas _canvas, float x, float y)
    {
        if (!_isEmpty)
        {
            _canvas.drawBitmap(tileSet, srcRect, new Rect((int)GetPosX(), (int)GetPosY(), (int)GetPosX() + tileSize, (int)GetPosY() + tileSize), null);
        }
    }

    @Override
    public boolean IsInit() {
        return _isInit;
    }

    @Override
    public int GetRenderLayer() {
        return LayerConstants.TILE_LAYER;
    }

    @Override
    public void SetRenderLayer(int _newLayer) {

    }

    public static TileEntity Create(boolean isEmpty, TileMapEntity _tileMap)
    {
        TileEntity result = new TileEntity();
        result._isEmpty = isEmpty;
        result.tileMap = _tileMap;
        EntityManager.Instance.AddEntity(result, ENTITY_TYPE.ENT_TILE);
        return result;
    }

    public void RenderTile(Canvas _canvas, Bitmap _tileSet, Rect _srcRect, Rect _newRect)
    {
        canvas = _canvas;
        tileSet = _tileSet;
        srcRect = _srcRect;
        newRect = _newRect;
    }

    @Override
    public ENTITY_TYPE GetEntityType() {
        return ENTITY_TYPE.ENT_TILE;
    }

    @Override
    public float GetPosX() {
        return xPos + tileMap.GetPosX();
    }

    @Override
    public float GetPosY() {
        return yPos + tileMap.GetPosY();
    }
}
