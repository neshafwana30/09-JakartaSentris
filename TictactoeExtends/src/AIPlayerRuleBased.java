public class AIPlayerRuleBased extends AIPlayer {

    public AIPlayerRuleBased(Board board) {
        super(board);
    }

    @Override
    public int[] move() {
        // Rule 1: If I have a winning move, take it.
        int[] winningMove = findWinningMove(mySeed);
        if (winningMove != null) {
            return winningMove;
        }

        // Rule 2: If the opponent has a winning move, block it.
        int[] blockingMove = findWinningMove(oppSeed);
        if (blockingMove != null) {
            return blockingMove;
        }

        // Rule 3: If I can create a fork, do it.
        int[] forkMove = findForkMove(mySeed);
        if (forkMove != null) {
            return forkMove;
        }

        // Rule 4: Block opponent's fork.
        int[] blockForkMove = findForkMove(oppSeed);
        if (blockForkMove != null) {
            return blockForkMove;
        }

        // Rule 5: Play the center if available.
        if (cells[1][1].content == Seed.NO_SEED) {
            return new int[] { 1, 1 };
        }

        // Rule 6: Play a corner if available.
        int[] cornerMove = findCornerMove();
        if (cornerMove != null) {
            return cornerMove;
        }

        // Rule 7: Play any side.
        int[] sideMove = findSideMove();
        if (sideMove != null) {
            return sideMove;
        }

        // No move found (should not happen)
        assert false : "No valid move found!";
        return null;
    }

    private int[] findWinningMove(Seed seed) {
        for (int row = 0; row < Board.ROWS; ++row) {
            for (int col = 0; col < Board.COLS; ++col) {
                if (cells[row][col].content == Seed.NO_SEED) {
                    cells[row][col].content = seed;
                    if (isWinningMove(seed, row, col)) {
                        cells[row][col].content = Seed.NO_SEED;
                        return new int[] { row, col };
                    }
                    cells[row][col].content = Seed.NO_SEED;
                }
            }
        }
        return null;
    }

    private boolean isWinningMove(Seed seed, int row, int col) {
        return (cells[row][0].content == seed && cells[row][1].content == seed && cells[row][2].content == seed)
                || (cells[0][col].content == seed && cells[1][col].content == seed && cells[2][col].content == seed)
                || (row == col && cells[0][0].content == seed && cells[1][1].content == seed
                        && cells[2][2].content == seed)
                || (row + col == 2 && cells[0][2].content == seed && cells[1][1].content == seed
                        && cells[2][0].content == seed);
    }

    private int[] findForkMove(Seed seed) {
        // Implement logic to find a fork move
        // This is more complex and requires checking for multiple winning opportunities
        return null; // Placeholder
    }

    private int[] findCornerMove() {
        int[][] corners = { { 0, 0 }, { 0, 2 }, { 2, 0 }, { 2, 2 } };
        for (int[] corner : corners) {
            if (cells[corner[0]][corner[1]].content == Seed.NO_SEED) {
                return corner;
            }
        }
        return null;
    }

    private int[] findSideMove() {
        int[][] sides = { { 0, 1 }, { 1, 0 }, { 1, 2 }, { 2, 1 } };
        for (int[] side : sides) {
            if (cells[side[0]][side[1]].content == Seed.NO_SEED) {
                return side;
            }
        }
        return null;
    }
}