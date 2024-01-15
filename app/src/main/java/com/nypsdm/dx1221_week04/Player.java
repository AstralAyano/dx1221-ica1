package com.nypsdm.dx1221_week04;

import android.util.Log;

import java.util.Random;

public class Player extends Character
{
    public String Name;
    public int Energy;

    Player(String name, int hp, int atk, int spd)
    {
        Name = name;
        ChangeStats(hp, atk, spd);
        SetMHP(GetHP());
        type = "player";
        Energy = 0;
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
        Log.d("test", "HP(MAX)/ATK/SPD: " + GetHP() + "(" + GetMHP() + ")/" + GetATK() + "/" + GetSPD());
        Log.d("test", "");
    }

    public void BasicAttack(Enemy enemy)
    {
        // hit an enemy for atk
        enemy.TakeDamage(GetATK());
        // gain energy
        Energy += GetATK() * 10;
        if (Energy > 100)
        {
            Energy = 100;
        }
    }

    public void UseSkill(Player[] players, Enemy[] enemies, int target)
    {
        switch (Name)
        {
            case "Physical":
                // hit an enemy for atk * 3
                enemies[target].TakeDamage(GetATK() * 3);
                // gain energy
                Energy += GetATK() * 12;
                if (Energy > 100)
                {
                    Energy = 100;
                }

                break;

            case "Emotional":

                Player temp = this;
                // finds player with the largest difference in hp and max hp
                for (int i = 0; i < players.length; i++)
                {
                    if (temp.GetMHP() - temp.GetHP() < players[i].GetMHP() - players[i].GetHP())
                    {
                        temp = players[i];
                    }
                }
                // gain energy
                Energy += (temp.GetMHP() - temp.GetHP()) * 7;
                if (Energy > 100)
                {
                    Energy = 100;
                }
                // heal all players by 4, up to max hp
                int newHP = temp.GetHP() + 4;

                if (newHP >= temp.GetMHP())
                {
                    temp.SetHP(temp.GetMHP());
                }
                else
                {
                    temp.SetHP(newHP);
                }

                break;

            case "Mental":
                // hit all enemies for atk / 2
                for(int i=0; i < enemies.length; i++)
                {
                    enemies[i].TakeDamage(GetATK() / 2);
                    // gain energy
                    Energy += GetATK() / 2 * 6;
                    if (Energy > 100)
                    {
                        Energy = 100;
                    }
                }

                break;
        }
    }

    public void UseUltimate(Player[] players, Enemy[] enemies, int target)
    {
        // reset energy
        Energy = 0;

        switch (Name)
        {
            case "Physical":
                // hit an enemy for atk * 5
                enemies[target].TakeDamage(GetATK() * 5);
                // hit enemies beside that enemy for atk * 2
                if (target - 1 >= 0)
                {
                    enemies[target - 1].TakeDamage(GetATK() * 2);
                }
                if (target + 1 < enemies.length)
                {
                    enemies[target + 1].TakeDamage(GetATK() * 2);
                }

                break;

            case "Emotional":
                // heal all players by 3, up to max hp
                for (int i = 0; i < players.length; i++)
                {
                    int newHP = players[i].GetHP() + 3;

                    if (newHP >= players[i].GetMHP())
                    {
                        players[i].SetHP(players[i].GetMHP());
                    }
                    else
                    {
                        players[i].SetHP(newHP);
                    }
                }

                break;

            case "Mental":

                Random rand = new Random();
                // hit an enemy for atk / 2, 5 times
                for(int i=0; i < 5; i++)
                {
                    int randEnemy = rand.nextInt(enemies.length);
                    enemies[randEnemy].TakeDamage(GetATK() / 2);
                }

                break;
        }
    }
}
