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

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.gokhankanber.breakout.provider.Config;

public class Ball extends Model
{
    private final int firstSectionAngle = 15; // 3. section
    private final int secondSectionAngle = 30; // 2. and 4. sections
    private final int thirdSectionAngle = 45; // 1. and 5. sections
    private final int hitAngleIncrement = 5;
    private final int hitCountOne = 3;
    private final int hitCountTwo = 7;
    private final int hitCountThree = 11;
    private final int hitCountMax = 12;
    private final float hitCountSpeedRatio = 1.1f;
    private final float ballSpeedRatio = (Config.difficulty == 0 ? (float) 3 / 4 : (Config.difficulty == 2 ? (float) 5 / 4 : 1));
    private final int negativeDirection = -1;
    private int count;
    private boolean speedBrickHit;
    private float ratio;
    private int hit = 0;
    private int hitCountAngle = 0;
    private Array<Block> border;
    private Array<Block> blocks;
    private Array<Brick> wall;
    private Paddle paddle;

    public Ball(float x, float y, float width, float height, String color, int count)
    {
        super(x, y, width, height, color);

        setAcceleration(Config.BALL_ACCELERATION * ballSpeedRatio);
        this.count = count;
    }

    @Override
    public void setListener(IWorld iWorld)
    {
        super.setListener(iWorld);

        border = iWorld.getBorder();
        blocks = iWorld.getBlocks();
        wall = iWorld.getWall();
        paddle = iWorld.getPaddle();
    }

    @Override
    public void update(float delta)
    {
        // Set state time, velocity, and bounds.
        stateTime += delta;

        if(ratio < 1)
        {
            velocity.x = direction.x * acceleration.y * delta * ratio;
            velocity.y = direction.y * acceleration.y * delta;
        }
        else
        {
            velocity.x = direction.x * acceleration.x * delta;
            velocity.y = direction.y * acceleration.x * delta / ratio;
        }

        // Check collision with bounds.
        checkCollision();

        // Set position.
        position.x = bounds.x;
        position.y = bounds.y;

        // Checks ball position for score.
        checkWorld();
    }

    @Override
    protected void checkCollision()
    {
        bounds.x += velocity.x;

        // Check collision with left and right border.
        if(checkCollisionX(border.get(0).bounds))
        {
            iWorld.playSideBorderSound();
        }

        if(checkCollisionX(border.get(2).bounds))
        {
            iWorld.playSideBorderSound();
        }

        // Check collision with left and right blocks.
        if(checkCollisionX(blocks.get(0).bounds))
        {
            iWorld.playSideBorderSound();
        }

        if(checkCollisionX(blocks.get(1).bounds))
        {
            iWorld.playSideBorderSound();
        }

        // Check collision with wall.
        int i = 0;

        for(Brick brick : wall)
        {
            if(checkCollisionX(brick.bounds))
            {
                iWorld.removeBrick(brick.getIndex(), i);
            }

            i++;
        }

        // Check collision with paddle.
        checkCollisionPaddleX();

        bounds.y += velocity.y;

        // Check collision with top border.
        if(checkCollisionY(border.get(1).bounds))
        {
            iWorld.playTopBorderSound();
        }

        // Check collision with wall.
        i = 0;

        for(Brick brick : wall)
        {
            if(checkCollisionY(brick.bounds))
            {
                iWorld.removeBrick(brick.getIndex(), i);
            }

            i++;
        }

        // Check collision with paddle.
        checkCollisionPaddleY();
    }

    /**
     * Resets ball position and state after new score.
     * @param x coordinate of ball.
     * @param y coordinate of ball.
     */
    public void reset(float x, float y, int count)
    {
        setPosition(x, y);
        resetStateTime();
        setAcceleration(Config.BALL_ACCELERATION * ballSpeedRatio);
        speedBrickHit = false;
        this.count = count;
    }

    public void setAcceleration(float accelerationValue)
    {
        acceleration.x = accelerationValue;
        acceleration.y = accelerationValue;
        direction.x = 1;
        direction.y = -1;
        ratio = 1;
    }

    public int getCount()
    {
        return count;
    }

    public boolean getSpeedBrickHit()
    {
        return speedBrickHit;
    }

    public void setSpeedBrickHit(boolean speedBrickHit)
    {
        this.speedBrickHit = speedBrickHit;
    }

