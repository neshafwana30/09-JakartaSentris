import java.util.ArrayList;
import java.util.List;

public class AIPlayerMinimax extends AIPlayer {
    private static final int DEFAULT_DEPTH = 2;
    private int crossPattern = 0;
    private int noughtPattern = 0;

    public AIPlayerMinimax(Board board, Seed mySeed) {
        super(board);
        this.mySeed = mySeed;
        this.oppSeed = (mySeed == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;
    }

    @Override
    public int[] move() {
        int[] result = minimax(DEFAULT_DEPTH, mySeed, Integer.MIN_VALUE, Integer.MAX_VALUE);
        return new int[] { result[1], result[2] }; // Return the best row and column
    }

    private int[] minimax(int depth, Seed player, int alpha, int beta) {
        List<int[]> nextMoves = generateMoves();

        int score;
        int bestRow = -1;
        int bestCol = -1;

        if (nextMoves.isEmpty() || depth == 0) {
            score = evaluate();
            return new int[] { score, bestRow, bestCol };
        }

        for (int[] move : nextMoves) {
            cells[move[0]][move[1]].content = player;
            if (player == mySeed) {
                score = minimax(depth - 1, oppSeed, alpha, beta)[0];
                if (score > alpha) {
                    alpha = score;
                    bestRow = move[0];
                    bestCol = move[1];
                }
            } else {
                score = minimax(depth - 1, mySeed, alpha, beta)[0];
                if (score < beta) {
                    beta = score;
                    bestRow = move[0];
                    bestCol = move[1];
                }
            }
            cells[move[0]][move[1]].content = Seed.NO_SEED;
            if (alpha >= beta) {
                break;
            }
        }
        return new int[] { (player == mySeed) ? alpha : beta, bestRow, bestCol };
    }

    private List<int[]> generateMoves() {
        List<int[]> nextMoves = new ArrayList<>();
        for (int row = 0; row < Board.ROWS; ++row) {
            for (int col = 0; col < Board.COLS; ++col) {
                if (cells[row][col].content == Seed.NO_SEED) {
                    nextMoves.add(new int[] { row, col });
                }
            }
        }
        return nextMoves;
    }

    private int evaluate() {
        int score = 0;
        score += evaluateLine(0, 0, 0, 1, 0, 2);
        score += evaluateLine(1, 0, 1, 1, 1, 2);
        score += evaluateLine(2, 0, 2, 1, 2, 2);
        score += evaluateLine(0, 0, 1, 0, 2, 0);
        score += evaluateLine(0, 1, 1, 1, 2, 1);
        score += evaluateLine(0, 2, 1, 2, 2, 2);
        score += evaluateLine(0, 0, 1, 1, 2, 2);
        score += evaluateLine(0, 2, 1, 1, 2, 0);
        return score;
    }

    private int evaluateLine(int row1, int col1, int row2, int col2, int row3, int col3) {
        return 0; 
    }
}