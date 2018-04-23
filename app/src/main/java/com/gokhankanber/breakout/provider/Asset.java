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

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;

public class Asset
{
    private static Asset instance;
    private Character characters;
    private Sound sound;

    private Asset()
    {
    }

    public static Asset get()
    {
        if(instance == null)
        {
            instance = new Asset();
            instance.init();
        }

        return instance;
    }

    public void init()
    {
        characters = Character.get();
        sound = Sound.get();
    }

    public void dispose()
    {
        characters.clear();
        sound.releaseAll();
        instance = null;
    }

    public Texture getTexture(Color color)
    {
        Pixmap pixel = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixel.setColor(color);
        pixel.fill();
        Texture texture = new Texture(pixel);
        pixel.dispose();

        return texture;
    }

    /**
     * Creates logo texture: BREAKOUT.
     * @return texture.
     */
    public Texture getLogoTexture()
    {
        int[] pixels = {-856772946, -286347602, -1433752924, -858862940, -288437596, -1433752924, -353718556, -890589468};

        Pixmap pixmap = new Pixmap(32, 8, Pixmap.Format.RGBA8888);
        drawPixels(pixmap, pixels, 0x8e8e8eff);
        Texture texture = new Texture(pixmap);
        pixmap.dispose();

        return texture;
    }

    /**
     * Draws pixels of logo with specified pixel data.
     * @param pixmap to draw pixels.
     * @param numbers are pixel data.
     * @param color is pixel color.
     */
    private void drawPixels(Pixmap pixmap, int[] numbers, int color)
    {
        int length = numbers.length;

        for(int y = 0; y < length; y++)
        {
            for(int i = 31; i >= 0; i--)
            {
                if(((numbers[y] >> i) & 1) == 1)
                {
                    pixmap.drawPixel((31 - i), y, color);
                }
            }
        }
    }

    /**
     * Creates texture for specified text (menu items).
     * @param text for texture content.
     * @return texture.
     */
    public Texture getTexture(String text, int color)
    {
        if(text == null)
        {
            text = "";
        }

        Pixmap pixmap = new Pixmap(text.length() * 8, 8, Pixmap.Format.RGBA8888);
        drawText(pixmap, text, color);
        Texture texture = new Texture(pixmap);
        pixmap.dispose();

        return texture;
    }

    public Texture getTexture(String text)
    {
        return getTexture(text, 0xffffffff);
    }

    /**
     * Draws pixels of characters of specified text.
     * @param pixmap to draw pixels.
     * @param text data.
     */
    private void drawText(Pixmap pixmap, String text, int color)
    {
        int index = 0;

        for(char character : text.toCharArray())
        {
            byte[] bytes = characters.getBytes(character);

            if(bytes != null)
            {
                int length = bytes.length;

                for(int y = 0; y < length; y++)
                {
                    for(int i = 7; i >= 0; i--)
                    {
                        if(((bytes[y] >> i) & 1) == 1)
                        {
                            pixmap.drawPixel(index * 8 + (7 - i), y, color);
                        }
                    }
                }
            }

            index++;
        }
    }

    public void playButtonSound()
    {
        sound.play(Sound.Track.BUTTON.getIndex());
    }

    public void playBrickSound(int index)
    {
        switch(index)
        {
            case 0:
                sound.play(Sound.Track.TOP_BRICK.getIndex());
                break;
            case 1:
                sound.play(Sound.Track.SECOND_LEVEL_BRICK.getIndex());
                break;
            case 2:
                sound.play(Sound.Track.THIRD_LEVEL_BRICK.getIndex());
                break;
            case 3:
                sound.play(Sound.Track.FOURTH_LEVEL_BRICK.getIndex());
                break;
            case 4:
                sound.play(Sound.Track.FIFTH_LEVEL_BRICK.getIndex());
                break;
            case 5:
                sound.play(Sound.Track.SIXTH_LEVEL_BRICK.getIndex());
                break;
        }
    }

    public void playPaddleSound()
    {
        sound.play(Sound.Track.PADDLE.getIndex());
    }

    public void playTopBorderSound()
    {
        sound.play(Sound.Track.TOP_BORDER.getIndex());
    }

    public void playSideBorderSound()
    {
        sound.play(Sound.Track.SIDE_BORDER.getIndex());
    }

    public void playBallOutSound()
    {
        sound.play(Sound.Track.BALL_OUT.getIndex());
    }
}
