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

package com.gokhankanber.breakout.controller.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.gokhankanber.breakout.Breakout;
import com.gokhankanber.breakout.R;
import com.gokhankanber.breakout.controller.BaseController;
import com.gokhankanber.breakout.controller.main.MainInputController.InputListener;
import com.gokhankanber.breakout.provider.Config;
import com.gokhankanber.breakout.view.GameScreen;
import com.gokhankanber.breakout.view.SettingsScreen;

/**
 * Controller class for {@link com.gokhankanber.breakout.view.MainMenuScreen}
 */
public class MainMenuController extends BaseController
{
    private final float blockSize = 20.0f;

    // Logo
    private final int logoTextureWidth = 192;
    private final int logoTextureHeight = 8;
    private Texture logoTexture;
    private float logoX;
    private float logoY;

    // Main menu
    private final float mainMenuItemWidth = 120.0f;
    private final float mainMenuItemX = (Config.WIDTH - mainMenuItemWidth) / 2;
    private final int menuItemFontSize = 16;
    private Texture[] menuItemTextures;
    private float mainMenuHeight;
    private float[] mainMenuItemsFontX;
    private float[] mainMenuItemsFontY;
    private float[] mainMenuItemsBoundY;
    private String[] mainMenuItems;
    private Rectangle playBounds;
    private Rectangle settingsBounds;
    private MainInputController inputController;

    public MainMenuController(Breakout game)
    {
        super(game);
    }

    @Override
    public void init()
    {
        super.init();

        // Init input.
        inputController = new MainInputController(camera, inputListener);
        Gdx.input.setInputProcessor(inputController);
        Gdx.input.setCatchBackKey(false);

        // Init logo and main menu.
        initLogo();
        initMainMenu();
    }

    /**
     * Creates logo font and sets x and y coordinates.
     */
    private void initLogo()
    {
        logoTexture = asset.getLogoTexture();
        logoX = (Config.WIDTH - logoTextureWidth) / 2;
        logoY = Config.getHeight() - blockSize - logoTextureHeight;
    }

    /**
     * Creates main menu font and sets x and y coordinates.
     * Creates menu item click bounds.
     */
    private void initMainMenu()
    {
        mainMenuItems = new String[]{
                game.getResources().getString(R.string.play),
                game.getResources().getString(R.string.settings)
        };

        int menuItemsLength = mainMenuItems.length;
        mainMenuItemsFontX = new float[menuItemsLength];
        mainMenuItemsFontY = new float[menuItemsLength];
        mainMenuItemsBoundY = new float[menuItemsLength];
        mainMenuHeight = Config.getHeight() - blockSize - logoTextureHeight;

        int i = 0;
        menuItemTextures = new Texture[menuItemsLength];

        for(String item : mainMenuItems)
        {
            menuItemTextures[i] = asset.getTexture(item);
            mainMenuItemsFontX[i] = (Config.WIDTH - item.length() * menuItemFontSize) / 2;
            mainMenuItemsBoundY[i] = (mainMenuHeight + (menuItemsLength * blockSize)) / 2 - (i + 1) * blockSize;
            mainMenuItemsFontY[i] = mainMenuItemsBoundY[i] + (blockSize - menuItemFontSize) / 2;
            i++;
        }

        playBounds = new Rectangle(mainMenuItemX, mainMenuItemsBoundY[0], mainMenuItemWidth, blockSize);
        settingsBounds = new Rectangle(mainMenuItemX, mainMenuItemsBoundY[1], mainMenuItemWidth, blockSize);
    }

    @Override
    public void update(float delta)
    {
    }

    @Override
    public void draw(float delta)
    {
        clear();

        // Draws logo and main menu.
        batch.begin();
        batch.draw(logoTexture, logoX, logoY, logoTextureWidth, logoTextureHeight);

        int menuItemsLength = mainMenuItems.length;

        for(int i = 0; i < menuItemsLength; i++)
        {
            batch.draw(menuItemTextures[i], mainMenuItemsFontX[i], mainMenuItemsFontY[i], mainMenuItems[i].length() * menuItemFontSize, menuItemFontSize);
        }

        batch.end();
    }

    @Override
    public void release()
    {
        super.release();

        logoTexture.dispose();

        for(Texture texture : menuItemTextures)
        {
            texture.dispose();
        }
    }

    private InputListener inputListener = new InputListener()
    {
        @Override
        public void check(float x, float y)
        {
            // Check user touch
            if(playBounds.contains(x, y))
            {
                // Main menu: Play.
                asset.playButtonSound();
                game.setScreen(new GameScreen(game));
            }
            else if(settingsBounds.contains(x, y))
            {
                // Main menu: Settings.
                asset.playButtonSound();
                game.setScreen(new SettingsScreen(game));
            }
        }
    };
}
