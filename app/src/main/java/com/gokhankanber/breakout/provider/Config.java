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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class Config
{
    public static final float WIDTH = 240;
    public static final float BALL_ACCELERATION = 50.0f;
    public static final String KEY_DIFFICULTY = "difficulty";
    public static final String KEY_SOUND = "sound";
    public static int difficulty = 1;
    public static boolean sound = true;

    public static float getHeight()
    {
        float ratio = (float) Gdx.graphics.getHeight() / Gdx.graphics.getWidth();

        return WIDTH * ratio;
    }

    public static void load()
    {
        Preferences preferences = Gdx.app.getPreferences(".breakout");
        difficulty = preferences.getInteger(KEY_DIFFICULTY, 1);
        sound = preferences.getBoolean(KEY_SOUND, true);
    }
}
