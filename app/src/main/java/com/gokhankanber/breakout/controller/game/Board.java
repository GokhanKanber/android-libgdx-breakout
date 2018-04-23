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

public class Board
{
    private boolean[][] numberBoard;
    private int x;

    /**
     * Builds score, balls, and player number in the board.
     * Contains number data as a boolean array.
     */
    public boolean[][] buildNumber(int number, int digits)
    {
        String value = String.valueOf(number);
        int length = value.length();
        int space = digits - 1;
        int width = digits * 3 + space;
        int height = 8;

        for(int i = 0; i < digits - length; i++)
        {
            value = "0" + value;
        }

        numberBoard = new boolean[width][height];
        x = 0;

        for(char c : value.toCharArray())
        {
            switch(c)
            {
                case '0':
                    buildZero();
                    break;
                case '1':
                    buildOne();
                    break;
                case '2':
                    buildTwo();
                    break;
                case '3':
                    buildThree();
                    break;
                case '4':
                    buildFour();
                    break;
                case '5':
                    buildFive();
                    break;
                case '6':
                    buildSix();
                    break;
                case '7':
                    buildSeven();
                    break;
                case '8':
                    buildEight();
                    break;
                case '9':
                    buildNine();
                    break;
            }

            x += 3;

            if(space > 0)
            {
                x++;
                space--;
            }
        }

        return numberBoard;
    }

    public boolean[][] buildNumber(int number)
    {
        return buildNumber(number, 1);
    }

    public int getSize()
    {
        int size = 0;

        for(boolean[] column : numberBoard)
        {
            for(boolean row: column)
            {
                if(row)
                {
                    size++;
                }
            }
        }

        return size;
    }

    private void buildZero()
    {
        numberBoard[x][0] = true;
        numberBoard[x][1] = true;
        numberBoard[x][2] = true;
        numberBoard[x][3] = true;
        numberBoard[x][4] = true;
        numberBoard[x][5] = true;
        numberBoard[x][6] = true;
        numberBoard[x][7] = true;
        numberBoard[x+1][0] = true;
        numberBoard[x+1][1] = true;
        numberBoard[x+1][6] = true;
        numberBoard[x+1][7] = true;
        numberBoard[x+2][0] = true;
        numberBoard[x+2][1] = true;
        numberBoard[x+2][2] = true;
        numberBoard[x+2][3] = true;
        numberBoard[x+2][4] = true;
        numberBoard[x+2][5] = true;
        numberBoard[x+2][6] = true;
        numberBoard[x+2][7] = true;
    }

    private void buildOne()
    {
        numberBoard[x+2][0] = true;
        numberBoard[x+2][1] = true;
        numberBoard[x+2][2] = true;
        numberBoard[x+2][3] = true;
        numberBoard[x+2][4] = true;
        numberBoard[x+2][5] = true;
        numberBoard[x+2][6] = true;
        numberBoard[x+2][7] = true;
    }

    private void buildTwo()
    {
        numberBoard[x][0] = true;
        numberBoard[x][1] = true;
        numberBoard[x][3] = true;
        numberBoard[x][4] = true;
        numberBoard[x][5] = true;
        numberBoard[x][6] = true;
        numberBoard[x][7] = true;
        numberBoard[x+1][0] = true;
        numberBoard[x+1][1] = true;
        numberBoard[x+1][3] = true;
        numberBoard[x+1][4] = true;
        numberBoard[x+1][6] = true;
        numberBoard[x+1][7] = true;
        numberBoard[x+2][0] = true;
        numberBoard[x+2][1] = true;
        numberBoard[x+2][2] = true;
        numberBoard[x+2][3] = true;
        numberBoard[x+2][4] = true;
        numberBoard[x+2][6] = true;
        numberBoard[x+2][7] = true;
    }

    private void buildThree()
    {
        numberBoard[x][0] = true;
        numberBoard[x][1] = true;
        numberBoard[x][6] = true;
        numberBoard[x][7] = true;
        numberBoard[x+1][0] = true;
        numberBoard[x+1][1] = true;
        numberBoard[x+1][3] = true;
        numberBoard[x+1][4] = true;
        numberBoard[x+1][6] = true;
        numberBoard[x+1][7] = true;
        numberBoard[x+2][0] = true;
        numberBoard[x+2][1] = true;
        numberBoard[x+2][2] = true;
        numberBoard[x+2][3] = true;
        numberBoard[x+2][4] = true;
        numberBoard[x+2][5] = true;
        numberBoard[x+2][6] = true;
        numberBoard[x+2][7] = true;
    }

