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

package com.gokhankanber.breakout.controller.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.gokhankanber.breakout.Breakout;
import com.gokhankanber.breakout.R;
import com.gokhankanber.breakout.controller.BaseController;
import com.gokhankanber.breakout.controller.game.GameInputController.InputListener;
import com.gokhankanber.breakout.model.World;
import com.gokhankanber.breakout.provider.Config;
import com.gokhankanber.breakout.view.MainMenuScreen;

/**
 * Controller class for {@link com.gokhankanber.breakout.view.GameScreen} class.
 * Listens for user input.
 * Creates, updates and renders world.
 * Draws game menu: pause menu, win / game over menu.
 */
public class GameController extends BaseController
{
    // Game menu: pause menu, win / game over menu
    private enum GameMenuType
    {
        PAUSE,
        END
    }

    private final float gameMenuPaddingTop = 12;
    private final float gameMenuWidth = 180;
    private final float gameMenuHeight = 120;
    private final float gameMenuX = (Config.WIDTH - gameMenuWidth) / 2;
    private final float gameMenuY = (Config.getHeight() - gameMenuHeight) / 2;
    private final float gameMenuItemWidth = 120.0f;
    private final float gameMenuItemHeight = 20.0f;
    private final float gameMenuItemX = (Config.WIDTH - gameMenuItemWidth) / 2;
    private final int menuItemFontSize = 16;
    private ShapeRenderer shapeRenderer;
    private Color gameMenuBackgroundColor;
    private Rectangle[] menuItemBounds;
    private Texture[] menuItemTextures;
    private Texture gameEndMessageTexture;
    private String gameEndMessage;
    private String[] gameMenuItems;
    private float gameEndMessageX;
    private float gameEndMessageY;
    private float[] gameMenuItemsFontX;
    private float[] gameMenuItemsFontY;
    private float[] gameMenuItemsBoundY;

    // Input, world, world renderer
    private GameInputController inputController;
    private World world;
    private WorldRenderer worldRenderer;

    public GameController(Breakout game)
    {
        super(game);
    }

    @Override
    public void init()
    {
        super.init();

        // Init input
        inputController = new GameInputController(camera, inputListener);
        Gdx.input.setInputProcessor(inputController);
        Gdx.input.setCatchBackKey(true);

        // Init world
        world = new World(game);
        worldRenderer = new WorldRenderer(world, batch, camera);

        // Init pause menu
        resetGameMenu(GameMenuType.PAUSE);
    }

