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

package com.gokhankanber.breakout.controller.settings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.gokhankanber.breakout.Breakout;
import com.gokhankanber.breakout.R;
import com.gokhankanber.breakout.controller.BaseController;
import com.gokhankanber.breakout.controller.settings.SettingsInputController.InputListener;
import com.gokhankanber.breakout.provider.Config;
import com.gokhankanber.breakout.view.MainMenuScreen;

/**
 * Controller class for {@link com.gokhankanber.breakout.view.SettingsScreen}
 */
public class SettingsController extends BaseController
{
    private final float blockSize = 20.0f;
    private final float blockPadding = 5.0f;
    private final int colorTitle = 0xffffffff;
    private final int colorValue = 0x8e8e8eff;
    private final int colorEnabled = 0x48a048ff;

    // Logo
    private final int logoTextureWidth = 192;
    private final int logoTextureHeight = 8;
    private final float logoX = (Config.WIDTH - logoTextureWidth) / 2;
    private final float logoY = Config.getHeight() - blockSize - logoTextureHeight;
    private Texture logoTexture;

    // Settings menu
    private final int fontSize = 8;
    private final float difficultyY = logoY - 2 * blockPadding - fontSize;
    private final float difficultyValuesY = difficultyY - (blockSize + fontSize) / 2;
    private final float difficultyValuesBoundY = difficultyY - blockSize;
    private final float soundY = difficultyValuesBoundY - fontSize;
    private final float soundValuesY = soundY - (blockSize + fontSize) / 2;
    private final float soundValuesBoundY = soundY - blockSize;
    private float difficultyNormalX;
    private float difficultyHardX;
    private float soundOffX;
    private Texture[] settingsMenuItemTextures;
    private Texture[] difficultyValueTextures;
    private Texture[] soundValueTextures;
    private String[] settingsMenuItems;
    private String[] difficultyValues;
    private String[] soundValues;
    private Rectangle[] difficultyValueBounds;
    private Rectangle[] soundValueBounds;

    // Back button
    private Texture backButtonTexture;
    private Rectangle backButtonBounds;

    private SettingsInputController inputController;
    private Preferences preferences;

    public SettingsController(Breakout game)
    {
        super(game);
    }

    @Override
    public void init()
    {
        super.init();

        inputController = new SettingsInputController(camera, inputListener);
        Gdx.input.setInputProcessor(inputController);
        Gdx.input.setCatchBackKey(true);

        preferences = Gdx.app.getPreferences(".breakout");

        logoTexture = asset.getLogoTexture();
        backButtonTexture = asset.getTexture(game.getResources().getString(R.string.back));
        backButtonBounds = new Rectangle(blockPadding, blockPadding, blockSize, blockSize);
        initSettingsMenu();
    }

    private void initSettingsMenu()
    {
        settingsMenuItems = new String[]{
                game.getResources().getString(R.string.difficulty),
                game.getResources().getString(R.string.sound)
        };

        difficultyValues = new String[]{
                game.getResources().getString(R.string.easy),
                game.getResources().getString(R.string.normal),
                game.getResources().getString(R.string.hard)
        };

        soundValues = new String[]{
                game.getResources().getString(R.string.on),
                game.getResources().getString(R.string.off)
        };

        settingsMenuItemTextures = new Texture[settingsMenuItems.length];
        difficultyValueTextures = new Texture[difficultyValues.length];
        soundValueTextures = new Texture[soundValues.length];

        createTextures(0, settingsMenuItemTextures, settingsMenuItems);
        createTextures(1, difficultyValueTextures, difficultyValues);
        createTextures(2, soundValueTextures, soundValues);

        difficultyNormalX = 2 * blockPadding + difficultyValues[0].length() * fontSize;
        difficultyHardX = blockPadding + difficultyNormalX + difficultyValues[1].length() * fontSize;
        soundOffX = 2 * blockPadding + soundValues[0].length() * fontSize;

        difficultyValueBounds = new Rectangle[3];
        difficultyValueBounds[0] = new Rectangle(blockPadding, difficultyValuesBoundY, difficultyValues[0].length() * fontSize, blockSize);
        difficultyValueBounds[1] = new Rectangle(difficultyNormalX, difficultyValuesBoundY, difficultyValues[1].length() * fontSize, blockSize);
        difficultyValueBounds[2] = new Rectangle(difficultyHardX, difficultyValuesBoundY, difficultyValues[2].length() * fontSize, blockSize);

        soundValueBounds = new Rectangle[2];
        soundValueBounds[0] = new Rectangle(blockPadding, soundValuesBoundY, soundValues[0].length() * fontSize, blockSize);
        soundValueBounds[1] = new Rectangle(soundOffX, soundValuesBoundY, soundValues[1].length() * fontSize, blockSize);
    }

