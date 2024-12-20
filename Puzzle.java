package Sudoku;

import java.util.Random;

public class Puzzle {
    int[][] numbers = new int[SudokuConstants.GRID_SIZE][SudokuConstants.GRID_SIZE];
    boolean[][] isGiven = new boolean[SudokuConstants.GRID_SIZE][SudokuConstants.GRID_SIZE];
    private Random random = new Random();

    public Puzzle() {
        super();
    }

    public void newPuzzle(int cellsToGuess) {
        int[][] baseNumbers = {
                { 5, 3, 4, 6, 7, 8, 9, 1, 2 },
                { 6, 7, 2, 1, 9, 5, 3, 4, 8 },
                { 1, 9, 8, 3, 4, 2, 5, 6, 7 },
                { 8, 5, 9, 7, 6, 1, 4, 2, 3 },
                { 4, 2, 6, 8, 5, 3, 7, 9, 1 },
                { 7, 1, 3, 9, 2, 4, 8, 5, 6 },
                { 9, 6, 1, 5, 3, 7, 2, 8, 4 },
                { 2, 8, 7, 4, 1, 9, 6, 3, 5 },
                { 3, 4, 5, 2, 8, 6, 1, 7, 9 }
        };
        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            System.arraycopy(baseNumbers[row], 0, numbers[row], 0, SudokuConstants.GRID_SIZE);
        }
        for (int block = 0; block < SudokuConstants.GRID_SIZE; block += SudokuConstants.SUBGRID_SIZE) {
            swapRowsInBlock(block);
            swapColsInBlock(block);
        }
        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                isGiven[row][col] = true;
            }
        }
        int cellsRemoved = 0;
        while (cellsRemoved < cellsToGuess) {
            int row = random.nextInt(SudokuConstants.GRID_SIZE);
            int col = random.nextInt(SudokuConstants.GRID_SIZE);
            if (isGiven[row][col]) {
                isGiven[row][col] = false;
                cellsRemoved++;
            }
        }
    }

    private void swapRowsInBlock(int blockStart) {
        int row1 = blockStart + random.nextInt(SudokuConstants.SUBGRID_SIZE);
        int row2 = blockStart + random.nextInt(SudokuConstants.SUBGRID_SIZE);
        if (row1 != row2) {
            int[] temp = numbers[row1];
            numbers[row1] = numbers[row2];
            numbers[row2] = temp;
        }
    }

    private void swapColsInBlock(int blockStart) {
        int col1 = blockStart + random.nextInt(SudokuConstants.SUBGRID_SIZE);
        int col2 = blockStart + random.nextInt(SudokuConstants.SUBGRID_SIZE);
        if (col1 != col2) {
            for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
                int temp = numbers[row][col1];
                numbers[row][col1] = numbers[row][col2];
                numbers[row][col2] = temp;
            }
        }
    }
}