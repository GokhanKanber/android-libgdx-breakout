/*
 * Copyright 2018 Gökhan Kanber
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gokhankanber.breakout.provider;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

public class Sound
{
    public enum Track
    {
        BUTTON(0, 600, 0.1f),
        TOP_BRICK(1, 466.2f, 0.1f),
        SECOND_LEVEL_BRICK(2, 392, 0.1f),
        THIRD_LEVEL_BRICK(3, 311.1f, 0.1f),
        FOURTH_LEVEL_BRICK(4, 277.2f, 0.1f),
        FIFTH_LEVEL_BRICK(5, 233.1f, 0.1f),
        SIXTH_LEVEL_BRICK(6, 185, 0.1f),
        TOP_BORDER(7, 1760, 0.1f),
        SIDE_BORDER(8, 1046.5f, 0.1f),
        PADDLE(9, 587.3f, 0.1f),
        BALL_OUT(10, 490, 0.257f);

        private final int index;
        private final float frequency;
        private final float duration;

        Track(int index, float frequency, float duration)
        {
            this.index = index;
            this.frequency = frequency;
            this.duration = duration;
        }

        public int getIndex()
        {
            return index;
        }

        public float getFrequency()
        {
            return frequency;
        }

        public float getDuration()
        {
            return duration;
        }
    }

    private AudioTrack[] audioTracks;
    private static Sound instance;

    private Sound()
    {
    }

    public static Sound get()
    {
        if(instance == null)
        {
            instance = new Sound();
            instance.init();
        }

        return instance;
    }

    public void init()
    {
        Track[] trackList = Track.values();
        audioTracks = new AudioTrack[trackList.length];

        for(Track track : trackList)
        {
            audioTracks[track.index] = create(track.frequency, track.duration);
        }
    }

    private AudioTrack create(float frequency, float duration)
    {
        int sampleRate = 48000;
        byte[] bytes = new byte[(int) (sampleRate * duration)];

        for(int i = 0; i < bytes.length; i++)
        {
            bytes[i] = (byte) (Math.sin(2 * Math.PI * i * frequency / sampleRate) * Byte.MAX_VALUE);
        }

        AudioTrack track = new AudioTrack(AudioManager.STREAM_MUSIC,
                sampleRate,
                AudioFormat.CHANNEL_OUT_DEFAULT,
                AudioFormat.ENCODING_PCM_8BIT, bytes.length,
                AudioTrack.MODE_STATIC);

        track.write(bytes, 0, bytes.length);
        track.setStereoVolume(0.5f, 0.5f);

        return track;
    }

    public void play(int index)
    {
        if(audioTracks[index].getPlayState() == AudioTrack.PLAYSTATE_PLAYING)
        {
            audioTracks[index].stop();
        }

        if(Config.sound)
        {
            audioTracks[index].play();
        }
    }

    public void releaseAll()
    {
        for(AudioTrack audioTrack : audioTracks)
        {
            audioTrack.release();
        }

        instance = null;
    }
}
