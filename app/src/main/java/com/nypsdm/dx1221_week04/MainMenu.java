package com.nypsdm.dx1221_week04;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainMenu extends Activity implements OnClickListener, StateBase
{
    public static MainMenu Instance = null;

    private Button btn_start;
    private Button btn_setting;
    private Button btn_quit;

    @Override
    public String GetName()
    {
        return "MainMenu";
    }

    @Override
    public void OnEnter(SurfaceView _view)
    {
        // 3. Create Background

        // Example to include another Renderview for Pause Button

        AudioManager.Instance.PlayAudio(R.raw.bgm_sorrowful, 0.25f, true);
    }

    @Override
    public void OnExit()
    {
        // 4. Clear any instance instantiated via EntityManager.

        // 5. Clear or end any instance instantiated via GamePage.
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

        }
    }

    @Override
    protected void onCreate(Bundle saveInstanceState)
    {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.mainmenu);

        btn_start = (Button) findViewById(R.id.btn_start);
        btn_start.setOnClickListener(this);
        // Set Listener to this button --> Start Button

        btn_setting = (Button) findViewById(R.id.btn_setting);
        btn_setting.setOnClickListener(this);
        // Set Listener to this button --> Back Button

        btn_quit = (Button) findViewById(R.id.btn_quit);
        btn_quit.setOnClickListener(this);
        // Set Listener to this button --> Quit Button

        StateManager.Instance.AddState(new MainMenu());

        Instance = this;

        StateManager.Instance.Init(new SurfaceView(this));
        GameSystem.Instance.Init(new SurfaceView(this));
        StateManager.Instance.ChangeState("MainMenu");
    }

    @Override
    //Invoke a callback event in the view
    public void onClick(View v)
    {
        //Intent = action to be performed
        //Intent is an object provides runtime binding
        //You have to new an instance of this object to use it
        //Because we need to check if start or back button is clicked/ touched on the
        //screen, then after
        //what do we want to do.
        //If start button is clicked, we go to Splash page.
        //If back button is clicked, we go to main menu.

        Intent intent = new Intent();

        if (v == btn_start)
        {
            //intent -> to set to another class which is another page or screen to be
            //launch.
            //Equal to change screen
            intent.setClass(this, GamePage.class);
            StateManager.Instance.ChangeState("MainGame");
        }
        else if (v == btn_setting)
        {
            intent.setClass(this, SettingPage.class);
            StateManager.Instance.ChangeState("SettingPage");

        }
        else if (v == btn_quit)
        {
            this.finishAffinity();
        }

        startActivity(intent);
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
