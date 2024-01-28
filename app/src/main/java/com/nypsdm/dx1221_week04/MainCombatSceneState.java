package com.nypsdm.dx1221_week04;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceView;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

// Created by TanSiewLan2021

public class MainCombatSceneState implements StateBase {
    public Player[] p;
    public int skillPoints;
    public Enemy[] e;

    public int count;

    public Random rand = new Random();

    public int randPlayer;
    public int round;
    public int entityCount;
    public int currPlace;

    public ButtonEnemyEntity enemy1Button;
    public ButtonEnemyEntity enemy2Button;
    public ButtonEnemyEntity enemy3Button;

    RenderAbilityTextEntity abilityText;
    RenderStatTextEntity statText;
    RenderRoundTextEntity roundText;
    RenderTurnOrderTextEntity turnOrderText;

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
        ButtonStartEntity startBtn = ButtonStartEntity.Create();
        startBtn.combatScene = this;
        ButtonNextEntity.Create();

        // Actual Combat Buttons
        ButtonBasicEntity basicBtn = ButtonBasicEntity.Create();
        basicBtn.combatScene = this;
        ButtonSkillEntity skillBtn = ButtonSkillEntity.Create();
        skillBtn.combatScene = this;
        ButtonUltimateEntity ultimateBtn = ButtonUltimateEntity.Create();
        ultimateBtn.combatScene = this;

        // Enemies
        int amtOfEnemies = 3;
        int baseX = 1600;
        int baseY = 945;

        if (amtOfEnemies == 1 || amtOfEnemies == 2 || amtOfEnemies == 3)
        {
            CombatEnemyEntity enemy1Sprite = CombatEnemyEntity.Create();
            enemy1Sprite.SetPos(baseX, baseY);
            enemy1Button = ButtonEnemyEntity.Create();
            enemy1Button.combatScene = this;
            enemy1Button.enemyNo = 0;
            enemy1Button.SetPos(enemy1Sprite.GetPosX(), enemy1Sprite.GetPosY());

            if (amtOfEnemies == 2 || amtOfEnemies == 3)
            {
                CombatEnemyEntity enemy2Sprite = CombatEnemyEntity.Create();
                enemy2Sprite.SetPos(baseX + 200, baseY);
                enemy2Button = ButtonEnemyEntity.Create();
                enemy2Button.combatScene = this;
                enemy2Button.enemyNo = 1;
                enemy2Button.SetPos(enemy2Sprite.GetPosX(), enemy2Sprite.GetPosY());

                if (amtOfEnemies == 3)
                {
                    CombatEnemyEntity enemy3Sprite = CombatEnemyEntity.Create();
                    enemy3Sprite.SetPos(baseX + 400, baseY);
                    enemy3Button = ButtonEnemyEntity.Create();
                    enemy3Button.combatScene = this;
                    enemy3Button.enemyNo = 2;
                    enemy3Button.SetPos(enemy3Sprite.GetPosX(), enemy3Sprite.GetPosY());
                }
            }
        }

        //Text
        RenderTextEntity.Create();
        abilityText = RenderAbilityTextEntity.Create();
        statText = RenderStatTextEntity.Create();
        roundText = RenderRoundTextEntity.Create();
        turnOrderText = RenderTurnOrderTextEntity.Create();

        Vibrator.Initialize((android.os.Vibrator)_view.getContext().getSystemService(_view.getContext().VIBRATOR_SERVICE));

        CreateAndSortPlayers();
        CreateAndSortEnemies(
                GetRandomNumber(3, 3),
                10, 15,
                1, 2,
                1, 5
        );

        randPlayer = rand.nextInt(p.length);

        round = 1;
        currPlace = 1;
        skillPoints = 3;

        PrintAllStats(round);
        PrintRoundStatus();

        if (LookForEntityType(currPlace) == "enemy")
        {
            DoDamage();
        }
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

    public void ResetEnemyButtonImage()
    {
        enemy1Button.SetImage(R.drawable.blank);
        enemy2Button.SetImage(R.drawable.blank);
        enemy3Button.SetImage(R.drawable.blank);
    }

    public void DoDamage()
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
    public void DoSkill()
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
    public void DoUltimate(int i)
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
    public void DoEnemyTurns()
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
        if (e.length - 1 <= 0)
        {
            NextPage.Instance.ChangeToWin();
        }
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
    public int EntityInArray(int place)
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
    public String LookForEntityType(int place)
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
    public void CreateAndSortPlayers()
    {
        p = new Player[3];
        for (int i = 0; i < p.length; i++)
        {
            p[i] = SmurfEntity.p[i];
        }

        entityCount += p.length;
    }
    public void CreateAndSortEnemies(int amount, int minHP, int maxHP, int minATK, int maxATK, int minSPD, int maxSPD)
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
    public void PrintAllStats(int roundNo)
    {
        // prints round details
        Log.d("test", "===============");
        Log.d("test", "Round: " + String.valueOf(roundNo));
        roundText.SetValues(roundNo);
        Log.d("test", "===============");
        Log.d("test", "");

        int count = 0;
        int place = 1;

        String[] turnOrderList = new String[6];
        int entityNo = 0;

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
                        turnOrderList[entityNo] = "E" + (k + 1);
                        entityNo++;
                        place++;
                        count++;
                    }
                    else
                    {
                        p[i].PrintStats(place, round);
                        turnOrderList[entityNo] = p[i].GetHT();
                        entityNo++;
                        place++;
                        break;
                    }
                }
                if (count >= e.length)
                {
                    p[i].PrintStats(place, round);
                    turnOrderList[entityNo] = p[i].GetHT();
                    entityNo++;
                    place++;
                }
            }
            else
            {
                p[i].PrintStats(place, round);
                turnOrderList[entityNo] = p[i].GetHT();
                entityNo++;
                place++;
            }
        }
        for (int k = count; k < e.length; k++)
        {
            e[k].PrintStats(place);
            turnOrderList[entityNo] = "E" + (k + 1);
            entityNo++;
            place++;
        }

        turnOrderText.SetValues(turnOrderList, currPlace);
    }
    public void PrintRoundStatus()
    {
        // normal flow
        if (e.length > 0)
        {
            Log.d("test", "====================");
            if (LookForEntityType(currPlace) == "player")
            {
                Log.d("test", "<" + p[EntityInArray(currPlace)].Name + ">'s Turn.");
                statText.SetValues(p[EntityInArray(currPlace)].GetHP(), p[EntityInArray(currPlace)].GetMHP(), p[EntityInArray(currPlace)].GetATK(), p[EntityInArray(currPlace)].GetSPD());
                Log.d("test", skillPoints + " Skill Point(s) Remaining.");
                Log.d("test", "Ultimate Charge: " + p[EntityInArray(currPlace)].Energy + "/100.");
                abilityText.value = skillPoints;
                abilityText.value2 = p[EntityInArray(currPlace)].Energy;
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
    public int GetRandomNumber(int lowest, int highest)
    {
        return rand.nextInt(highest - lowest + 1) + lowest;
    }
}



