package com.nighthawk.spring_portfolio.mvc;

// Online Java Compiler
// Use this editor to write, compile and run your Java code online

import java.util.*;

public class Successor {
    static class Position {
        private int r;
        private int c;
        public Position(int r, int c) {
            this.r = r;
            this.c = c;
        }

        public boolean equals(Object o) {

            if (o == this) {
                return true;
            }
            if (!(o instanceof Position)) {
                return false;
            }
            Position p = (Position) o;
            return r == p.r && c == p.c;
        }
    }

    public static void main(String[] args) {
        
        int[][] twodArray = {
            {15, 5, 9, 10},
            {12, 16, 11, 6},
            {14, 8, 13, 7}
        };

        Position[][] answer = getSuccessorArrayAnswer(twodArray);
        Position[][] studentAnswer = getSuccessorArray(twodArray);

        if (answer.length != studentAnswer.length) {
            System.out.println("Your answer is incorrect.");
            System.exit(255);
        }

        for (int row=0; row<answer.length; row++) {
            for (int col=0; col<answer[row].length; col++) {

                if (answer[row][col] == null && studentAnswer[row][col] == null) {
                    continue;
                }
                if (!answer[row][col].equals(studentAnswer[row][col])) {
                    System.out.println("Your answer is incorrect.");
                    System.exit(255);
                }
            }
        }
        
        System.exit(0);
    }

    public static Position findPosition (int num, int[][] intArr) {
        for (int row=0; row<intArr.length; row++) {
            for (int col=0; col<intArr[row].length; col++) {
                if (intArr[row][col] == num) {
                    return new Position(row, col);
                }
            }
        }
        return null;
    }

    public static Position[][] getSuccessorArray(int[][] intArr) {
        Position[][] newArr = new Position[intArr.length][intArr[0].length]; for (int row=0; row < intArr.length; row++) { for (int col=0; col < intArr[0].length; col++) { newArr[row][col] = findPosition(intArr[row][col]+1, intArr); } } return newArr;
    }

    public static Position[][] getSuccessorArrayAnswer(int[][] intArr) {
        Position[][] answerArray = new Position[intArr.length][intArr[0].length];
        for (int row=0; row<intArr.length; row++) {
            for (int col=0; col<intArr[row].length; col++) {
                answerArray[row][col] = findPosition(intArr[row][col] + 1, intArr);
            }
        }
        return answerArray;
    }
}