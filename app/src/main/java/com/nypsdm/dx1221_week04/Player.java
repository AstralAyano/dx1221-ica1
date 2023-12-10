package com.nypsdm.dx1221_week04;

import android.util.Log;

import java.util.Random;

public class Player extends Character
{
    public int healCount;
    public String Name;

    Player(String name, int hp, int atk, int spd, int NoOfHeals)
    {
        Name = name;
        ChangeStats(hp, atk, spd);
        type = "player";
        healCount = NoOfHeals;
        Log.d("test", "player created");
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
