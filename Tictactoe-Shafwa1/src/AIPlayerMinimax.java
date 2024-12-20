import java.util.ArrayList;
import java.util.List;

public class AIPlayerMinimax extends AIPlayer {
    private static final int DEFAULT_DEPTH = 2;
    private int crossPattern = 0;
    private int noughtPattern = 0;

    public AIPlayerMinimax(Board board) {
        super(board);
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

    @Override
    public int[] move() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}