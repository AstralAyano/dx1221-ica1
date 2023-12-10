package com.nypsdm.dx1221_week04;

import android.util.Log;

import java.util.Random;

public class Enemy extends Character
{
    private Random rand = new Random();
    int randInt;
    public String Name;

    Enemy(String name, int hp, int atk, int spd)
    {
        Name = name;
        ChangeStats(hp, atk, spd);
        type = "enemy";
        Log.d("test", "enemy created");
        Log.d("test", "");
    }

    public void TakeDamage(int i)
    {
        SetHP(GetHP() - i);
    }

    public void PrintStats(int place)
    {
        placeInTurn = place;
        Log.d("test", "<" + Name + ">");
        Log.d("test", "HP/ATK/SPD: " + GetHP() + "/" + GetATK() + "/" + GetSPD());
        Log.d("test", "");
    }
}
