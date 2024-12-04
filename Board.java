public class Board {
    private int[][] board;
    private final int SIZE = 9;

    public Board() {
        this.board = new int[SIZE][SIZE];
        fillBoard();
    }

    private void fillBoard() {
        Random random = new Random();
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (random.nextInt(10) < 5) { // 50% chance to fill a cell
                    board[i][j] = random.nextInt(9) + 1; // Fill with numbers 1-9
                }
            }
        }
    }

    public int getCell(int row, int col) {
        return board[row][col];
    }

    public void setCell(int row, int col, int value) {
        board[row][col] = value;
    }

    public boolean isFull() {
        for (int[] row : board) {
            for (int cell : row) {
                if (cell == 0) return false;
            }
        }
        return true;
    }

    public void printBoard() {
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                System.out.print(board[r][c] + " ");
            }
            System.out.println();
        }
    }
}