    private void buildFour()
    {
        numberBoard[x][0] = true;
        numberBoard[x][1] = true;
        numberBoard[x][2] = true;
        numberBoard[x][3] = true;
        numberBoard[x][4] = true;
        numberBoard[x+1][3] = true;
        numberBoard[x+1][4] = true;
        numberBoard[x+2][0] = true;
        numberBoard[x+2][1] = true;
        numberBoard[x+2][2] = true;
        numberBoard[x+2][3] = true;
        numberBoard[x+2][4] = true;
        numberBoard[x+2][5] = true;
        numberBoard[x+2][6] = true;
        numberBoard[x+2][7] = true;
    }

    private void buildFive()
    {
        numberBoard[x][0] = true;
        numberBoard[x][1] = true;
        numberBoard[x][2] = true;
        numberBoard[x][3] = true;
        numberBoard[x][4] = true;
        numberBoard[x][6] = true;
        numberBoard[x][7] = true;
        numberBoard[x+1][0] = true;
        numberBoard[x+1][1] = true;
        numberBoard[x+1][3] = true;
        numberBoard[x+1][4] = true;
        numberBoard[x+1][6] = true;
        numberBoard[x+1][7] = true;
        numberBoard[x+2][0] = true;
        numberBoard[x+2][1] = true;
        numberBoard[x+2][3] = true;
        numberBoard[x+2][4] = true;
        numberBoard[x+2][5] = true;
        numberBoard[x+2][6] = true;
        numberBoard[x+2][7] = true;
    }

    private void buildSix()
    {
        numberBoard[x][0] = true;
        numberBoard[x][1] = true;
        numberBoard[x][2] = true;
        numberBoard[x][3] = true;
        numberBoard[x][4] = true;
        numberBoard[x][5] = true;
        numberBoard[x][6] = true;
        numberBoard[x][7] = true;
        numberBoard[x+1][0] = true;
        numberBoard[x+1][1] = true;
        numberBoard[x+1][3] = true;
        numberBoard[x+1][4] = true;
        numberBoard[x+1][6] = true;
        numberBoard[x+1][7] = true;
        numberBoard[x+2][0] = true;
        numberBoard[x+2][1] = true;
        numberBoard[x+2][3] = true;
        numberBoard[x+2][4] = true;
        numberBoard[x+2][5] = true;
        numberBoard[x+2][6] = true;
        numberBoard[x+2][7] = true;
    }

    private void buildSeven()
    {
        numberBoard[x][0] = true;
        numberBoard[x][1] = true;
        numberBoard[x+1][0] = true;
        numberBoard[x+1][1] = true;
        numberBoard[x+2][0] = true;
        numberBoard[x+2][1] = true;
        numberBoard[x+2][2] = true;
        numberBoard[x+2][3] = true;
        numberBoard[x+2][4] = true;
        numberBoard[x+2][5] = true;
        numberBoard[x+2][6] = true;
        numberBoard[x+2][7] = true;
    }

    private void buildEight()
    {
        numberBoard[x][0] = true;
        numberBoard[x][1] = true;
        numberBoard[x][2] = true;
        numberBoard[x][3] = true;
        numberBoard[x][4] = true;
        numberBoard[x][5] = true;
        numberBoard[x][6] = true;
        numberBoard[x][7] = true;
        numberBoard[x+1][0] = true;
        numberBoard[x+1][1] = true;
        numberBoard[x+1][3] = true;
        numberBoard[x+1][4] = true;
        numberBoard[x+1][6] = true;
        numberBoard[x+1][7] = true;
        numberBoard[x+2][0] = true;
        numberBoard[x+2][1] = true;
        numberBoard[x+2][2] = true;
        numberBoard[x+2][3] = true;
        numberBoard[x+2][4] = true;
        numberBoard[x+2][5] = true;
        numberBoard[x+2][6] = true;
        numberBoard[x+2][7] = true;
    }

    private void buildNine()
    {
        numberBoard[x][0] = true;
        numberBoard[x][1] = true;
        numberBoard[x][2] = true;
        numberBoard[x][3] = true;
        numberBoard[x][4] = true;
        numberBoard[x+1][0] = true;
        numberBoard[x+1][1] = true;
        numberBoard[x+1][3] = true;
        numberBoard[x+1][4] = true;
        numberBoard[x+2][0] = true;
        numberBoard[x+2][1] = true;
        numberBoard[x+2][2] = true;
        numberBoard[x+2][3] = true;
        numberBoard[x+2][4] = true;
        numberBoard[x+2][5] = true;
        numberBoard[x+2][6] = true;
        numberBoard[x+2][7] = true;
    }
}
