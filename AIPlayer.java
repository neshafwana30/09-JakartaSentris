import java.util.ArrayList;
import java.util.List;

public abstract class AIPlayer {
    protected Cell[][] cells;
    protected Seed mySeed;
    protected Seed oppSeed;

    private int crossPattern;
    private int noughtPattern;
    private static final int[] WINNING_PATTERNS = { /* Define your winning patterns here */ };

    public AIPlayer(Board board) {
        this.cells = board.getCells();
    }

    public abstract int[] move();

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

    private List<int[]> generateMoves() {
        List<int[]> availableMoves = new ArrayList<>();
        for (int row = 0; row < Board.ROWS; ++row) {
            for (int col = 0; col < Board.COLS; ++col) {
                if (cells[row][col].content == Seed.NO_SEED) {
                    availableMoves.add(new int[] { row, col });
                }
            }
        }
        return availableMoves;
    }

    private int[] minimaxMove(int depth) {
        return new int[] { 0, 0 }; 
    }
}