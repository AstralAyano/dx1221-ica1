package com.nypsdm.dx1221_week04;

// Created by TanSiewLan2023

// StateManager to deal with which state is current or next.

import android.graphics.Canvas;
import android.view.SurfaceView;

import java.util.HashMap;
import java.util.Objects;

public class StateManager {
    // Singleton Instance
    public static final StateManager Instance = new StateManager();

    // Container to store all our states!
    private HashMap<String, StateBase> stateMap = new HashMap<String, StateBase>();

    // KS (28/11/23): We convert hard references of StateBase instances, to soft references using
    // strings instead. Each soft reference would allow us to grab its corresponding hard reference
    // any time the hard reference is ready (i.e. on StateManager.Update(dt)).
    private String prevState = null;
    private String currState = null;
    private String nextState = null;

    private SurfaceView view = null;

    // This is the protected constructor for singleton
    private StateManager()
    {
    }

    public void Init(SurfaceView _view)
    {
        view = _view;
    }

    void AddState(StateBase _newState)
    {
        // Add the state into the state map
        stateMap.put(_newState.GetName(), _newState);
    }

    void ChangeState(String _nextState)
    {
        if (_nextState != null) {
            nextState = _nextState;
            prevState = currState;
        } else {
            nextState = currState;
        }
    }

    void Update(float _dt)
    {
        StateBase curr = stateMap.get(currState);
        if (!Objects.equals(nextState, currState) || currState == null) {
            StateBase next = stateMap.get(nextState);
            if (curr != null) curr.OnExit();
            if (next != null) next.OnEnter(view);
            prevState = currState;
            currState = nextState;
            curr = next;
        }

        if (curr != null)
            curr.Update(_dt);
    }

    void Render(Canvas _canvas)
    {

        StateBase curr = stateMap.get(currState);
        if (curr != null)
            curr.Render(_canvas);
    }

    String GetCurrentState()
    {
        if (currState == null)
            return "INVALID";

        return currState;
    }

    String GetPrevState()
    {
        if (prevState == null)
            return "INVALID";

        return prevState;
    }

    public void Clean(){
        stateMap.clear();
    }
}
