import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * Tic-Tac-Toe: Two-player Graphics version with Simple-OO in one class
 */
public class TTTGraphics extends JFrame {
    private static final long serialVersionUID = 1L; // to prevent serializable warning

    // Define named constants for the game board
    public static final int ROWS = 8; // ROWS x COLS cells
    public static final int COLS = 8;

    // Define named constants for the drawing graphics
    public static final int CELL_SIZE = 120; // cell width/height (square)
    public static final int BOARD_WIDTH = CELL_SIZE * COLS; // the drawing canvas
    public static final int BOARD_HEIGHT = CELL_SIZE * ROWS;
    public static final int GRID_WIDTH = 10; // Grid-line's width
    public static final int GRID_WIDTH_HALF = GRID_WIDTH / 2;
    // Symbols (cross/nought) are displayed inside a cell, with padding from border
    public static final int CELL_PADDING = CELL_SIZE / 5;
    public static final int SYMBOL_SIZE = CELL_SIZE - CELL_PADDING * 2; // width/height
    public static final int SYMBOL_STROKE_WIDTH = 8; // pen's stroke width
    public static final Color COLOR_BG = Color.BLACK; // background
    public static final Color COLOR_BG_STATUS = new Color(216, 216, 216);
    public static final Color COLOR_GRID = Color.LIGHT_GRAY; // grid lines
    public static final Color COLOR_CROSS = new Color(211, 45, 65); // Red #D32D41
    public static final Color COLOR_NOUGHT = new Color(76, 181, 245); // Blue #4CB5F5
    public static final Font FONT_STATUS = new Font("OCR A Extended", Font.PLAIN, 14);

    // This enum (inner class) contains the various states of the game
    public enum State {
        PLAYING, DRAW, CROSS_WON, NOUGHT_WON
    }

    private State currentState; // the current game state

    // This enum (inner class) is used for:
    // 1. Player: CROSS, NOUGHT
    // 2. Cell's content: CROSS, NOUGHT and NO_SEED
    public enum Seed {
        BLACK, WHITE, NO_SEED
    }

    private Seed currentPlayer; // the current player
    private Seed[][] board; // Game board of ROWS-by-COLS cells

    // UI Components
    private GamePanel gamePanel; // Drawing canvas (JPanel) for the game board
    private JLabel statusBar; // Status Bar
    private JLabel blackCountLabel;
    private JLabel whiteCountLabel;

