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
    private JButton homeButton; // Declare the homeButton
    private Difficulty difficulty;
    private CustomWelcomePage page;
    private boolean isVsComputer;
    private Seed playerSeed;

    public GameMain(boolean isVsComputer, Difficulty difficulty, Seed playerSeed) {
        this.isVsComputer = isVsComputer;
        this.playerSeed = playerSeed;
        this.difficulty = difficulty;
        board = new Board();
        if (isVsComputer) {
            aiPlayer = new AIPlayerMinimax(board, difficulty);
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

        if (isVsComputer) {
            statusLabel = new JLabel((currentPlayer == playerSeed ? "Your" : "Computer's") + " turn",
                    SwingConstants.CENTER);
        } else {
            statusLabel = new JLabel("CROSS's turn", SwingConstants.CENTER);
        }
        statusLabel.setFont(new Font("Arial", Font.BOLD, 14));
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setOpaque(true);
        statusLabel.setBackground(new Color(154, 123, 109));

        statusPanel.add(statusLabel, BorderLayout.CENTER);
        add(statusPanel, BorderLayout.SOUTH);

        replayButton = new RoundedButton("Replay");
        replayButton.addActionListener(e -> replayGame());

        homeButton = new RoundedButton("Home");
        homeButton.addActionListener(e -> goToHome());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(154, 123, 109)); // Set the background color to brown
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

                            SoundEffect.CLICK.play(); // Play click sound on move

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
            if (isVsComputer) {
                if (currentPlayer != playerSeed) {
                    statusLabel.setText("You Won!");
                    SoundEffect.WIN.play(); // Play win sound if player wins
                } else {
                    statusLabel.setText("Computer won!");
                    SoundEffect.DIE.play(); // Play die sound if computer wins
                }
            } else {
                statusLabel.setText("CROSS won!");
                SoundEffect.WIN.play(); // Play win sound for 2-player game
            }
        } else if (currentState == State.NOUGHT_WON) {
            if (isVsComputer) {
                if (currentPlayer != playerSeed) {
                    statusLabel.setText("You won!");
                    SoundEffect.WIN.play(); // Play win sound if player wins
                } else {
                    statusLabel.setText("Computer won!");
                    SoundEffect.DIE.play(); // Play die sound if computer wins
                }
            } else {
                statusLabel.setText("NOUGHT won!");
                SoundEffect.WIN.play(); // Play win sound for 2-player game
            }
        } else if (currentState == State.DRAW) {
            statusLabel.setText("It's a Draw!");
            SoundEffect.EXPLODE.play(); // Play explode sound for a draw
        } else {
            if (isVsComputer) {
                statusLabel.setText((currentPlayer == playerSeed ? "Your's" : "Computer's") + " turn");
            } else {
                statusLabel.setText((currentPlayer == Seed.CROSS ? "CROSS" : "NOUGHT") + "'s turn");
            }
        }
    }

    private void resetGame() {
        dispose();
        CustomWelcomePage welcomePage = new CustomWelcomePage();
        welcomePage.modeSelectionPage();
        welcomePage.dispose();
    }

    private void replayGame() {
        // Reset the game board and state without disposing of the window
        board.reset(); // Assuming there's a reset method in Board
        currentPlayer = Seed.CROSS;
        currentState = State.PLAYING;
        statusLabel.setText(isVsComputer ? "Your turn" : "CROSS's turn");
        board.repaint();
    }

    private void goToHome() {
        dispose();
        CustomWelcomePage welcomePage = new CustomWelcomePage();
        welcomePage.modeSelectionPage();
        welcomePage.dispose();
    }

}