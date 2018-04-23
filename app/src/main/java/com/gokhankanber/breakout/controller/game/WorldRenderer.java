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

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteCache;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.gokhankanber.breakout.model.Ball;
import com.gokhankanber.breakout.model.Block;
import com.gokhankanber.breakout.model.Brick;
import com.gokhankanber.breakout.model.Paddle;
import com.gokhankanber.breakout.model.World;
import com.gokhankanber.breakout.provider.Asset;
import com.gokhankanber.breakout.provider.Config;

public class WorldRenderer
{
    // Board: Score, balls, and player number.
    private final int scoreBoardX = 54;
    private final int scoreBoardWidth = 60;
    private final int digitBlockWidth = 6;
    private final int digitBlockHeight = 1;
    private final int digitWidth = digitBlockWidth * 3;
    private final int boardY = (int) Config.getHeight() - 3 - (digitBlockHeight * 8);
    private final int ballsX = scoreBoardX + scoreBoardWidth + 36;
    private final int playerNumberX = ballsX + digitWidth + 24;

    // Render
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Asset asset;
    private TextureRegion ballTextureRegion, paddleTextureRegion, borderTextureRegion, leftBlockTextureRegion, rightBlockTextureRegion;
    private TextureRegion[] brickTextureRegions;
    private Ball ball;
    private Paddle paddle;
    private Array<Block> border;
    private Block leftBlock, rightBlock;
    private Array<Brick> wall;
    private Board board;
    private SpriteCache cache;
    private int cacheId;
    private int brickRows;

    public WorldRenderer(World world, SpriteBatch batch, OrthographicCamera camera)
    {
        this.batch = batch;
        this.camera = camera;
        asset = world.getAsset();
        ball = world.getBall();
        paddle = world.getPaddle();
        border = world.getBorder();
        leftBlock = world.getBlocks().get(0);
        rightBlock = world.getBlocks().get(1);
        wall = world.getWall();
        brickRows = world.getBrickRows();
        board = new Board();

        // Creates textures for models and creates world.
        createTextureRegions();
        resetWorld();
    }

    /**
     * Draw world and models.
     */
    public void render()
    {
        drawWorld();

        batch.begin();
        batch.draw(ballTextureRegion, ball.getX(), ball.getY(), ball.getWidth(), ball.getHeight());
        batch.draw(paddleTextureRegion, paddle.getX(), paddle.getY(), paddle.getWidth(), paddle.getHeight());
        batch.end();
    }

    /**
     * Releases renderer resources
     */
    public void dispose()
    {
        cache.dispose();

        ballTextureRegion.getTexture().dispose();
        paddleTextureRegion.getTexture().dispose();
        borderTextureRegion.getTexture().dispose();
        leftBlockTextureRegion.getTexture().dispose();
        rightBlockTextureRegion.getTexture().dispose();

        for(TextureRegion textureRegion : brickTextureRegions)
        {
            textureRegion.getTexture().dispose();
        }
    }

    private void createTextureRegions()
    {
        ballTextureRegion = new TextureRegion(asset.getTexture(ball.getColor()));
        paddleTextureRegion = new TextureRegion(asset.getTexture(paddle.getColor()));
        borderTextureRegion = new TextureRegion(asset.getTexture(border.get(0).getColor()));
        leftBlockTextureRegion = new TextureRegion(asset.getTexture(leftBlock.getColor()));
        rightBlockTextureRegion = new TextureRegion(asset.getTexture(rightBlock.getColor()));

        // Wall
        brickTextureRegions = new TextureRegion[brickRows];

        for(Brick brick : wall)
        {
            brickTextureRegions[brick.getIndex()] = new TextureRegion(asset.getTexture(brick.getColor()));
        }
    }

    /**
     * Creates world with a border, up and down walls, and a scoreboard by using SpriteCache.
     */
    public void resetWorld()
    {
        int size = border.size
                + wall.size
                + 2;
        boolean[][] score = board.buildNumber(paddle.points, 3);
        size += board.getSize();
        boolean[][] balls = board.buildNumber(ball.getCount());
        size += board.getSize();
        boolean[][] playerNumber = board.buildNumber(1);
        size += board.getSize();

        if(cache != null)
        {
            cache.dispose();
        }

        cache = new SpriteCache(size, false);
        cache.beginCache();

        addBorder();
        addWalls();
        add(score, scoreBoardX);
        add(balls, ballsX);
        add(playerNumber, playerNumberX);

        cacheId = cache.endCache();
    }

    private void addBorder()
    {
        for(Block block : border)
        {
            cache.add(borderTextureRegion, block.getX(), block.getY(), block.getWidth(), block.getHeight());
        }

        cache.add(leftBlockTextureRegion, leftBlock.getX(), leftBlock.getY(), leftBlock.getWidth(), leftBlock.getHeight());
        cache.add(rightBlockTextureRegion, rightBlock.getX(), rightBlock.getY(), rightBlock.getWidth(), rightBlock.getHeight());
    }

    private void addWalls()
    {
        for(Brick brick : wall)
        {
            cache.add(brickTextureRegions[brick.getIndex()], brick.getX(), brick.getY(), brick.getWidth(), brick.getHeight());
        }
    }

    /**
     * Adds score, balls, and player number to board.
     * @param numberBoard
     */
    private void add(boolean[][] numberBoard, int startX)
    {
        int width = numberBoard.length;
        int height = numberBoard[0].length;

        for(int x = 0; x < width; x++)
        {
            for(int y = 0; y < height; y++)
            {
                if(numberBoard[x][y])
                {
                    cache.add(borderTextureRegion, startX + x * digitBlockWidth, boardY + (height - y - 1) * digitBlockHeight, digitBlockWidth, digitBlockHeight);
                }
            }
        }
    }

    /**
     * Draws world with SpriteCache.
     */
    private void drawWorld()
    {
        cache.setProjectionMatrix(camera.combined);
        cache.begin();
        cache.draw(cacheId);
        cache.end();
    }
}
