package com.nypsdm.dx1221_week04;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceView;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

// Created by TanSiewLan2021

public class MainCombatSceneState implements StateBase {
    private Player[] p;
    private int skillPoints;
    private Enemy[] e;

    private int count;

    private Random rand = new Random();

    int randPlayer;
    int round;
    int entityCount;
    int currPlace;

    @Override
    public String GetName() {
        return "MainCombat";
    }

    @Override
    public void OnEnter(SurfaceView _view)
    {
        RenderCombatBackground.Create();

        AudioManager.Instance.PlayAudio(R.raw.bgm_sorrowful, 0.25f, true);

        // Player Entities
        PhysicalEntity.Create();
        MentalEntity.Create();
        EmotionalEntity.Create();

        // Temp
        ButtonStartEntity.Create();
        ButtonNextEntity.Create();

        // Actual Combat Buttons
        ButtonBasicEntity.Create();
        ButtonSkillEntity.Create();
        ButtonUltimateEntity.Create();

        // Enemies
        int amtOfEnemies = 3;
        int baseX = 1200;
        int baseY = 875;

        if (amtOfEnemies == 1 || amtOfEnemies == 2 || amtOfEnemies == 3)
        {
            CombatEnemyEntity enemy1Sprite = CombatEnemyEntity.Create();
            enemy1Sprite.SetPos(baseX, baseY);
            ButtonEnemyEntity enemy1Button = ButtonEnemyEntity.Create();
            enemy1Button.SetPos(enemy1Sprite.GetPosX(), enemy1Sprite.GetPosY());

            if (amtOfEnemies == 2 || amtOfEnemies == 3)
            {
                CombatEnemyEntity enemy2Sprite = CombatEnemyEntity.Create();
                enemy2Sprite.SetPos(baseX + 200, baseY);
                ButtonEnemyEntity enemy2Button = ButtonEnemyEntity.Create();
                enemy2Button.SetPos(enemy2Sprite.GetPosX(), enemy2Sprite.GetPosY());

                if (amtOfEnemies == 3)
                {
                    CombatEnemyEntity enemy3Sprite = CombatEnemyEntity.Create();
                    enemy3Sprite.SetPos(baseX + 400, baseY);
                    ButtonEnemyEntity enemy3Button = ButtonEnemyEntity.Create();
                    enemy3Button.SetPos(enemy3Sprite.GetPosX(), enemy3Sprite.GetPosY());
                }
            }
        }

        //Text
        RenderTextEntity.Create();
        RenderAbilityTextEntity.Create();
        RenderStatTextEntity.Create();

        Vibrator.Initialize((android.os.Vibrator)_view.getContext().getSystemService(_view.getContext().VIBRATOR_SERVICE));
    }

    @Override
    public void OnExit()
    {
        /*for (int i = 0; i < p.length; i++)
        {
            SmurfEntity.p[i] = p[i];
        }*/

        EntityManager.Instance.Clean();

        NextPage.Instance.finish();
    }

    @Override
    public void Render(Canvas _canvas)
    {
        EntityManager.Instance.Render(_canvas, 0, 0);
    }

    @Override
    public void Update(float _dt)
    {
        EntityManager.Instance.Update(_dt);

        if (TouchManager.Instance.IsDown())
        {
			//6. Example of touch on screen in the main game to trigger back to Main menu
            //StateManager.Instance.ChangeState("MainMenu");
        }
    }

