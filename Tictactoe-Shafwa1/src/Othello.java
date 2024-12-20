import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;


public class Othello extends JFrame {
    private static final long serialVersionUID = 1L;
    public static final int ROWS = 8;
    public static final int COLS = 8;
    public static final int CELL_SIZE = 80;
    public static final int SYMBOL_SIZE = CELL_SIZE - 20;
    public static final int CELL_PADDING = 10;
    public static final int GRID_WIDTH = 8;
    public static final int GRID_WIDTH_HALF = GRID_WIDTH / 2;
    public static final int BOARD_WIDTH = CELL_SIZE * COLS;
    public static final int BOARD_HEIGHT = CELL_SIZE * ROWS;
    private static final Font FONT_STATUS = new Font("Arial", Font.BOLD, 14);
    private static final Color COLOR_BG_STATUS = new Color(154, 123, 109);


    private BufferedImage blackIcon;
    private BufferedImage whiteIcon;
    private Seed currentPlayer;
    private Seed[][] board;
    private State currentState;
    private ArrayList<int[]> legalMoves;


    public enum Seed {
        BLACK, WHITE, NO_SEED
    }


    public enum State {
        PLAYING, DRAW, CROSS_WON, NOUGHT_WON
    }


    private RoundedButton restartButton;
    private RoundedButton homeButton;
    private JLabel blackCountLabel;
    private JLabel whiteCountLabel;
    private JPanel countPanel;
    private JPanel statusBar;
    private JPanel buttonPanel;
    private JLabel statusTextLabel;