    @Override
    public void update(float delta)
    {
    }

    @Override
    public void draw(float delta)
    {
        clear();

        batch.begin();
        batch.draw(logoTexture, logoX, logoY, logoTextureWidth, logoTextureHeight);
        batch.draw(settingsMenuItemTextures[0], blockPadding, difficultyY, settingsMenuItems[0].length() * fontSize, fontSize);
        batch.draw(difficultyValueTextures[0], blockPadding, difficultyValuesY, difficultyValues[0].length() * fontSize, fontSize);
        batch.draw(difficultyValueTextures[1], difficultyNormalX, difficultyValuesY, difficultyValues[1].length() * fontSize, fontSize);
        batch.draw(difficultyValueTextures[2], difficultyHardX, difficultyValuesY, difficultyValues[2].length() * fontSize, fontSize);
        batch.draw(settingsMenuItemTextures[1], blockPadding, soundY, settingsMenuItems[1].length() * fontSize, fontSize);
        batch.draw(soundValueTextures[0], blockPadding, soundValuesY, soundValues[0].length() * fontSize, fontSize);
        batch.draw(soundValueTextures[1], soundOffX, soundValuesY, soundValues[1].length() * fontSize, fontSize);
        batch.draw(backButtonTexture, blockPadding, blockPadding, blockSize, blockSize);
        batch.end();
    }

    @Override
    public void release()
    {
        super.release();

        logoTexture.dispose();

        for(Texture texture : settingsMenuItemTextures)
        {
            texture.dispose();
        }

        backButtonTexture.dispose();
    }

    private void createTextures(int index, Texture[] textures, String[] values)
    {
        int i = 0;
        int color = colorTitle;

        for(String item : values)
        {
            if(index == 1)
            {
                color = (i == Config.difficulty ? colorEnabled : colorValue);
            }
            else if(index == 2)
            {
                color = ((i == 0 && Config.sound) || (i == 1 && !Config.sound) ? colorEnabled : colorValue);
            }

            textures[i] = asset.getTexture(item, color);
            i++;
        }
    }

    private void setValue(int value)
    {
        asset.playButtonSound();
        Config.difficulty = value;
        preferences.putInteger(Config.KEY_DIFFICULTY, value);
        preferences.flush();
        createTextures(1, difficultyValueTextures, difficultyValues);
    }

    private void setValue(boolean value)
    {
        asset.playButtonSound();
        Config.sound = value;
        preferences.putBoolean(Config.KEY_SOUND, value);
        preferences.flush();
        createTextures(2, soundValueTextures, soundValues);
    }

    private InputListener inputListener = new InputListener()
    {
        @Override
        public void back()
        {
            asset.playButtonSound();
            game.setScreen(new MainMenuScreen(game));
        }

        @Override
        public void check(float x, float y)
        {
            if(difficultyValueBounds[0].contains(x, y))
            {
                setValue(0);
            }
            else if(difficultyValueBounds[1].contains(x, y))
            {
                setValue(1);
            }
            else if(difficultyValueBounds[2].contains(x, y))
            {
                setValue(2);
            }
            else if(soundValueBounds[0].contains(x, y))
            {
                setValue(true);
            }
            else if(soundValueBounds[1].contains(x, y))
            {
                setValue(false);
            }
            else if(backButtonBounds.contains(x, y))
            {
                asset.playButtonSound();
                game.setScreen(new MainMenuScreen(game));
            }
        }
    };
}
