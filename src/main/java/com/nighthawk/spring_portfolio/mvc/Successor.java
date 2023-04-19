package com.nighthawk.spring_portfolio.mvc;

// Online Java Compiler
// Use this editor to write, compile and run your Java code online

import java.util.*;

public class Successor {
    public static void main(String[] args) {
        boolean[][] grid =  {
            {true, false, false, true, true, true, false, false, false},
            {false, false, false, false, true, false, false, false, false},
            {false, false, false, false, false, false, true, true, true},
            {false, false, true, false, false, false, true, false, false},
            {true, true, true, false, false, false, false, false, false},
            {false, false, false, false, true, false, false, false, false},
            {false, false, false, true, true, true, false, false, true}
        };

        boolean[][] labeled = {
            {false, true, true, false, false, false, true, true, true},
            {true, false, false, true, false, true, false, false, false},
            {true, false, false, false, true, false, false, false, false},
            {true, false, false, true, false, false, false, true, true},
            {false, false, false, true, false, false, true, false, false},
            {true, true, true, false, false, true, false, false, false},
            {true, false, false, false, false, false, true, false, false}
        };

        for (int i = 0; i<grid.length; i++) {
            for (int j = 0; j<grid[i].length; j++) {
                if (toBeLabeled(i, j, grid) != labeled[i][j]) {
                    System.exit(255);
                }
            }
        }

        System.exit(0);
    }

    public static boolean toBeLabeled(int r, int c, boolean[][] blackSquares) {
        return true;
    }
}