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

package com.gokhankanber.breakout.model;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.gokhankanber.breakout.Breakout;
import com.gokhankanber.breakout.provider.Asset;
import com.gokhankanber.breakout.provider.Config;

/**
 * Creates models.
 * Updates and checks score.
 * Manages world's state.
 */
public class World
{
    // State
    private enum State
    {
        READY,
        PAUSE,
        RESUME,
        ENDING,
        END
    }

    private State state = State.READY;
    private float stateTime = 0;
    private boolean boardChanged = false;
    private boolean gameOver = false;

    // Asset
    private Asset asset;

    private final int maxRound = 2;
    private int round = 0;

    // Models
    // Block Properties
    private final int blockSize = 12;

    // Brick Properties
    private final int paddingWall = 18;
    private final String topBrickColor = "#c84848";
    private final String secondLevelBrickColor = "#c66c3a";
    private final String thirdLevelBrickColor = "#b47a30";
    private final String fourthLevelBrickColor = "#a2a22a";
    private final String fifthLevelBrickColor = "#48a048";
    private final String sixthLevelBrickColor = "#4248c8";
    private final String wallColor = "#8e8e8e";
    private final String leftBottomBlockColor = "#429e82";
    private final String rightBottomBlockColor = "#c84848";
    private final int brickHighPoints = 7;
    private final int brickMiddlePoints = 4;
    private final int brickLowPoints = 1;
    private final int brickRows = 6;
    private final int rowBrickCount = 18;
    private final int brickWidth = 12;
    private final int brickHeight = 4;
    private final int speedBrickVelocityRatio = 2;

    // Paddle Properties
    private final String paddleColor = "#c84848";
    private final float paddleWidthRatio = (Config.difficulty == 0 ? (float) 5 / 4 : (Config.difficulty == 2 ? (float) 3 / 4 : 1));
    private final float paddleWidth = 24 * paddleWidthRatio;
    private final int paddleHeight = 3;
    private final int paddleSectionNumber = 5;
    private final Vector2 paddleStartPoint = new Vector2((Config.WIDTH - paddleWidth) / 2, blockSize + paddleHeight);

    // Ball Properties
    private final String ballColor = "#c84848";
    private final int ballSize = 3;
    private final int ballNumber = 5;
    private final Vector2 ballStartPoint = new Vector2(blockSize, Config.getHeight() - 3 * blockSize - paddingWall - brickRows * brickHeight);

    private Array<Block> border;
    private Array<Block> blocks;
    private Array<Brick> wall;
    private Paddle paddle;
    private Ball ball;

    public World(Breakout game)
    {
        // Get asset, create models.
        asset = game.getAsset();
        createBorder();
        createBlocks();
        createWall();
        paddle = new Paddle(paddleStartPoint.x, paddleStartPoint.y, paddleWidth, paddleHeight, paddleColor, paddleSectionNumber);
        paddle.setListener(iWorld);
        ball = new Ball(ballStartPoint.x, ballStartPoint.y, ballSize, ballSize, ballColor, ballNumber);
        ball.setListener(iWorld);
    }

    public boolean isBoardChanged()
    {
        return boardChanged;
    }

    public void resetBoardChanged()
    {
        boardChanged = false;
    }

    private void newBall()
    {
        asset.playBallOutSound();

        int count = ball.getCount();

        if(count > 0)
        {
            ball.reset(ballStartPoint.x, ballStartPoint.y, --count);
        }
        else
        {
            gameOver = true;
            ending();
        }

        boardChanged = true;
    }

    private void removeBrick(int index, int number)
    {
        asset.playBrickSound(index);

        // speed up ball
        if(!ball.getSpeedBrickHit() && wall.get(number).isSpeedBrick())
        {
            ball.speed(speedBrickVelocityRatio);
            ball.setSpeedBrickHit(true);
        }

        // add points
        paddle.points += wall.get(number).getPoints();

        // remove brick
        wall.removeIndex(number);

        if(wall.size == 0)
        {
            round++;

            if(round < maxRound)
            {
                createWall();
            }
            else
            {
                ending();
            }
        }

        // notify board is changed
        boardChanged = true;
    }

    public boolean isGameOver()
    {
        return gameOver;
    }

    /**
     * Resets world for a new game.
     */
    public void newGame()
    {
        createWall();
        paddle.reset(paddleStartPoint.x, paddleStartPoint.y);
        ball.reset(ballStartPoint.x, ballStartPoint.y, ballNumber);
        state = State.READY;
        gameOver = false;
        round = 0;
        boardChanged = true;
    }

    public void pause()
    {
        state = State.PAUSE;
    }

