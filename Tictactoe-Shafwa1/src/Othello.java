import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Othello extends JFrame {
    private static final long serialVersionUID = 1L;
    private static final int ROWS = 8, COLS = 8, CELL_SIZE = 80, SYMBOL_SIZE = CELL_SIZE - 20;
    private BufferedImage blackIcon, whiteIcon;
    private Seed currentPlayer;
    private Seed[][] board;
    private State currentState;
    private ArrayList<int[]> legalMoves;
    private JLabel statusTextLabel;

    public enum Seed {
        BLACK, WHITE, NO_SEED
    }

    public enum State {
        PLAYING, DRAW, CROSS_WON, NOUGHT_WON
    }

    public Othello() {
        initGame();
        legalMoves = new ArrayList<>();
        loadIcons();
        setupUI();
        newGame();
    }

    private void loadIcons() {
        try {
            blackIcon = ImageIO.read(new File("TictactoeExtends\\src\\Image\\Othello\\Black.png"));
            whiteIcon = ImageIO.read(new File("TictactoeExtends\\src\\Image\\Othello\\White.png"));
        } catch (IOException e) {
            e.printStackTrace();
            blackIcon = new BufferedImage(SYMBOL_SIZE, SYMBOL_SIZE, BufferedImage.TYPE_INT_ARGB);
            whiteIcon = new BufferedImage(SYMBOL_SIZE, SYMBOL_SIZE, BufferedImage.TYPE_INT_ARGB);
        }
    }

    private void setupUI() {
        GamePanel gamePanel = new GamePanel();
        gamePanel.setPreferredSize(new Dimension(CELL_SIZE * COLS, CELL_SIZE * ROWS));

        statusTextLabel = new JLabel();
        statusTextLabel.setFont(new Font("Arial", Font.BOLD, 20));
        statusTextLabel.setHorizontalAlignment(JLabel.CENTER);

        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBackground(new Color(154, 123, 109));
        statusBar.add(statusTextLabel, BorderLayout.CENTER);

        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());
        cp.add(statusBar, BorderLayout.PAGE_START);
        cp.add(gamePanel, BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setTitle("Othello");
        setResizable(false);
        setVisible(true);
    }

    private void initGame() {
        board = new Seed[ROWS][COLS];
    }

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

    private boolean isLegalMove(Seed mySeed, int rowSelected, int colSelected) {
        if (board[rowSelected][colSelected] != Seed.NO_SEED)
            return false;
        Seed opponentSeed = (mySeed == Seed.BLACK) ? Seed.WHITE : Seed.BLACK;
        return checkDirection(mySeed, opponentSeed, rowSelected, colSelected, 1, 0) || // Right
                checkDirection(mySeed, opponentSeed, rowSelected, colSelected, -1, 0) || // Left
                checkDirection(mySeed, opponentSeed, rowSelected, colSelected, 0, 1) || // Down
                checkDirection(mySeed, opponentSeed, rowSelected, colSelected, 0, -1); // Up
    }

    private boolean checkDirection(Seed mySeed, Seed opponentSeed, int row, int col, int rowDir, int colDir) {
        int r = row + rowDir, c = col + colDir;
        boolean hasOpponentSeed = false;
        while (r >= 0 && r < ROWS && c >= 0 && c < COLS && board[r][c] == opponentSeed) {
            r += rowDir;
            c += colDir;
            hasOpponentSeed = true;
        }
        return hasOpponentSeed && r >= 0 && r < ROWS && c >= 0 && c < COLS && board[r][c] == mySeed;
    }

    private void updateStatusBar() {
        String statusText = (currentState == State.PLAYING)
                ? ((currentPlayer == Seed.BLACK) ? "Brown's Turn" : "White's Turn")
                : "Game Over";
        statusTextLabel.setText(statusText);
    }

    private class GamePanel extends JPanel {
        private static final long serialVersionUID = 1L;

        public GamePanel() {
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    int rowSelected = e.getY() / CELL_SIZE;
                    int colSelected = e.getX() / CELL_SIZE;
                    if (currentState == State.PLAYING) {
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
                g.fillRect(0, CELL_SIZE * row - 4, CELL_SIZE * COLS, 8);
            }
            for (int col = 1; col < COLS; ++col) {
                g.fillRect(CELL_SIZE * col - 4, 0, 8, CELL_SIZE * ROWS);
            }
            for (int row = 0; row < ROWS; ++row) {
                for (int col = 0; col < COLS; ++col) {
                    int x1 = col * CELL_SIZE + 10;
                    int y1 = row * CELL_SIZE + 10;
                    if (board[row][col] == Seed.BLACK) {
                        g.drawImage(blackIcon, x1, y1, SYMBOL_SIZE, SYMBOL_SIZE, this);
                    } else if (board[row][col] == Seed.WHITE) {
                        g.drawImage(whiteIcon, x1, y1, SYMBOL_SIZE, SYMBOL_SIZE, this);
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Othello::new);
    }
}