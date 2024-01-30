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

    public PhysicalEntity phyChar;
    public MentalEntity menChar;
    public EmotionalEntity emoChar;

    public CombatEnemyEntity enemy1Sprite;
    public CombatEnemyEntity enemy2Sprite;
    public CombatEnemyEntity enemy3Sprite;

    public ButtonEnemyEntity enemy1Button;
    public ButtonEnemyEntity enemy2Button;
    public ButtonEnemyEntity enemy3Button;

    public RenderEnemyHPTextEntity enemy1HP;
    public RenderEnemyHPTextEntity enemy2HP;
    public RenderEnemyHPTextEntity enemy3HP;

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
        // Done by Bernard Ng
        RenderCombatBackground.Create();

        AudioManager.Instance.PlayAudio(R.raw.bgm_sorrowful, 0.25f, true);

        // Player Entities by Bernard Ng
        phyChar = PhysicalEntity.Create();
        menChar = MentalEntity.Create();
        emoChar = EmotionalEntity.Create();

        // Actual Combat Buttons by Bernard Ng
        ButtonBasicEntity basicBtn = ButtonBasicEntity.Create();
        basicBtn.combatScene = this;
        ButtonSkillEntity skillBtn = ButtonSkillEntity.Create();
        skillBtn.combatScene = this;
        ButtonUltimateEntity ultimateBtn = ButtonUltimateEntity.Create();
        ultimateBtn.combatScene = this;

        // Enemies by Bernard Ng
        int amtOfEnemies = 3;
        int baseX = 1600;
        int baseY = 945;

        if (amtOfEnemies == 1 || amtOfEnemies == 2 || amtOfEnemies == 3)
        {
            enemy1Sprite = CombatEnemyEntity.Create();
            enemy1Sprite.SetPos(baseX, baseY);
            enemy1Button = ButtonEnemyEntity.Create();
            enemy1Button.combatScene = this;
            enemy1Button.enemyNo = 0;
            enemy1Button.SetPos(enemy1Sprite.GetPosX(), enemy1Sprite.GetPosY());
            enemy1HP = RenderEnemyHPTextEntity.Create();
            enemy1HP.enemy = 0;
            enemy1HP.SetPos(enemy1Button.GetPosX() - 50, enemy1Button.GetPosY() - 100);

            if (amtOfEnemies == 2 || amtOfEnemies == 3)
            {
                enemy2Sprite = CombatEnemyEntity.Create();
                enemy2Sprite.SetPos(baseX + 200, baseY);
                enemy2Button = ButtonEnemyEntity.Create();
                enemy2Button.combatScene = this;
                enemy2Button.enemyNo = 1;
                enemy2Button.SetPos(enemy2Sprite.GetPosX(), enemy2Sprite.GetPosY());
                enemy2HP = RenderEnemyHPTextEntity.Create();
                enemy2HP.enemy = 1;
                enemy2HP.SetPos(enemy2Button.GetPosX() - 50, enemy2Button.GetPosY() - 100);

                if (amtOfEnemies == 3)
                {
                    enemy3Sprite = CombatEnemyEntity.Create();
                    enemy3Sprite.SetPos(baseX + 400, baseY);
                    enemy3Button = ButtonEnemyEntity.Create();
                    enemy3Button.combatScene = this;
                    enemy3Button.enemyNo = 2;
                    enemy3Button.SetPos(enemy3Sprite.GetPosX(), enemy3Sprite.GetPosY());
                    enemy3HP = RenderEnemyHPTextEntity.Create();
                    enemy3HP.enemy = 2;
                    enemy3HP.SetPos(enemy3Button.GetPosX() - 50, enemy3Button.GetPosY() - 100);
                }
            }
        }

        // Text by Bernard Ng
        RenderTextEntity.Create();
        abilityText = RenderAbilityTextEntity.Create();
        statText = RenderStatTextEntity.Create();
        roundText = RenderRoundTextEntity.Create();
        turnOrderText = RenderTurnOrderTextEntity.Create();

        // Done by Bernard Ng
        Vibrator.Initialize((android.os.Vibrator)_view.getContext().getSystemService(_view.getContext().VIBRATOR_SERVICE));

        // Done by Kodey Chin
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

    // Done by Bernard Ng
    public void ResetEnemyButtonImage()
    {
        enemy1Button.SetImage(R.drawable.blank);
        enemy2Button.SetImage(R.drawable.blank);
        enemy3Button.SetImage(R.drawable.blank);
    }

    // Done by Kodey Chin
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

    // Done by Kodey Chin
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

    // Done by Kodey Chin
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

    // Done by Kodey Chin
    public void DoEnemyTurns()
    {
        // while its the enemy turn
        while (LookForEntityType(currPlace) == "enemy")
        {
            if (!p[EntityInArray(currPlace)].isDead)
            {
                // continue turn order
                PrintAllStats(round);
                PrintRoundStatus();
                // do attack code on random player
                randPlayer = rand.nextInt(p.length);
                //check if player is dead
                if (!p[randPlayer].isDead)
                {
                    p[randPlayer].TakeDamage(e[EntityInArray(currPlace)].GetATK(), e[EntityInArray(currPlace)].GetHT());
                    //check if player is dead
                    if (p[randPlayer].GetHP() <= 0)
                    {
                        p[randPlayer].isDead = true;
                    }
                }
            }
            // progress fight
            currPlace++;
            if (LookForEntityType(currPlace) == null)
            {
                currPlace = 1;
                round++;

            }
        }
        while (LookForEntityType(currPlace) == "player")
        {
            if (p[EntityInArray(currPlace)].isDead)
            {
                // progress fight
                currPlace++;
                if (LookForEntityType(currPlace) == null)
                {
                    currPlace = 1;
                    round++;

                }
            }
            else
            {
                break;
            }
        }
    }

    // Done by Kodey Chin
    private void RemoveEnemy(int positionInArray)
    {
        // set enemy to dead
        e[positionInArray].isDead = true;
        // check if all enemies are dead
        int k = 0;
        for (int i = 0; i < e.length; i++)
        {
            if (e[i].isDead)
            {
                k++;
            }
        }
        // if all enemies are dead
        if (k == e.length)
        {
            NextPage.Instance.ChangeToWin();
        }
        else // set target to next enemy
        {
            count++;
            if (count == e.length)
            {
                for (int i = 0; i < e.length; i++)
                {
                    if (!e[i].isDead)
                    {
                        count = i;
                        break;
                    }
                }
            }
        }

        // un-render enemy by Bernard Ng
        switch (positionInArray)
        {
            case 0:
                enemy1Sprite.SetIsDone(true);
                enemy1Button.SetIsDone(true);
                enemy1HP.SetIsDone(true);
                break;
            case 1:
                enemy2Sprite.SetIsDone(true);
                enemy2Button.SetIsDone(true);
                enemy2HP.SetIsDone(true);
                break;
            case 2:
                enemy3Sprite.SetIsDone(true);
                enemy3Button.SetIsDone(true);
                enemy3HP.SetIsDone(true);
                break;
        }
    }

    // Done by Kodey Chin
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
        return 0;
    }

    // Done by Kodey Chin
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

    // Done by Kodey Chin
    public void CreateAndSortPlayers()
    {
        p = new Player[3];
        for (int i = 0; i < p.length; i++)
        {
            p[i] = SmurfEntity.p[i];
        }

        entityCount += p.length;
    }

    // Done by Kodey Chin
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

    // Done by Kodey Chin
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

        enemy1HP.SetHealthText(e[0].GetHP());
        enemy2HP.SetHealthText(e[1].GetHP());
        enemy3HP.SetHealthText(e[2].GetHP());

        turnOrderText.SetValues(turnOrderList, currPlace);
    }

    // Done by Kodey Chin
    public void PrintRoundStatus()
    {
        // normal flow
        if (e.length > 0)
        {
            if (LookForEntityType(currPlace) == "player")
            {
                statText.SetValues(p[EntityInArray(currPlace)].GetHP(), p[EntityInArray(currPlace)].GetMHP(), p[EntityInArray(currPlace)].GetATK(), p[EntityInArray(currPlace)].GetSPD());
                abilityText.value = skillPoints;
                abilityText.value2 = p[EntityInArray(currPlace)].Energy;
            }
            else if (LookForEntityType(currPlace) == "enemy")
            {
                Log.d("test", "<" + e[EntityInArray(currPlace)].Name + ">'s turn.");
                Log.d("test", "Targeting: <" + p[randPlayer].Name + ">" + CheckWeakness(p[EntityInArray(currPlace)].GetHT(), e[count].GetHT()));
            }
        }
    }

    // Done by Kodey Chin
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

    // Done by Kodey Chin
    public int GetRandomNumber(int lowest, int highest)
    {
        return rand.nextInt(highest - lowest + 1) + lowest;
    }
}



