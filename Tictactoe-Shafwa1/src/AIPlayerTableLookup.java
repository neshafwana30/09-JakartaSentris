public class AIPlayerTableLookup extends AIPlayer {
    private int[][] preferredMoves = {
            { 1, 1 }, { 0, 0 }, { 0, 2 }, { 2, 0 }, { 2, 2 }, { 0, 1 }, { 1, 0 }, { 1, 2 }, { 2, 1 }
    };

    public AIPlayerTableLookup(Board board) {
        super(board);
    }

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

        // Evaluate rows, columns, and diagonals
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
}