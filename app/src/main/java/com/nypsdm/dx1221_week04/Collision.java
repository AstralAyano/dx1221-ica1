package com.nypsdm.dx1221_week04;

// Created by TanSiewLan2021

public class Collision {

    public static boolean SphereToSphere(float x1, float y1, float radius1, float x2, float y2, float radius2)
    {
        float xVec = x2 - x1;
        float yVec = y2 - y1;

        float distSquared = xVec * xVec + yVec * yVec;

        float rSquared = radius1 + radius2;
        rSquared *= rSquared;

        if (distSquared > rSquared)
            return false;

        return true;
    }

    // Whole script done by Kodey Chin
    public static boolean AABBCollision(float x1, float y1, float width1, float height1, float x2, float y2, float width2, float height2)
    {
        float xScale1 = width1 / 2;
        float yScale1 = height1 / 2;
        float xScale2 = width2 / 2;
        float yScale2 = height2 / 2;


        if ((x1 - x2) * (x1 - x2) < (xScale1 + xScale2) * (xScale1 + xScale2)) // find distance squared and check
        {
            // check top collision
            if (y1 + yScale1 > y2 - yScale2 && y1 < y2)
            {
                return true;
            }
            // check bottom collision
            else if (y1 - yScale1 < y2 + yScale2 && y1 > y2)
            {
                return true;
            }
        }
        if ((y1 - y2) * (y1 - y2) < (yScale1 + yScale2) * (yScale1 + yScale2)) // find distance squared and check
        {
            // check left collision
            if (x1 + xScale1 > x2 - xScale2 && x1 < x2)
            {
                return true;
            }
            // check right collision
            else if (x1 - xScale1 < x2 + xScale2 && x1 > x2)
            {
                return true;
            }
        }

        return false;
    }
}
