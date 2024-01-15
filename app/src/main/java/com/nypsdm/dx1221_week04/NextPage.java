package com.nypsdm.dx1221_week04;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.app.Activity;
import android.view.ViewDebug;
import android.widget.Button;

import java.util.Arrays;
import java.util.Random;

public class NextPage extends Activity implements OnClickListener, StateBase
{
    public static NextPage Instance = null;

    private Button btn_back;
    private Button btn_create;
    private Button btn_basic;
    private Button btn_skill;
    private Button btn_ultimate;
    private Button btn_cycle;

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
    protected void onCreate (Bundle saveInstanceState)
    {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.nextpage);
        // Set Listener to this button --> Start Button
        btn_back = (Button)findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);
        btn_create = (Button)findViewById(R.id.btn_create);
        btn_create.setOnClickListener(this);
        btn_basic = (Button)findViewById(R.id.btn_basic);
        btn_basic.setOnClickListener(this);
        btn_skill = (Button)findViewById(R.id.btn_skill);
        btn_skill.setOnClickListener(this);
        btn_ultimate = (Button)findViewById(R.id.btn_ultimate);
        btn_ultimate.setOnClickListener(this);
        btn_cycle = (Button)findViewById(R.id.btn_cycle);
        btn_cycle.setOnClickListener(this);

        StateManager.Instance.AddState(new NextPage());

        Instance = this;

        StateManager.Instance.Init(new SurfaceView(this));
        GameSystem.Instance.Init(new SurfaceView(this));
        StateManager.Instance.ChangeState("NextPage");

        count = 0;
    }

    @Override
    public void onClick (View v)
    {
        Intent intent = new Intent();
        if (v == btn_back)
        {
            intent.setClass(this,MainMenu.class);
            startActivity(intent);
        }

        if (v == btn_create)
        {
            // initializes fight
            CreateAndSortPlayers();
            CreateAndSortEnemies(
                    GetRandomNumber(1, 5),
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
        if (v == btn_basic)
        {
            // when no more enemies
            if (e.length == 0)
            {
                intent.setClass(this,MainMenu.class);
                startActivity(intent);
            }
            else
            {
                DoDamage();
            }
        }
        if (v == btn_skill)
        {
            // when no more enemies
            if (e.length == 0)
            {
                intent.setClass(this,MainMenu.class);
                startActivity(intent);
            }
            // has skill points
            else if (skillPoints > 0)
            {
                DoSkill();
            }
        }
        if (v == btn_ultimate)
        {
            // find player
            int i = EntityInArray(currPlace);
            // when no more enemies
            if (e.length == 0)
            {
                intent.setClass(this,MainMenu.class);
                startActivity(intent);
            }
            //ultimate is charged
            else if (p[i].Energy == 100)
            {
                DoUltimate(i);
            }
        }
        if (v == btn_cycle)
        {
            // changes the enemy that the player is targeting
            count++;
            if (count >= e.length)
            {
                count = 0;
            }

            PrintAllStats(round);
            PrintRoundStatus();
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
            p[randPlayer].TakeDamage(e[EntityInArray(currPlace)].GetATK());
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
            e[i] = new Enemy("Enemy " + String.valueOf(i + 1), GetRandomNumber(minHP, maxHP), GetRandomNumber(minATK, maxATK), GetRandomNumber(minSPD, maxSPD));
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
                Log.d("test", "Targeting: <" + e[count].Name + ">.");
            }
            else if (LookForEntityType(currPlace) == "enemy")
            {
                Log.d("test", "<" + e[EntityInArray(currPlace)].Name + ">'s turn.");
                Log.d("test", "Targeting: <" + p[randPlayer].Name + ">");
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
    private int GetRandomNumber(int lowest, int highest)
    {
        return rand.nextInt(highest - lowest + 1) + lowest;
    }

    @Override // Mainmenu state is given a name as MainMenu.
    public String GetName()
    {
        return "NextPage";
    }

    @Override
    public void Render(Canvas _canvas)
    {

    }

    @Override
    public void OnEnter(SurfaceView _view)
    {

    }

    @Override
    public void Update(float _dt)
    {

    }

    @Override
    public void OnExit()
    {
        for (int i = 0; i < p.length; i++)
        {
            SmurfEntity.p[i] = p[i];
        }
    }

    @Override
    protected void onPause()
    {
        super.onPause();
    }
    @Override
    protected void onStop()
    {
        super.onStop();
    }
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }
}