    public Othello() {
        initGame();
        legalMoves = new ArrayList<>();


        try {
            blackIcon = ImageIO.read(new File("src\\Image\\Othello\\Black.png"));
            whiteIcon = ImageIO.read(new File("src\\Image\\Othello\\White.png"));
        } catch (IOException e) {
            e.printStackTrace();
            blackIcon = new BufferedImage(SYMBOL_SIZE, SYMBOL_SIZE, BufferedImage.TYPE_INT_ARGB);
            whiteIcon = new BufferedImage(SYMBOL_SIZE, SYMBOL_SIZE, BufferedImage.TYPE_INT_ARGB);
        }


        GamePanel gamePanel = new GamePanel();
        gamePanel.setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));


        statusBar = new JPanel(new BorderLayout());
        statusBar.setOpaque(true);
        statusBar.setBackground(COLOR_BG_STATUS);
        statusTextLabel = new JLabel();
        statusTextLabel.setFont(new Font("Arial", Font.BOLD, 20));
        statusTextLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 12));
        statusTextLabel.setHorizontalAlignment(JLabel.CENTER);
        countPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        countPanel.setOpaque(false);
        blackCountLabel = new JLabel("Brown: 0");
        blackCountLabel.setFont(FONT_STATUS);
        whiteCountLabel = new JLabel("White: 0");
        whiteCountLabel.setFont(FONT_STATUS);
        countPanel.add(blackCountLabel);
        countPanel.add(whiteCountLabel);
        statusBar.add(statusTextLabel, BorderLayout.CENTER);
        statusBar.add(countPanel, BorderLayout.LINE_END);
        restartButton = new RoundedButton("Restart");
        homeButton = new RoundedButton("Home");
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(restartButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(homeButton);




        restartButton.addActionListener(e -> newGame());
        homeButton.addActionListener(e -> {
            System.out.println("Home button clicked");
            new CustomWelcomePage();
            dispose();
        });


        buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));


        buttonPanel.add(restartButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(homeButton);




        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());
        cp.add(statusBar, BorderLayout.PAGE_START);
        cp.add(gamePanel, BorderLayout.CENTER);
        cp.add(buttonPanel, BorderLayout.PAGE_END);


        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setTitle("Othello");
        setResizable(false);
        setVisible(true);


        newGame();
    }


    /** Initialize the Game (run once) */
    public void initGame() {
        board = new Seed[ROWS][COLS];
    }


    private void updateStatusBar() {
        String statusText;
        if (currentState == State.PLAYING) {
            statusText = (currentPlayer == Seed.BLACK) ? "Brown's Turn" : "White's Turn";
        } else if (currentState == State.DRAW) {
            statusText = "It's a Draw! Click to play again";
        } else if (currentState == State.CROSS_WON) {
            statusText = "'Brown' Won! Click to play again";
        } else {
            statusText = "'White' Won! Click to play again";
        }
        statusTextLabel.setText(statusText);
    }


    private int getBlackCount() {
        int blackCount = 0;
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                if (board[row][col] == Seed.BLACK) {
                    blackCount++;
                }
            }
        }
        return blackCount;
    }


    private int getWhiteCount() {
        int whiteCount = 0;
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                if (board[row][col] == Seed.WHITE) {
                    whiteCount++;
                }
            }
        }
        return whiteCount;
    }


    /** Reset the game-board contents and the status, ready for new game */
    public void newGame() {
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                board[row][col] = Seed.NO_SEED;
            }
        }
        board[3][3] = Seed.WHITE;
        board[3][4] = Seed.BLACK;
        board[4][3] = Seed.BLACK;
        board[4][4] = Seed.WHITE;
        currentPlayer = Seed.BLACK;
        currentState = State.PLAYING;


        updateCounts();
        updateLegalMoves();
        updateStatusBar();
        repaint();
    }


    private void updateLegalMoves() {
        legalMoves.clear();
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                if (isLegalMove(currentPlayer, row, col)) {
                    legalMoves.add(new int[] { row, col });
                }
            }
        }
    }


    public void updateGame(Seed mySeed, int rowSelected, int colSelected) {
        if (!isLegalMove(mySeed, rowSelected, colSelected)) {
            return;
        }


        board[rowSelected][colSelected] = mySeed;
        Seed opponentSeed = (mySeed == Seed.BLACK) ? Seed.WHITE : Seed.BLACK;
        flipDirection(mySeed, opponentSeed, rowSelected, colSelected, 1, 0);
        flipDirection(mySeed, opponentSeed, rowSelected, colSelected, -1, 0);
        flipDirection(mySeed, opponentSeed, rowSelected, colSelected, 0, 1);
        flipDirection(mySeed, opponentSeed, rowSelected, colSelected, 0, -1);
        flipDirection(mySeed, opponentSeed, rowSelected, colSelected, 1, 1);
        flipDirection(mySeed, opponentSeed, rowSelected, colSelected, -1, -1);
        flipDirection(mySeed, opponentSeed, rowSelected, colSelected, 1, -1);
        flipDirection(mySeed, opponentSeed, rowSelected, colSelected, -1, 1);


        updateCounts();
        updateStatusBar();


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
            currentPlayer = (currentPlayer == Seed.BLACK) ? Seed.WHITE : Seed.BLACK;
            updateLegalMoves();


            if (legalMoves.isEmpty()) {
                currentPlayer = (currentPlayer == Seed.BLACK) ? Seed.WHITE : Seed.BLACK;
                updateLegalMoves();
            }
        }
        updateStatusBar();
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


    private void updateCounts() {
        int blackCount = getBlackCount();
        int whiteCount = getWhiteCount();
        blackCountLabel.setText("Brown: " + blackCount);
        whiteCountLabel.setText("White: " + whiteCount);
    }


    private void drawLegalMoves(Graphics g) {
        if (currentPlayer == Seed.WHITE) {
            g.setColor(new Color(192, 192, 192, 128));
        } else {
            g.setColor(new Color(154, 123, 109, 128));
        }
        for (int[] move : legalMoves) {
            int x1 = move[1] * CELL_SIZE + CELL_PADDING;
            int y1 = move[0] * CELL_SIZE + CELL_PADDING;
            g.fillOval(x1, y1, SYMBOL_SIZE, SYMBOL_SIZE);
        }
    }


    private boolean isLegalMove(Seed mySeed, int rowSelected, int colSelected) {
        if (board[rowSelected][colSelected] != Seed.NO_SEED) {
            return false;
        }
        Seed opponentSeed = (mySeed == Seed.BLACK) ? Seed.WHITE : Seed.BLACK;
        return checkDirection(mySeed, opponentSeed, rowSelected, colSelected, 1, 0) ||
                checkDirection(mySeed, opponentSeed, rowSelected, colSelected, -1, 0) ||
                checkDirection(mySeed, opponentSeed, rowSelected, colSelected, 0, 1) ||
                checkDirection(mySeed, opponentSeed, rowSelected, colSelected, 0, -1) ||
                checkDirection(mySeed, opponentSeed, rowSelected, colSelected, 1, 1) ||
                checkDirection(mySeed, opponentSeed, rowSelected, colSelected, -1, -1) ||
                checkDirection(mySeed, opponentSeed, rowSelected, colSelected, 1, -1) ||
                checkDirection(mySeed, opponentSeed, rowSelected, colSelected, -1, 1);
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
    private class GamePanel extends JPanel {
        private static final long serialVersionUID = 1L;


        public GamePanel() {
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    int mouseX = e.getX();
                    int mouseY = e.getY();
                    int rowSelected = mouseY / CELL_SIZE;
                    int colSelected = mouseX / CELL_SIZE;


                    if (currentState == State.PLAYING) {
                        updateGame(currentPlayer, rowSelected, colSelected);
                        repaint();
                    }
                }
            });
        }


        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            setBackground(new Color(255, 248, 233));
            g.setColor(new Color(154, 123, 109));


            for (int row = 1; row < ROWS; ++row) {
                g.fillRoundRect(0, CELL_SIZE * row - GRID_WIDTH_HALF,
                        BOARD_WIDTH - 1, GRID_WIDTH, GRID_WIDTH, GRID_WIDTH);
            }
            for (int col = 1; col < COLS; ++col) {
                g.fillRoundRect(CELL_SIZE * col - GRID_WIDTH_HALF, 0,
                        GRID_WIDTH, BOARD_HEIGHT - 1, GRID_WIDTH, GRID_WIDTH);
            }


            for (int row = 0; row < ROWS; ++row) {
                for (int col = 0; col < COLS; ++col) {
                    int x1 = col * CELL_SIZE + CELL_PADDING;
                    int y1 = row * CELL_SIZE + CELL_PADDING;
                    if (board[row][col] == Seed.BLACK) {
                        g.drawImage(blackIcon, x1, y1, SYMBOL_SIZE, SYMBOL_SIZE, this);
                    } else if (board[row][col] == Seed.WHITE) {
                        g.drawImage(whiteIcon, x1, y1, SYMBOL_SIZE, SYMBOL_SIZE, this);
                    }
                }
            }
            drawLegalMoves(g);
            updateStatusBarColors();
        }
    }


    private void updateStatusBarColors() {
        Color backgroundColor;
        Color foregroundColor;


        if (currentState == State.PLAYING) {
            if (currentPlayer == Seed.BLACK) {
                backgroundColor = new Color(154, 123, 109);
                foregroundColor = new Color(255, 248, 233);
            } else {
                backgroundColor = new Color(255, 248, 233);
                foregroundColor = new Color(154, 123, 109);
            }
        } else {
            backgroundColor = Color.RED;
            foregroundColor = Color.WHITE;
        }


        statusBar.setBackground(backgroundColor);
        statusTextLabel.setForeground(foregroundColor);
        countPanel.setBackground(backgroundColor);
        blackCountLabel.setForeground(foregroundColor);
        whiteCountLabel.setForeground(foregroundColor);
        buttonPanel.setBackground(backgroundColor);
        updateButtonPanelColors();
    }


    private void updateButtonPanelColors() {
        Color backgroundColor;
        Color foregroundColor;


        if (currentState == State.PLAYING) {
            if (currentPlayer == Seed.BLACK) {
                backgroundColor = new Color(255, 248, 233);
                foregroundColor = new Color(154, 123, 109);
            } else {
                backgroundColor = new Color(154, 123, 109);
                foregroundColor = new Color(255, 248, 233);
            }
        } else {
            backgroundColor = Color.RED;
            foregroundColor = Color.WHITE;
        }


        restartButton.setBackground(backgroundColor);
        restartButton.setForeground(foregroundColor);
        homeButton.setBackground(backgroundColor);
        homeButton.setForeground(foregroundColor);
    }


    /** The entry main() method */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Othello());
    }
}