    @Override
    public void update(float delta)
    {
        // Update world on resume and ready states
        // Init win / game over menu on ending state and set world's state to end
        if(world.isResumed() || world.isReady())
        {
            world.update(delta);

            if(world.isBoardChanged())
            {
                worldRenderer.resetWorld();
                world.resetBoardChanged();

                if(world.isEnding())
                {
                    if(world.isGameOver())
                    {
                        gameEndMessage = game.getResources().getString(R.string.game_over);
                    }
                    else
                    {
                        gameEndMessage = game.getResources().getString(R.string.win);
                    }

                    Gdx.app.postRunnable(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            resetGameMenu(GameMenuType.END);
                            world.end();
                        }
                    });
                }
            }
        }
    }

    @Override
    public void draw(float delta)
    {
        clear();

        // Render world
        worldRenderer.render();

        // Draw game menu: pause menu, win / game over menu
        if(world.isPaused() || world.isEnd())
        {
            drawGameMenu();
        }
    }

    @Override
    public void release()
    {
        super.release();

        for(Texture texture : menuItemTextures)
        {
            texture.dispose();
        }

        if(gameEndMessageTexture != null)
        {
            gameEndMessageTexture.dispose();
        }

        shapeRenderer.dispose();
        worldRenderer.dispose();
    }

    /**
     * Resets game menu content and creates game menu.
     * Creates menu item click bounds.
     * @param gameMenuType is used to reset menu items.
     */
    private void resetGameMenu(GameMenuType gameMenuType)
    {
        switch(gameMenuType)
        {
            case PAUSE:
                pauseMenuItems();
                break;
            case END:
                endMenuItems();
                break;
        }

        initGameMenu();

        int menuItemsLength = gameMenuItems.length;
        menuItemBounds = new Rectangle[menuItemsLength];

        for(int i = 0; i < gameMenuItems.length; i++)
        {
            menuItemBounds[i] = new Rectangle(gameMenuItemX, gameMenuItemsBoundY[i], gameMenuItemWidth, gameMenuItemHeight);
        }
    }

    private void pauseMenuItems()
    {
        // Get string values from res/values/strings.xml resource files.
        gameMenuItems = new String[]{
                game.getResources().getString(R.string.resume),
                game.getResources().getString(R.string.new_game),
                game.getResources().getString(R.string.exit)
        };
    }

    private void endMenuItems()
    {
        gameMenuItems = new String[]{
                game.getResources().getString(R.string.new_game),
                game.getResources().getString(R.string.exit)
        };
    }

    /**
     * Creates black transparent background of game menu by using ShapeRenderer.
     * Creates a font and sets x and y coordinates of the content by using GlyphLayout.
     */
    private void initGameMenu()
    {
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(camera.combined);
        gameMenuBackgroundColor = new Color(0.0f, 0.0f, 0.0f, 0.9f);

        int menuItemsLength = gameMenuItems.length;
        gameMenuItemsFontX = new float[menuItemsLength];
        gameMenuItemsFontY = new float[menuItemsLength];
        gameMenuItemsBoundY = new float[menuItemsLength];
        menuItemTextures = new Texture[menuItemsLength];
        float height = gameMenuHeight;

        if(world.isEnding())
        {
            gameEndMessageTexture = asset.getTexture(gameEndMessage);
            gameEndMessageX = (Config.WIDTH - gameEndMessage.length() * menuItemFontSize) / 2;
            gameEndMessageY = height + gameMenuY - gameMenuPaddingTop - (gameMenuItemHeight + menuItemFontSize) / 2;
            height -= (gameMenuPaddingTop + gameMenuItemHeight);
        }

        for(int i = 0; i < menuItemsLength; i++)
        {
            menuItemTextures[i] = asset.getTexture(gameMenuItems[i]);
            gameMenuItemsFontX[i] = (Config.WIDTH - gameMenuItems[i].length() * menuItemFontSize) / 2;
            gameMenuItemsBoundY[i] = (height + 2 * gameMenuY + (menuItemsLength * gameMenuItemHeight)) / 2 - (i + 1) * gameMenuItemHeight;
            gameMenuItemsFontY[i] = gameMenuItemsBoundY[i] + (gameMenuItemHeight - menuItemFontSize) / 2;
        }
    }

    /**
     * Draws game menu.
     * If world state is end, shows game end message: win / game over.
     * Draws menu items: Resume, New.
     */
    private void drawGameMenu()
    {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        // Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(gameMenuBackgroundColor);
        shapeRenderer.rect(gameMenuX, gameMenuY, gameMenuWidth, gameMenuHeight);
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        batch.begin();

        if(world.isEnd())
        {
            batch.draw(gameEndMessageTexture, gameEndMessageX, gameEndMessageY, gameEndMessage.length() * menuItemFontSize, menuItemFontSize);
        }

        for(int i = 0; i < gameMenuItems.length; i++)
        {
            batch.draw(menuItemTextures[i], gameMenuItemsFontX[i], gameMenuItemsFontY[i], gameMenuItems[i].length() * menuItemFontSize, menuItemFontSize);
        }

        batch.end();
    }

    private InputListener inputListener = new InputListener()
    {
        @Override
        public void back()
        {
            asset.playButtonSound();

            // Back button toggles between pause and resume.
            if(world.isResumed())
            {
                world.pause();
            }
            else if(world.isPaused())
            {
                world.resume();
            }
        }

        @Override
        public void move(float amount)
        {
            // Move by amount of change in x coordinate.
            if(world.isResumed())
            {
                world.getPaddle().move(amount);
            }
        }

        @Override
        public void check(float x, float y)
        {
            // Check user touch for world states.
            if(world.isReady())
            {
                world.resume();
            }
            else if(world.isPaused())
            {
                if(menuItemBounds[0].contains(x, y))
                {
                    // Pause menu: Resume.
                    asset.playButtonSound();
                    world.resume();
                }
                else if(menuItemBounds[1].contains(x, y))
                {
                    // Pause menu: New.
                    asset.playButtonSound();
                    world.newGame();
                }
                else if(menuItemBounds[2].contains(x, y))
                {
                    // Pause menu: Exit.
                    asset.playButtonSound();
                    game.setScreen(new MainMenuScreen(game));
                }
            }
            else if(world.isEnd())
            {
                if(menuItemBounds[0].contains(x, y))
                {
                    // Win / game over menu: New.
                    asset.playButtonSound();
                    world.newGame();
                    resetGameMenu(GameMenuType.PAUSE);
                }
                else if(menuItemBounds[1].contains(x, y))
                {
                    // Win / game over menu: Exit.
                    asset.playButtonSound();
                    game.setScreen(new MainMenuScreen(game));
                }
            }
        }
    };
}
