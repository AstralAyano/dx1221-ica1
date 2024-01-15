package com.nypsdm.dx1221_week04;

import android.util.Log;

public abstract class Character
{
    private int HP, MAXHP;
    private int ATK;
    private int SPD;
    public int placeInTurn;
    public String type;

    //Change all stats
    public void ChangeStats(int hp, int atk, int spd)
    {
        HP = hp;
        ATK = atk;
        SPD = spd;
    }

    //Get & Set for stats
    public void SetHP(int hp)
    {
        HP = hp;
    }

    public int GetHP()
    {
        return HP;
    }

    public void SetMHP(int hp)
    {
        MAXHP = hp;
    }

    public int GetMHP()
    {
        return MAXHP;
    }

    public void SetATK(int atk)
    {
        ATK = atk;
    }

    public int GetATK()
    {
        return ATK;
    }

    public void SetSPD(int spd)
    {
        SPD = spd;
    }

    public int GetSPD()
    {
        return SPD;
    }
}
