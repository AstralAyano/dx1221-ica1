package com.sdm.mgp2023;

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
    private Button btn_damage;
    private Button btn_heal;
    private Button btn_cycle;

    private Player[] p;
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
        btn_damage = (Button)findViewById(R.id.btn_damage);
        btn_damage.setOnClickListener(this);
        btn_heal = (Button)findViewById(R.id.btn_heal);
        btn_heal.setOnClickListener(this);
        btn_cycle = (Button)findViewById(R.id.btn_cycle);
        btn_cycle.setOnClickListener(this);

        StateManager.Instance.AddState(new NextPage());

        Instance = this;

        StateManager.Instance.Init(new SurfaceView(this));
        GameSystem.Instance.Init(new SurfaceView(this));
        StateManager.Instance.Start("NextPage");

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

            PrintAllStats(round);
            PrintRoundStatus();

            if (LookForEntityType(currPlace) == "enemy")
            {
                DoDamage();
            }
        }
        if (v == btn_damage)
        {
            if (e.length == 0) // send back if game end
            {
                intent.setClass(this,MainMenu.class);
                startActivity(intent);
            }
            else
            {
                DoDamage();
            }
        }
        if (v == btn_heal)
        {
            if (e.length == 0) // send back if game end
            {
                intent.setClass(this,MainMenu.class);
                startActivity(intent);
            }
            else
            {
                DoHeal(5);
            }
        }
        if (v == btn_cycle)
        {
            count++;
            if (count >= e.length)
            {
                count = 0;
            }

            PrintAllStats(round);
            PrintRoundStatus();
        }
    }

    private void DoHeal(int healAmt)
    {
        int i = EntityInArray(currPlace);
        if (p[i].healCount > 0)
        {
            p[i].SetHP(p[i].GetHP() + healAmt);
            p[i].healCount--;

            // progress fight
            currPlace++;
            if (LookForEntityType(currPlace) == null)
            {
                currPlace = 1;
                round++;
            }
        }

        DoEnemyTurns();

        PrintAllStats(round);
        PrintRoundStatus();
    }
    private void DoDamage()
    {
        if (LookForEntityType(currPlace) == "player")
        {
            e[count].TakeDamage(p[EntityInArray(currPlace)].GetATK());
            if (e[count].GetHP() <= 0)
            {
                RemoveEnemy(count);
            }
            // progress fight
            currPlace++;
            if (LookForEntityType(currPlace) == null)
            {
                currPlace = 1;
                round++;
            }
        }

        DoEnemyTurns();

        PrintAllStats(round);
        PrintRoundStatus();
    }
    private void DoEnemyTurns()
    {
        while (LookForEntityType(currPlace) == "enemy")
        {
            p[randPlayer].TakeDamage(e[EntityInArray(currPlace)].GetATK());
            randPlayer = rand.nextInt(p.length);

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
            p[i] = StateManager.p[i];
        }

        entityCount += p.length;
    }
    private void CreateAndSortEnemies(int amount, int minHP, int maxHP, int minATK, int maxATK, int minSPD, int maxSPD)
    {
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
                        p[i].PrintStats(place);
                        place++;
                        break;
                    }
                }
                if (count >= e.length)
                {
                    p[i].PrintStats(place);
                    place++;
                }
            }
            else
            {
                p[i].PrintStats(place);
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
                Log.d("test", "<" + p[EntityInArray(currPlace)].Name + ">'s turn.");
                Log.d("test", "heals left: " + String.valueOf(p[EntityInArray(currPlace)].healCount));
                Log.d("test", "targeting: <" + e[count].Name + ">");
            }
            else if (LookForEntityType(currPlace) == "enemy")
            {
                Log.d("test", "<" + e[EntityInArray(currPlace)].Name + ">'s turn.");
                Log.d("test", "targeting: <" + p[randPlayer].Name + ">");
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
            StateManager.p[i] = p[i];
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
