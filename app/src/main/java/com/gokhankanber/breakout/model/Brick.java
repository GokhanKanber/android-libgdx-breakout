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

/**
 * Creates bricks of the wall.
 */
public class Brick extends Model
{
    private int index;
    private int points;
    private boolean speedBrick;

    public Brick(float x, float y, float width, float height, String color)
    {
        super(x, y, width, height, color);
    }

    public int getIndex()
    {
        return index;
    }

    public void setIndex(int index)
    {
        this.index = index;
    }

    public int getPoints()
    {
        return points;
    }

    public void setPoints(int points)
    {
        this.points = points;
    }

    public void setSpeedBrick()
    {
        this.speedBrick = true;
    }

    public boolean isSpeedBrick()
    {
        return speedBrick;
    }
}
