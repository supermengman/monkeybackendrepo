package com.nighthawk.spring_portfolio.mvc;

// Online Java Compiler
// Use this editor to write, compile and run your Java code online

import java.util.*;

public class Successor {
    static class Square {
        public boolean isBlack;
        publit int num;

        public Square(boolean a, int b) {
            isBlack = a;
            num = b;
        }
    }

    public static void main(String[] args) {
        boolean[][] grid = {
            {true, false, false, true, true, true, false, false, false},
            {false, false, false, false, true, false, false, false, false},
            {false, false, false, false, false, false, true, true, true},
            {false, false, true, false, false, false, true, false, false},
            {true, true, true, false, false, false, false, false, false},
            {false, false, false, false, true, false, false, false, false},
            {false, false, false, true, true, true, false, false, true}
        };

        int[][] labels = {
            {0,1,2,0,0,0,3,4,5},
            {6,0,0,7,0,8,0,0,0},
            {9,0,0,0,10,0,0,0,0},
            {11,0,0,12,0,0,0,13,14},
            {0,0,0,15,0,0,16,0,0},
            {17,18,19,0,0,20,0,0,0},
            {21,0,0,0,0,0,22,0,0}
        }

        {{ classname }} obj = new {{ classname }}();

        if (obj == null || obj.puzzle == null || grid.length != obj.puzzle.length || grid[0].length != obj.puzzle[0].length) {
            System.exit(255);
        }

        for (int i = 0; i<grid.length; i++) {
            for (int j = 0; j<grid[i].length) {
                if (obj.puzzle[i][j].isBlack != grid[i][j] || obj.puzzle[i][j].num != labels[i][j]) {
                    System.exit(255);
                }
            }
        }

        System.exit({{ exitcode }});
    }

    public boolean toBeLabeled(int r, int c, boolean[][] blackSquares) {
        boolean[][] labels = {
            {false, true, true, false, false, false, true, true, true},
            {true, false, false, true, false, true, false, false, false},
            {true, false, false, false, true, false, false, false, false},
            {true, false, false, true, false, false, false, true, true},
            {false, false, false, true, false, flase, true, false, false},
            {true, true, true, false, false, true, false, false, false},
            {true, false, false, false, false, false, true, false, false}
        };
        return labels[r][c];
    }

    public Square[][] puzzle;

    public Successor (boolean[][] blackSquares) {
        {{ answer }}

        
    }
}