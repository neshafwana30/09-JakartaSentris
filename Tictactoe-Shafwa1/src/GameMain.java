import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

public class GameMain extends JFrame {
    private Board board;
    private AIPlayer aiPlayer;
    private Seed currentPlayer;
    private State currentState;
    private JLabel statusLabel;
    private JButton replayButton;
    private JButton homeButton;
    private boolean isVsComputer;
    private Seed playerSeed;

    public GameMain() {
        // Set default values
        this.isVsComputer = true; // Default to playing against the computer
        this.playerSeed = Seed.CROSS; // Default player seed

        board = new Board();
        if (isVsComputer) {
            aiPlayer = new AIPlayerMinimax(board);
            aiPlayer.mySeed = (playerSeed == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;
            aiPlayer.oppSeed = playerSeed;
        }
        currentPlayer = Seed.CROSS;
        currentState = State.PLAYING;

        setLayout(new BorderLayout());
        board.setPreferredSize(new Dimension(Board.CANVAS_WIDTH, Board.CANVAS_HEIGHT));
        add(board, BorderLayout.CENTER);

        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        statusPanel.setBackground(new Color(154, 123, 109));

        statusLabel = new JLabel(isVsComputer ? "Your turn" : "CROSS's turn", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 14));
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setOpaque(true);
        statusLabel.setBackground(new Color(154, 123, 109));

        statusPanel.add(statusLabel, BorderLayout.CENTER);
        add(statusPanel, BorderLayout.SOUTH);

        replayButton = new JButton("Replay");
        replayButton.addActionListener(e -> replayGame());

        homeButton = new JButton("Home");
        homeButton.addActionListener(e -> System.out.println("home button"));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(154, 123, 109));
        buttonPanel.add(replayButton);
        buttonPanel.add(homeButton);
        add(buttonPanel, BorderLayout.NORTH);

        setTitle("Tic-Tac-Toe");
        setSize(Board.CANVAS_WIDTH, Board.CANVAS_HEIGHT + 60);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        playGame();
    }

    public void playGame() {
        board.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (currentState == State.PLAYING) {
                    int row = e.getY() / Cell.SIZE;
                    int col = e.getX() / Cell.SIZE;
                    if (isVsComputer && currentPlayer == aiPlayer.mySeed) {
                        makeAIMoveWithDelay();
                    } else {
                        if (board.getCells()[row][col].content == Seed.NO_SEED) {
                            currentState = board.stepGame(currentPlayer, row, col);
                            currentPlayer = (currentPlayer == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;
                            board.repaint();
                            updateGameState();

                            if (isVsComputer && currentState == State.PLAYING && currentPlayer == aiPlayer.mySeed) {
                                makeAIMoveWithDelay();
                            }
                        }
                    }
                }
            }
        });
    }

    private void makeAIMoveWithDelay() {
        Timer timer = new Timer(1000, e -> {
            int[] move = aiPlayer.move();
            if (move != null) {
                currentState = board.stepGame(aiPlayer.mySeed, move[0], move[1]);
                currentPlayer = aiPlayer.oppSeed;
                board.repaint();
                updateGameState();
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    private void updateGameState() {
        if (currentState == State.CROSS_WON) {
            statusLabel.setText(
                    isVsComputer ? (currentPlayer != playerSeed ? "You Won!" : "Computer won!") : "CROSS won!");
        } else if (currentState == State.NOUGHT_WON) {
            statusLabel.setText(
                    isVsComputer ? (currentPlayer != playerSeed ? "You won!" : "Computer won!") : "NOUGHT won!");
        } else if (currentState == State.DRAW) {
            statusLabel.setText("It's a Draw!");
        } else {
            statusLabel.setText(isVsComputer ? (currentPlayer == playerSeed ? "Your's turn" : "Computer's turn")
                    : (currentPlayer == Seed.CROSS ? "CROSS's turn" : "NOUGHT's turn"));
        }
    }

    private void replayGame() {
        board.reset();
        currentPlayer = Seed.CROSS;
        currentState = State.PLAYING;
        statusLabel.setText(isVsComputer ? "Your turn" : "CROSS's turn");
        board.repaint();
    }
}