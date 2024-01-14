package com.nypsdm.dx1221_week04;

import android.content.res.Resources;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.view.SurfaceView;

import java.util.HashMap;

public class AudioManager
{
    public final static AudioManager Instance = new AudioManager();

    private SurfaceView view = null;
    private HashMap<Integer, MediaPlayer> audioMap = new HashMap<Integer, MediaPlayer>();

    private AudioManager()
    {

    }

    public void Init(SurfaceView _view)
    {
        view = _view;
        Release();
    }

    private MediaPlayer GetAudio(int _id)
    {
        if (audioMap.containsKey(_id))
        {
            return audioMap.get(_id);
        }

        MediaPlayer result = MediaPlayer.create(view.getContext(), _id);
        audioMap.put(_id, result);
        return result;
    }

    public void PlayAudio(int _id, float _vol, boolean _loop)
    {
        if (audioMap.containsKey(_id))
        {
            //audioMap.get(_id).reset();
            //audioMap.get(_id).start();

            MediaPlayer curr = audioMap.get(_id);
            curr.seekTo(0);
            curr.setVolume(_vol, _vol);
            curr.setLooping(_loop);
            curr.start();
        }
        else
        {
            MediaPlayer curr = MediaPlayer.create(view.getContext(), _id);
            audioMap.put(_id, curr);
            curr.setLooping(_loop);
            curr.start();
        }

        // Load the audio
        /*MediaPlayer newAudio = MediaPlayer.create(view.getContext(), _id);
        audioMap.put(_id, newAudio);
        newAudio.start();*/
    }

    public boolean IsPlaying(int _id)
    {
        if (!audioMap.containsKey(_id))
        {
            return false;
        }

        return audioMap.get(_id).isPlaying();
    }

    public void StopAudio(int _id)
    {
        MediaPlayer curr = audioMap.get(_id);
        curr.stop();
    }

    public void Release()
    {
        for (HashMap.Entry<Integer, MediaPlayer> entry : audioMap.entrySet())
        {
            entry.getValue().stop();
            entry.getValue().reset();
            entry.getValue().release();
        }

        audioMap.clear();
    }
}