    /** Constructor to setup the game and the GUI components */
    public TTTGraphics() {
        // Initialize the game objects
        initGame();

        gamePanel = new GamePanel();
        gamePanel.setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));

        gamePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int mouseX = e.getX();
                int mouseY = e.getY();
                int row = mouseY / CELL_SIZE;
                int col = mouseX / CELL_SIZE;

                if (currentState == State.PLAYING) {
                    if (row >= 0 && row < ROWS && col >= 0 && col < COLS && board[row][col] == Seed.NO_SEED) {
                        if (isLegalMove(currentPlayer, row, col)) {
                            updateGame(currentPlayer, row, col);
                            currentPlayer = (currentPlayer == Seed.BLACK) ? Seed.WHITE : Seed.BLACK;
                        }
                    }
                } else {
                    newGame();
                }
                repaint();
            }
        });

        statusBar = new JLabel("       ");
        statusBar.setFont(FONT_STATUS);
        statusBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 12));
        statusBar.setOpaque(true);
        statusBar.setBackground(COLOR_BG_STATUS);

        blackCountLabel = new JLabel("Orange: 2");
        blackCountLabel.setFont(FONT_STATUS);
        blackCountLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 12));

        whiteCountLabel = new JLabel("Pink: 2");
        whiteCountLabel.setFont(FONT_STATUS);
        whiteCountLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 12));

        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());
        cp.add(gamePanel, BorderLayout.CENTER);

        // Create a panel for the status and count labels
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.add(statusBar, BorderLayout.CENTER);
        statusPanel.add(blackCountLabel, BorderLayout.WEST);
        statusPanel.add(whiteCountLabel, BorderLayout.EAST);
        cp.add(statusPanel, BorderLayout.PAGE_END);

        JPanel countPanel = new JPanel();
        countPanel.setLayout(new BoxLayout(countPanel, BoxLayout.Y_AXIS));
        countPanel.add(blackCountLabel);
        countPanel.add(whiteCountLabel);

        cp.add(countPanel, BorderLayout.EAST); // Add the count panel to the right of the board
        cp.add(statusBar, BorderLayout.PAGE_END); // Keep the status bar at the bottom

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setTitle("Othello");
        setVisible(true);

        newGame();
    }

    /** Initialize the Game (run once) */
    public void initGame() {
        board = new Seed[ROWS][COLS]; // allocate array
    }

    /** Reset the game-board contents and the status, ready for new game */
    public void newGame() {
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                board[row][col] = Seed.NO_SEED; // all cells empty
            }
        }
        // Place the initial four discs
        board[3][3] = Seed.WHITE;
        board[3][4] = Seed.BLACK;
        board[4][3] = Seed.BLACK;
        board[4][4] = Seed.WHITE;

        currentPlayer = Seed.BLACK; // black plays first
        currentState = State.PLAYING; // ready to play
        updateCounts();
    }

    /**
     * The given player makes a move on (selectedRow, selectedCol).
     * Update cells[selectedRow][selectedCol]. Compute and return the
     * new game state (PLAYING, DRAW, CROSS_WON, NOUGHT_WON).
     */

    public void updateGame(Seed mySeed, int rowSelected, int colSelected) {
        if (!isLegalMove(mySeed, rowSelected, colSelected)) {
            return; // If the move is not legal, do nothing
        }

        Seed opponentSeed = (mySeed == Seed.BLACK) ? Seed.WHITE : Seed.BLACK;
        board[rowSelected][colSelected] = mySeed; // Place the seed

        // Flip opponent's seeds in all directions
        flipDirection(mySeed, opponentSeed, rowSelected, colSelected, 1, 0); // Right
        flipDirection(mySeed, opponentSeed, rowSelected, colSelected, -1, 0); // Left
        flipDirection(mySeed, opponentSeed, rowSelected, colSelected, 0, 1); // Down
        flipDirection(mySeed, opponentSeed, rowSelected, colSelected, 0, -1); // Up
        flipDirection(mySeed, opponentSeed, rowSelected, colSelected, 1, 1); // Diagonal down-right
        flipDirection(mySeed, opponentSeed, rowSelected, colSelected, -1, -1); // Diagonal up-left
        flipDirection(mySeed, opponentSeed, rowSelected, colSelected, 1, -1); // Diagonal down-left
        flipDirection(mySeed, opponentSeed, rowSelected, colSelected, -1, 1); // Diagonal up-right

        updateCounts(); // Update counts after each move
        // Check for game over and declare winner
        int blackCount = 0;
        int whiteCount = 0;
        boolean hasEmpty = false;
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                if (board[row][col] == Seed.BLACK) {
                    blackCount++;
                } else if (board[row][col] == Seed.WHITE) {
                    whiteCount++;
                } else if (board[row][col] == Seed.NO_SEED) {
                    hasEmpty = true;
                }
            }
        }

        if (!hasEmpty) {
            currentState = (blackCount > whiteCount) ? State.CROSS_WON : State.NOUGHT_WON;
        } else {
            currentState = State.PLAYING;
        }
    }

    private void flipDirection(Seed mySeed, Seed opponentSeed, int row, int col, int rowDir, int colDir) {
        int r = row + rowDir;
        int c = col + colDir;
        boolean hasOpponentSeed = false;

        while (r >= 0 && r < ROWS && c >= 0 && c < COLS && board[r][c] == opponentSeed) {
            r += rowDir;
            c += colDir;
            hasOpponentSeed = true;
        }

        if (hasOpponentSeed && r >= 0 && r < ROWS && c >= 0 && c < COLS && board[r][c] == mySeed) {
            r -= rowDir;
            c -= colDir;
            while (r != row || c != col) {
                board[r][c] = mySeed;
                r -= rowDir;
                c -= colDir;
            }
        }
    }

    /**
     * Inner class DrawCanvas (extends JPanel) used for custom graphics drawing.
     */
    class GamePanel extends JPanel {
        private static final long serialVersionUID = 1L; // to prevent serializable warning

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            setBackground(COLOR_BG); // set its background color

            // Draw the grid lines
            g.setColor(COLOR_GRID);
            for (int row = 1; row < ROWS; ++row) {
                g.fillRoundRect(0, CELL_SIZE * row - GRID_WIDTH_HALF,
                        BOARD_WIDTH - 1, GRID_WIDTH, GRID_WIDTH, GRID_WIDTH);
            }
            for (int col = 1; col < COLS; ++col) {
                g.fillRoundRect(CELL_SIZE * col - GRID_WIDTH_HALF, 0,
                        GRID_WIDTH, BOARD_HEIGHT - 1, GRID_WIDTH, GRID_WIDTH);
            }

            // Draw the Seeds of all the cells if they are not empty
            // Use Graphics2D which allows us to set the pen's stroke
            Graphics2D g2d = (Graphics2D) g;
            g2d.setStroke(new BasicStroke(SYMBOL_STROKE_WIDTH,
                    BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            for (int row = 0; row < ROWS; ++row) {
                for (int col = 0; col < COLS; ++col) {
                    int x1 = col * CELL_SIZE + CELL_PADDING;
                    int y1 = row * CELL_SIZE + CELL_PADDING;
                    if (board[row][col] == Seed.BLACK) { // draw a black disc
                        g2d.setColor(new Color(255, 179, 71));
                        g2d.fillOval(x1, y1, SYMBOL_SIZE, SYMBOL_SIZE);
                    } else if (board[row][col] == Seed.WHITE) { // draw a white disc
                        g2d.setColor(new Color(255, 182, 193));
                        g2d.fillOval(x1, y1, SYMBOL_SIZE, SYMBOL_SIZE);
                    }
                }
            }

            // Print status message
            if (currentState == State.PLAYING) {
                statusBar.setForeground(Color.BLACK);
                statusBar.setText((currentPlayer == Seed.BLACK) ? "Orange's Turn" : "White's Turn");
            } else if (currentState == State.DRAW) {
                statusBar.setForeground(Color.RED);
                statusBar.setText("It's a Draw! Click to play again");
            } else if (currentState == State.CROSS_WON) {
                statusBar.setForeground(Color.RED);
                statusBar.setText("'Orange' Won! Click to play again");
            } else if (currentState == State.NOUGHT_WON) {
                statusBar.setForeground(Color.RED);
                statusBar.setText("'White' Won! Click to play again");
            }
        }
    }

    private void updateCounts() {
        int blackCount = 0;
        int whiteCount = 0;
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                if (board[row][col] == Seed.BLACK) {
                    blackCount++;
                } else if (board[row][col] == Seed.WHITE) {
                    whiteCount++;
                }
            }
        }
        blackCountLabel.setText("Black: " + blackCount);
        whiteCountLabel.setText("White: " + whiteCount);
    }

    private boolean isLegalMove(Seed mySeed, int rowSelected, int colSelected) {
        if (board[rowSelected][colSelected] != Seed.NO_SEED) {
            return false;
        }
        Seed opponentSeed = (mySeed == Seed.BLACK) ? Seed.WHITE : Seed.BLACK;
        return checkDirection(mySeed, opponentSeed, rowSelected, colSelected, 1, 0) || // Right
                checkDirection(mySeed, opponentSeed, rowSelected, colSelected, -1, 0) || // Left
                checkDirection(mySeed, opponentSeed, rowSelected, colSelected, 0, 1) || // Down
                checkDirection(mySeed, opponentSeed, rowSelected, colSelected, 0, -1) || // Up
                checkDirection(mySeed, opponentSeed, rowSelected, colSelected, 1, 1) || // Diagonal down-right
                checkDirection(mySeed, opponentSeed, rowSelected, colSelected, -1, -1) || // Diagonal up-left
                checkDirection(mySeed, opponentSeed, rowSelected, colSelected, 1, -1) || // Diagonal down-left
                checkDirection(mySeed, opponentSeed, rowSelected, colSelected, -1, 1); // Diagonal up-right
    }

    private boolean checkDirection(Seed mySeed, Seed opponentSeed, int row, int col, int rowDir, int colDir) {
        int r = row + rowDir;
        int c = col + colDir;
        boolean hasOpponentSeed = false;

        while (r >= 0 && r < ROWS && c >= 0 && c < COLS && board[r][c] == opponentSeed) {
            r += rowDir;
            c += colDir;
            hasOpponentSeed = true;
        }

        return hasOpponentSeed && r >= 0 && r < ROWS && c >= 0 && c < COLS && board[r][c] == mySeed;
    }

    /** The entry main() method */
    public static void main(String[] args) {
        // Run GUI codes in the Event-Dispatching thread for thread safety
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new TTTGraphics(); // Let the constructor do the job
            }
        });
    }
}