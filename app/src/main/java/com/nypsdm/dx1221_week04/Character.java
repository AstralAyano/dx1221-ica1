package com.nypsdm.dx1221_week04;

import android.util.Log;

public abstract class Character
{
    private int HP, MAXHP;
    private int ATK;
    private int SPD;
    public int placeInTurn;
    public String type, healthType;

    //Change all stats
    public void ChangeStats(int hp, int atk, int spd)
    {
        HP = hp;
        ATK = atk;
        SPD = spd;
    }

    public void TakeDamage(int i, String type)
    {
        // check typing for weakness and resistance (x2 and /2 dmg respectively)
        switch (type)
        {
            case "PHY":
                if (healthType == "PHY")
                {
                    SetHP(GetHP() - i);
                }
                else if (healthType == "MEN")
                {
                    SetHP(GetHP() - i / 2);
                }
                else if (healthType == "EMO")
                {
                    SetHP(GetHP() - i * 2);
                }
                break;
            case "MEN":
                if (healthType == "PHY")
                {
                    SetHP(GetHP() - i * 2);
                }
                else if (healthType == "MEN")
                {
                    SetHP(GetHP() - i);
                }
                else if (healthType == "EMO")
                {
                    SetHP(GetHP() - i / 2);
                }
                break;
            case "EMO":
                if (healthType == "PHY")
                {
                    SetHP(GetHP() - i / 2);
                }
                else if (healthType == "MEN")
                {
                    SetHP(GetHP() - i * 2);
                }
                else if (healthType == "EMO")
                {
                    SetHP(GetHP() - i);
                }
                break;
        }
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

    public String GetHT()
    {
        return healthType;
    }
}
