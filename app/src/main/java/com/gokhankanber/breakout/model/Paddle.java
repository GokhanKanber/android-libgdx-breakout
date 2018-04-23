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

import com.gokhankanber.breakout.provider.Config;

public class Paddle extends Model
{
    // Score
    public int points;
    private int sectionNumber;
    private Block leftBlock, rightBlock;

    public Paddle(float x, float y, float width, float height, String color, int sectionNumber)
    {
        super(x, y, width, height, color);

        this.sectionNumber = sectionNumber;
    }

    @Override
    public void setListener(IWorld iWorld)
    {
        super.setListener(iWorld);

        leftBlock = iWorld.getBlocks().get(0);
        rightBlock = iWorld.getBlocks().get(1);
    }

    public float getSectionWidth()
    {
        return getWidth() / sectionNumber;
    }

    public void reset(float x, float y)
    {
        setPosition(x, y);
        points = 0;
    }

    /**
     * Moves paddle by amount of change in x coordinate.
     * @param amount is change in x coordinate.
     */
    public void move(float amount)
    {
        velocity.x = amount;
        bounds.x += velocity.x;
        checkWorld();
        setPosition(bounds.x, bounds.y);
    }

    /**
     * Checks bounds of paddle in x coordinate to stay paddle in world.
     */
    public void checkWorld()
    {
        if(bounds.x + bounds.width > Config.WIDTH - rightBlock.getWidth())
        {
            bounds.x = Config.WIDTH - rightBlock.getWidth() - bounds.width;
        }
        else if(bounds.x < leftBlock.getWidth())
        {
            bounds.x = leftBlock.getWidth();
        }
    }
}
