import java.util.ArrayList;
import java.util.List;

public class AIPlayerMinimax extends AIPlayer {
    private Difficulty difficulty;
    private static final int DEFAULT_DEPTH = 2;
    private int crossPattern = 0;
    private int noughtPattern = 0;
    private static final int[] WINNING_PATTERNS = {
            0x1C0, // 0b111 000 000 (row 2)
            0x038, // 0b000 111 000 (row 1)
            0x007, // 0b000 000 111 (row 0)
            0x124, // 0b100 100 100 (col 2)
            0x092, // 0b010 010 010 (col 1)
            0x049, // 0b001 001 001 (col 0)
            0x111, // 0b100 010 001 (diagonal)
            0x054 // 0b001 010 100 (opposite diagonal)
    };

    public AIPlayerMinimax(Board board, Difficulty difficulty) {
        super(board);
        this.difficulty = difficulty;
    }

    public int[] move() {
        switch (difficulty) {
            case EASY:
                return randomMove();
            case MEDIUM:
                return strategicMediumMove();
            case HARD:
                return minimaxMove(DEFAULT_DEPTH);
            default:
                return minimaxMove(DEFAULT_DEPTH);
        }
    }

    private int[] randomMove() {
        List<int[]> availableMoves = generateMoves();
        if (availableMoves.isEmpty())
            return null;
        return availableMoves.get((int) (Math.random() * availableMoves.size()));
    }

    private int[] strategicMediumMove() {
        List<int[]> availableMoves = generateMoves();
        if (availableMoves.isEmpty())
            return null;
        for (int[] move : availableMoves) {
            cells[move[0]][move[1]].content = oppSeed;
            if (hasWon(oppSeed)) {
                cells[move[0]][move[1]].content = Seed.NO_SEED;
                return move;
            }
            cells[move[0]][move[1]].content = Seed.NO_SEED;
        }

        int[] center = { 1, 1 };
        if (availableMoves.contains(center) && Math.random() > 0.5) {
            availableMoves.remove(center);
        }

        int[][] corners = { { 0, 0 }, { 0, 2 }, { 2, 0 }, { 2, 2 } };
        for (int[] corner : corners) {
            if (availableMoves.contains(corner)) {
                return corner;
            }
        }

        return minimaxMove(1);
    }

    private boolean hasWon(Seed player) {
        int playerPattern = (player == mySeed) ? crossPattern : noughtPattern;
        for (int aWinningPattern : WINNING_PATTERNS) {
            if ((aWinningPattern & playerPattern) == aWinningPattern) {
                return true;
            }
        }
        return false;
    }

    private int[] minimaxMove(int depth) {
        int[] result = minimax(depth, mySeed, Integer.MIN_VALUE, Integer.MAX_VALUE);
        return new int[] { result[1], result[2] };
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
}