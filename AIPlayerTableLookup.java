public class AIPlayerTableLookup extends AIPlayer {
    private int[][] preferredMoves = {
            { 1, 1 }, { 0, 0 }, { 0, 2 }, { 2, 0 }, { 2, 2 }, { 0, 1 }, { 1, 0 }, { 1, 2 }, { 2, 1 }
    };

    public AIPlayerTableLookup(Board board, Seed mySeed) {
        super(board);
        this.mySeed = mySeed;
        this.oppSeed = (mySeed == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;
    }

    @Override
    public int[] move() {
        int bestScore = Integer.MIN_VALUE;
        int[] bestMove = null;

        for (int[] move : preferredMoves) {
            if (cells[move[0]][move[1]].content == Seed.NO_SEED) {
                // Simulate the move
                cells[move[0]][move[1]].content = mySeed;
                int score = evaluateBoard();
                // Undo the move
                cells[move[0]][move[1]].content = Seed.NO_SEED;

                if (score > bestScore) {
                    bestScore = score;
                    bestMove = move;
                }
            }
        }

        assert bestMove != null : "No valid move found!";
        return bestMove;
    }

    private int evaluateBoard() {
        int score = 0;

        score += evaluateLine(0, 0, 0, 1, 0, 2); // Row 0
        score += evaluateLine(1, 0, 1, 1, 1, 2); // Row 1
        score += evaluateLine(2, 0, 2, 1, 2, 2); // Row 2
        score += evaluateLine(0, 0, 1, 0, 2, 0); // Column 0
        score += evaluateLine(0, 1, 1, 1, 2, 1); // Column 1
        score += evaluateLine(0, 2, 1, 2, 2, 2); // Column 2
        score += evaluateLine(0, 0, 1, 1, 2, 2); // Diagonal
        score += evaluateLine(0, 2, 1, 1, 2, 0); // Opposite diagonal

        return score;
    }

    private int evaluateLine(int row1, int col1, int row2, int col2, int row3, int col3) {
        int score = 0;

        if (cells[row1][col1].content == mySeed) {
            score = 1;
        } else if (cells[row1][col1].content == oppSeed) {
            score = -1;
        }

        if (cells[row2][col2].content == mySeed) {
            if (score == 1) { // mySeed already in cell1
                score = 10;
            } else if (score == -1) { // oppSeed in cell1
                return 0;
            } else { // cell1 is empty
                score = 1;
            }
        } else if (cells[row2][col2].content == oppSeed) {
            if (score == -1) { // oppSeed already in cell1
                score = -10;
            } else if (score == 1) { // mySeed in cell1
                return 0;
            } else { // cell1 is empty
                score = -1;
            }
        }

        if (cells[row3][col3].content == mySeed) {
            if (score > 0) { // mySeed in cell1 and/or cell2
                score *= 10;
            } else if (score < 0) { // oppSeed in cell1 and/or cell2
                return 0;
            } else { // cell1 and cell2 are empty
                score = 1;
            }
        } else if (cells[row3][col3].content == oppSeed) {
            if (score < 0) { // oppSeed in cell1 and/or cell2
                score *= 10;
            } else if (score > 0) { // mySeed in cell1 and/or cell2
                return 0;
            } else { // cell1 and cell2 are empty
                score = -1;
            }
        }
        return score;
    }
}