    public void resume()
    {
        state = State.RESUME;
    }

    public void ending()
    {
        state = State.ENDING;
    }

    public void end()
    {
        state = State.END;
    }

    public boolean isReady()
    {
        return state == State.READY;
    }

    public boolean isPaused()
    {
        return state == State.PAUSE;
    }

    public boolean isResumed()
    {
        return state == State.RESUME;
    }

    public boolean isEnding()
    {
        return state == State.ENDING;
    }

    public boolean isEnd()
    {
        return state == State.END;
    }

    private void createBorder()
    {
        border = new Array<>();
        border.add(new Block(0, 1.5f * blockSize, blockSize, Config.getHeight() - 2.5f * blockSize, wallColor));
        border.add(new Block(blockSize, Config.getHeight() - 2 * blockSize, Config.WIDTH - 2 * blockSize, blockSize, wallColor));
        border.add(new Block(Config.WIDTH - blockSize, 1.5f * blockSize, blockSize, Config.getHeight() - 2.5f * blockSize, wallColor));
    }

    private void createBlocks()
    {
        blocks = new Array<>();
        blocks.add(new Block(0, blockSize, blockSize, blockSize / 2, leftBottomBlockColor));
        blocks.add(new Block(Config.WIDTH - blockSize, blockSize, blockSize, blockSize / 2, rightBottomBlockColor));
    }

    private void createWall()
    {
        if(wall == null)
        {
            wall = new Array<>();
        }
        else
        {
            wall.clear();
        }

        for(int i = 0; i < brickRows; i++)
        {
            for(int j = 0; j < rowBrickCount; j++)
            {
                Brick brick = new Brick(blockSize + (j * brickWidth), Config.getHeight() - blockSize - blockSize - paddingWall - (i + 1) * brickHeight, brickWidth, brickHeight, getColor(i));
                brick.setIndex(i);
                brick.setPoints(getBrickPoints(i));

                if(i < 3)
                {
                    brick.setSpeedBrick();
                }

                wall.add(brick);
            }
        }
    }

    private String getColor(int i)
    {
        switch(i)
        {
            case 0:
                return topBrickColor;
            case 1:
                return secondLevelBrickColor;
            case 2:
                return thirdLevelBrickColor;
            case 3:
                return fourthLevelBrickColor;
            case 4:
                return fifthLevelBrickColor;
            case 5:
                return sixthLevelBrickColor;
            default:
                return sixthLevelBrickColor;
        }
    }

    private int getBrickPoints(int i)
    {
        switch(i)
        {
            case 0:
                return brickHighPoints;
            case 1:
                return brickHighPoints;
            case 2:
                return brickMiddlePoints;
            case 3:
                return brickMiddlePoints;
            case 4:
                return brickLowPoints;
            case 5:
                return brickLowPoints;
            default:
                return brickLowPoints;
        }
    }

    public Asset getAsset()
    {
        return asset;
    }

    public Ball getBall()
    {
        return ball;
    }

    public Paddle getPaddle()
    {
        return paddle;
    }

    public Array<Block> getBorder()
    {
        return border;
    }

    public Array<Block> getBlocks()
    {
        return blocks;
    }

    public Array<Brick> getWall()
    {
        return wall;
    }

    public int getBrickRows()
    {
        return brickRows;
    }

    public void update(float delta)
    {
        if(isResumed())
        {
            ball.update(delta);
        }
        else if(isReady())
        {
            // Wait for 3 seconds on game start or touch to start.
            wait(delta, 3);
        }
    }

    public void wait(float delta, int waitTime)
    {
        if(stateTime >= waitTime)
        {
            stateTime = 0;
            resume();
        }
        else
        {
            stateTime += delta;
        }
    }

    // World interface instance for models.
    private IWorld iWorld = new IWorld()
    {
        @Override
        public Paddle getPaddle()
        {
            return paddle;
        }

        @Override
        public Array<Block> getBorder()
        {
            return border;
        }

        @Override
        public Array<Block> getBlocks()
        {
            return blocks;
        }

        @Override
        public Array<Brick> getWall()
        {
            return wall;
        }

        @Override
        public void newBall()
        {
            World.this.newBall();
        }

        @Override
        public void removeBrick(int index, int number)
        {
            World.this.removeBrick(index, number);
        }

        @Override
        public void playPaddleSound()
        {
            asset.playPaddleSound();
        }

        @Override
        public void playTopBorderSound()
        {
            asset.playTopBorderSound();
        }

        @Override
        public void playSideBorderSound()
        {
            asset.playSideBorderSound();
        }
    };
}
