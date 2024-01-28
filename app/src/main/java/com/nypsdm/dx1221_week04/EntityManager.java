package com.nypsdm.dx1221_week04;

import android.graphics.Canvas;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceView;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

// Created by TanSiewLan2021

public class EntityManager {

    public final static EntityManager Instance = new EntityManager();
    private LinkedList<EntityBase> entityList = new LinkedList<EntityBase>();
    private SurfaceView view = null;
    private Camera camera; // Add a private member variable for the camera

    DisplayMetrics metrics;

    private static final EntityManager instance = new EntityManager();

    public static EntityManager Instance() {
        return instance;
    }

    // ... (other existing methods)

    // Add a method to get the camera
    public Camera GetCamera() {
        return camera;
    }

    private EntityManager()
    {
        camera = new Camera();
    }

    public void Init(SurfaceView _view)
    {
        metrics = _view.getResources().getDisplayMetrics();
        view = _view;
    }

    public void Update(float _dt)
    {
        LinkedList<EntityBase> removalList = new LinkedList<EntityBase>();

        // Update all
        for(int i = 0; i < entityList.size(); ++i)
        {
            // Lets check if is init, initialize if not
            if (!entityList.get(i).IsInit())
            {
                entityList.get(i).Init(view);
            }

            entityList.get(i).Update(_dt);

            // Check if need to clean up
            if (entityList.get(i).IsDone()) {
                // Done! Time to add to the removal list
                removalList.add(entityList.get(i));
            }
        }

        // Remove all entities that are done
        for (EntityBase currEntity : removalList) {
            entityList.remove(currEntity);
        }
        removalList.clear(); // Clean up of removal list

        // Collision Check
        for (int i = 0; i < entityList.size(); ++i)
        {
            EntityBase currEntity = entityList.get(i);

            if (currEntity instanceof Collidable)
            {
                Collidable first = (Collidable) currEntity;

                if (first.GetPosX() > metrics.widthPixels || first.GetPosY() > metrics.heightPixels || first.GetPosX() < 0 || first.GetPosY() < 0)
                {
                    continue;
                }

                for (int j = i + 1; j < entityList.size(); ++j)
                {
                    EntityBase otherEntity = entityList.get(j);

                    if (otherEntity instanceof Collidable)
                    {
                        Collidable second = (Collidable) otherEntity;

                        if (second.GetPosX() > metrics.widthPixels || second.GetPosY() > metrics.heightPixels || second.GetPosX() < 0 || second.GetPosY() < 0)
                        {
                            continue;
                        }

                        //if (Collision.SphereToSphere(first.GetPosX(), first.GetPosY(), first.GetRadius(), second.GetPosX(), second.GetPosY(), second.GetRadius()))
                        if (Collision.AABBCollision(first.GetPosX(), first.GetPosY(), first.GetWidth(), first.GetHeight(), second.GetPosX(), second.GetPosY(), second.GetWidth(), second.GetHeight()))
                        {
                            first.OnHit(second);
                            second.OnHit(first);
                        }
                    }
                }
            }

            // Check if need to clean up
            if (currEntity.IsDone()) {
                removalList.add(currEntity);
            }
        }

        // Remove all entities that are done
        for (EntityBase currEntity : removalList) {
            entityList.remove(currEntity);
        }
        removalList.clear();
    }

    public void Render(Canvas _canvas, float _cameraOffsetX, float _cameraOffsetY)
    {
      
        // Use the new "rendering layer" to sort the render order
        Collections.sort(entityList, new Comparator<EntityBase>() {
            @Override
            public int compare(EntityBase o1, EntityBase o2) {
                return o1.GetRenderLayer() - o2.GetRenderLayer();
            }
        });

        for (int i = 0; i < entityList.size(); ++i) {
            EntityBase entity = entityList.get(i);

            // Use the camera offset when rendering entities
            float transformedX = entity.GetPosX() - _cameraOffsetX;
            float transformedY = entity.GetPosY() - _cameraOffsetY;

            entity.Render(_canvas, transformedX, transformedY);
        }
    }

    public void AddEntity(EntityBase _newEntity, EntityBase.ENTITY_TYPE entity_type)
    {
        entityList.add(_newEntity);
    }

    public void Clean()
    {
        entityList.clear();
    }

    public int GetEntityCount()
    {
        return entityList.size();
    }
}