    private void DoDamage()
    {
        if (LookForEntityType(currPlace) == "player")
        {
            // do attack code
            p[EntityInArray(currPlace)].BasicAttack(e[count]);
            // gain skill point
            if (skillPoints < 5)
            {
                skillPoints++;
            }
            // check if enemy is dead and removes it from the fight
            if (e[count].GetHP() <= 0)
            {
                RemoveEnemy(count);
                count = 0;
            }
            // progress fight
            currPlace++;
            if (LookForEntityType(currPlace) == null)
            {
                currPlace = 1;
                round++;
            }
        }
        // enemy turns
        DoEnemyTurns();
        // continue turn order
        PrintAllStats(round);
        PrintRoundStatus();
    }
    private void DoSkill()
    {
        // find player
        int i = EntityInArray(currPlace);
        // do skill code
        p[i].UseSkill(p, e, count, round);
        // consume skill point
        skillPoints--;
        // checks for any dead enemies and removes them from the fight
        for (int k = 0; k < e.length; k++)
        {
            if (e[k].GetHP() <= 0)
            {
                RemoveEnemy(k);
                count = 0;
            }
        }
        // progress fight
        currPlace++;
        if (LookForEntityType(currPlace) == null)
        {
            currPlace = 1;
            round++;
        }
        // enemy turns
        DoEnemyTurns();
        // continue turn order
        PrintAllStats(round);
        PrintRoundStatus();
    }
    private void DoUltimate(int i)
    {
        // do ultimate code
        p[i].UseUltimate(p, e, count);
        // consume skill point
        skillPoints--;
        // checks for any dead enemies and removes them from the fight
        for (int k = 0; k < e.length; k++)
        {
            if (e[k].GetHP() <= 0)
            {
                RemoveEnemy(k);
                count = 0;
            }
        }
        // progress fight
        currPlace++;
        if (LookForEntityType(currPlace) == null)
        {
            currPlace = 1;
            round++;
        }
        // enemy turns
        DoEnemyTurns();
        // continue turn order
        PrintAllStats(round);
        PrintRoundStatus();
    }
    private void DoEnemyTurns()
    {
        // while its the enemy turn
        while (LookForEntityType(currPlace) == "enemy")
        {
            // continue turn order
            PrintAllStats(round);
            PrintRoundStatus();
            // do attack code on random player
            randPlayer = rand.nextInt(p.length);
            p[randPlayer].TakeDamage(e[EntityInArray(currPlace)].GetATK(), e[EntityInArray(currPlace)].GetHT());
            // progress fight
            currPlace++;
            if (LookForEntityType(currPlace) == null)
            {
                currPlace = 1;
                round++;
            }
        }
    }
    private void RemoveEnemy(int positionInArray)
    {
        // removes the dead enemy and resizes enemy array
        Enemy[] temp = new Enemy[e.length - 1];

        for(int i=0, k=0; i < e.length; i++)
        {
            if(i != positionInArray)
            {
                temp[k] = e[i];
                k++;
            }
        }

        e = new Enemy[temp.length];
        e = temp;
    }
    private int EntityInArray(int place)
    {
        // find an entities position in its respective array
        for (int i = 0; i < p.length; i++)
        {
            if (p[i].placeInTurn == place)
            {
                return i;
            }
        }
        for (int i = 0; i < e.length; i++)
        {
            if (e[i].placeInTurn == place)
            {
                return i;
            }
        }
        return -1;
    }
    private String LookForEntityType(int place)
    {
        // finds the entity type in a specific place
        for (int i = 0; i < p.length; i++)
        {
            if (p[i].placeInTurn == place)
            {
                return p[i].type;
            }
        }
        for (int i = 0; i < e.length; i++)
        {
            if (e[i].placeInTurn == place)
            {
                return e[i].type;
            }
        }
        return null;
    }
    private void CreateAndSortPlayers()
    {
        p = new Player[3];
        for (int i = 0; i < p.length; i++)
        {
            p[i] = SmurfEntity.p[i];
        }

        entityCount += p.length;
    }
    private void CreateAndSortEnemies(int amount, int minHP, int maxHP, int minATK, int maxATK, int minSPD, int maxSPD)
    {
        // creates a random amount of enemies with random stats
        e = new Enemy[amount];
        for (int i = 0; i < e.length; i++)
        {
            e[i] = new Enemy("Enemy " + String.valueOf(i + 1),
                    GetRandomNumber(minHP, maxHP),
                    GetRandomNumber(minATK, maxATK),
                    GetRandomNumber(minSPD, maxSPD),
                    "PHY");
        }
        for (int k = 0; k < e.length; k++)
        {
            for (int i = 0; i < e.length; i++)
            {
                if (i + 1 < e.length)
                {
                    if (e[i].GetSPD() < e[i+1].GetSPD())
                    {
                        Enemy temp = e[i];
                        e[i] = e[i + 1];
                        e[i + 1] = temp;
                    }
                }
            }
        }
        for (int i = 0; i < e.length; i++)
        {
            e[i].Name = "Enemy " + String.valueOf(i + 1);
        }
        entityCount += e.length;
    }
    private void PrintAllStats(int roundNo)
    {
        // prints round details
        Log.d("test", "===============");
        Log.d("test", "Round: " + String.valueOf(roundNo));
        Log.d("test", "===============");
        Log.d("test", "");

        int count = 0;
        int place = 1;

        //prints all entities in order of speed
        for (int i = 0; i < p.length; i++)
        {
            if (count < e.length)
            {
                for (int k = count; k < e.length; k++)
                {
                    if (p[i].GetSPD() < e[k].GetSPD())
                    {
                        e[k].PrintStats(place);
                        place++;
                        count++;
                    }
                    else
                    {
                        p[i].PrintStats(place, round);
                        place++;
                        break;
                    }
                }
                if (count >= e.length)
                {
                    p[i].PrintStats(place, round);
                    place++;
                }
            }
            else
            {
                p[i].PrintStats(place, round);
                place++;
            }
        }
        for (int k = count; k < e.length; k++)
        {
            e[k].PrintStats(place);
            place++;
        }
    }
    private void PrintRoundStatus()
    {
        // normal flow
        if (e.length > 0)
        {
            Log.d("test", "====================");
            if (LookForEntityType(currPlace) == "player")
            {
                Log.d("test", "<" + p[EntityInArray(currPlace)].Name + ">'s Turn.");
                Log.d("test", skillPoints + " Skill Point(s) Remaining.");
                Log.d("test", "Ultimate Charge: " + p[EntityInArray(currPlace)].Energy + "/100.");
                Log.d("test", "Targeting: <" + e[count].Name + "> [" + e[count].GetHT() + "]. " + CheckWeakness(p[EntityInArray(currPlace)].GetHT(), e[count].GetHT()));
            }
            else if (LookForEntityType(currPlace) == "enemy")
            {
                Log.d("test", "<" + e[EntityInArray(currPlace)].Name + ">'s turn.");
                Log.d("test", "Targeting: <" + p[randPlayer].Name + ">" + CheckWeakness(p[EntityInArray(currPlace)].GetHT(), e[count].GetHT()));
            }
            Log.d("test", "====================");
            Log.d("test", "");
        }
        // if player win
        else
        {
            Log.d("test", "====================");
            if (LookForEntityType(currPlace) == "player")
            {
                Log.d("test", "You Win!");
            }
            Log.d("test", "====================");
            Log.d("test", "");
        }
    }
    private String CheckWeakness(String ht1, String ht2)
    {
        switch (ht1)
        {
            case "PHY":
                if (ht2 == "PHY")
                {
                    return "";
                }
                else if (ht2 == "MEN")
                {
                    return "(-)";
                }
                else if (ht2 == "EMO")
                {
                    return "(+)";
                }
                break;
            case "MEN":
                if (ht2 == "PHY")
                {
                    return "(+)";
                }
                else if (ht2 == "MEN")
                {
                    return "";
                }
                else if (ht2 == "EMO")
                {
                    return "(-)";
                }
                break;
            case "EMO":
                if (ht2 == "PHY")
                {
                    return "(-)";
                }
                else if (ht2 == "MEN")
                {
                    return "(+)";
                }
                else if (ht2 == "EMO")
                {
                    return "";
                }
                break;
        }

        return "";
    }
    private int GetRandomNumber(int lowest, int highest)
    {
        return rand.nextInt(highest - lowest + 1) + lowest;
    }
}