    public void speed(float ratio)
    {
        if(ratio != 0)
        {
            acceleration.x *= ratio;
            acceleration.y *= ratio;
        }
    }

    private void checkHit()
    {
        hit++;

        int mod = hit % hitCountMax;

        if(mod == hitCountOne || mod == hitCountTwo || mod == hitCountThree)
        {
            hitCountAngle += hitAngleIncrement;
        }
        else if(mod == 0)
        {
            hitCountAngle = 0;
            speed(hitCountSpeedRatio);
        }
    }

    /**
     * Checks collision on x-axis.
     * @param rectangle is the model to check.
     */
    private boolean checkCollisionX(Rectangle rectangle)
    {
        if(bounds.overlaps(rectangle))
        {
            if(velocity.x < 0)
            {
                bounds.x = rectangle.x + rectangle.width;
            }
            else if(velocity.x > 0)
            {
                bounds.x = rectangle.x - bounds.width;
            }

            direction.x *= negativeDirection;

            return true;
        }

        return false;
    }

    /**
     * Checks collision on y-axis.
     * @param rectangle is the model to check.
     */
    private boolean checkCollisionY(Rectangle rectangle)
    {
        if(bounds.overlaps(rectangle))
        {
            if(velocity.y < 0)
            {
                bounds.y = rectangle.y + rectangle.height;
            }
            else if(velocity.y > 0)
            {
                bounds.y = rectangle.y - bounds.height;
            }

            direction.y *= negativeDirection;

            return true;
        }

        return false;
    }

    private void checkCollisionPaddleX()
    {
        if(bounds.overlaps(paddle.bounds))
        {
            float paddleVelocityRatio = Math.abs(paddle.velocity.x) + 1;

            if(velocity.x < 0)
            {
                if(paddle.velocity.x >= 0)
                {
                    bounds.x = paddle.bounds.x + paddle.getWidth();
                    direction.x *= negativeDirection;
                }
                else
                {
                    bounds.x = paddle.bounds.x - bounds.width;
                }
            }
            else if(velocity.x > 0)
            {
                if(paddle.velocity.x <= 0)
                {
                    bounds.x = paddle.bounds.x - bounds.width;
                    direction.x *= negativeDirection;
                }
                else
                {
                    bounds.x = paddle.bounds.x + paddle.getWidth();
                }
            }

            speed(paddleVelocityRatio);
            iWorld.playPaddleSound();
        }
    }

    /**
     * Checks collision for paddle.
     * Sets ball direction according to ball direction and paddle section.
     * Sets ball angle for each paddle section.
     */
    private void checkCollisionPaddleY()
    {
        if(bounds.overlaps(paddle.bounds))
        {
            checkHit();
            int angle;

            if(getCenter() > paddle.bounds.x + 4 * paddle.getSectionWidth())
            {
                // last section
                angle = thirdSectionAngle + hitCountAngle;

                if(velocity.x < 0)
                {
                    direction.x *= negativeDirection;
                }
            }
            else if(getCenter() > paddle.bounds.x + 3 * paddle.getSectionWidth())
            {
                angle = secondSectionAngle + hitCountAngle;

                if(velocity.x < 0)
                {
                    direction.x *= negativeDirection;
                }
            }
            else if(getCenter() > paddle.bounds.x + 2 * paddle.getSectionWidth())
            {
                angle = firstSectionAngle + hitCountAngle;
            }
            else if(getCenter() > paddle.bounds.x + paddle.getSectionWidth())
            {
                angle = secondSectionAngle + hitCountAngle;

                if(velocity.x > 0)
                {
                    direction.x *= negativeDirection;
                }
            }
            else
            {
                angle = thirdSectionAngle + hitCountAngle;

                if(velocity.x > 0)
                {
                    direction.x *= negativeDirection;
                }
            }

            ratio = (float) Math.tan(Math.toRadians(angle));

            if(velocity.y < 0)
            {
                bounds.y = paddle.bounds.y + paddle.bounds.height;
            }
            else if(velocity.y > 0)
            {
                bounds.y = paddle.bounds.y - bounds.height;
            }

            direction.y *= negativeDirection;
            iWorld.playPaddleSound();
        }
    }

    private void checkWorld()
    {
        if(position.y + bounds.height < 0)
        {
            iWorld.newBall();
        }
    }
}
