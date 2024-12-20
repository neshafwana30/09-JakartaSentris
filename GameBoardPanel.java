package Sudoku;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

public final class GameBoardPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private JFrame frame;

    public static final int CELL_SIZE = 60;
    public static final int BOARD_WIDTH = CELL_SIZE * SudokuConstants.GRID_SIZE;
    public static final int BOARD_HEIGHT = CELL_SIZE * SudokuConstants.GRID_SIZE;

    private static final int MAX_LIVES = 5;
    private int remainingLives = MAX_LIVES;
    private JTextField statusBar;

    private boolean hintMode = false;

    private Cell[][] cells = new Cell[SudokuConstants.GRID_SIZE][SudokuConstants.GRID_SIZE];
    private Puzzle puzzle = new Puzzle();

    public GameBoardPanel() {
        super.setLayout(new GridLayout(SudokuConstants.GRID_SIZE, SudokuConstants.GRID_SIZE));

        // Initialize cells
        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                cells[row][col] = new Cell(row, col);
                int top = (row % SudokuConstants.SUBGRID_SIZE == 0) ? 3 : 1;
                int left = (col % SudokuConstants.SUBGRID_SIZE == 0) ? 3 : 1;
                int bottom = (row == SudokuConstants.GRID_SIZE - 1) ? 3 : 1;
                int right = (col == SudokuConstants.GRID_SIZE - 1) ? 3 : 1;
                Border border = BorderFactory.createMatteBorder(top, left, bottom, right, new Color(62, 115, 167));
                Border thinGrayBorder = BorderFactory.createMatteBorder(
                        (top == 1) ? 1 : 0, (left == 1) ? 1 : 0, (bottom == 1) ? 1 : 0, (right == 1) ? 1 : 0,
                        Color.WHITE);
                cells[row][col].setBorder(BorderFactory.createCompoundBorder(border, thinGrayBorder));
                super.add(cells[row][col]);
            }
        }

        // Create and set up the frame
        frame = new JFrame("Sudoku Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(BOARD_WIDTH, BOARD_HEIGHT + 50); // Add extra height for status bar
        frame.setLocationRelativeTo(null); // Center the frame on the screen
        frame.setLayout(new BorderLayout());

        // Add the menu bar to the frame
        JPanel menuBar = createMenuBar();

        // Add listeners
        CellInputListener listener = new CellInputListener();
        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                if (cells[row][col].isEditable()) {
                    cells[row][col].addActionListener(listener);
                }
            }
        }

        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                cells[row][col].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        Cell sourceCell = (Cell) e.getSource();
                        if (sourceCell.status == CellStatus.CORRECT_GUESS || sourceCell.status == CellStatus.GIVEN) {
                            if (sourceCell.getBackground().equals(Cell.BG_HIGHLIGHT)) {
                                resetAllCellColors();
                            } else {
                                highlightSameNumbers(sourceCell.number);
                            }
                        }
                    }
                });
            }
        }

        super.setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));// Create and set up the frame
        frame = new JFrame("Sudoku Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(BOARD_WIDTH, BOARD_HEIGHT + 50); // Add extra height for status bar
        frame.setLocationRelativeTo(null); // Center the frame on the screen
        frame.setLayout(new BorderLayout());
        frame.add(this, BorderLayout.CENTER);
        menuBar.add(statusBar, BorderLayout.EAST);
        frame.add(menuBar, BorderLayout.NORTH);
        // frame.add(statusBar, BorderLayout.NORTH);

    }

    public void show() {
        frame.setVisible(true);
    }

    // Make new game
    public void newGame(int cellsToGuess) {
        resetRemainingLives();
        // Generate a new puzzle
        puzzle.newPuzzle(cellsToGuess);
        // Initialize all the 9x9 cells, based on the puzzle.
        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                cells[row][col].newGame(puzzle.numbers[row][col], puzzle.isGiven[row][col]);
            }
        }
    }

    /**
     * Return true if the puzzle is solved
     * i.e., none of the cell have status of TO_GUESS or WRONG_GUESS
     */
    public boolean isSolved() {
        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                if (cells[row][col].status == CellStatus.TO_GUESS || cells[row][col].status == CellStatus.WRONG_GUESS) {
                    return false;
                }
            }
        }
        return true;
    }

    public class CellInputListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Cell sourceCell = (Cell) e.getSource();
            if (sourceCell.getText().isEmpty()) {
                // Reset the cell if the input is empty
                sourceCell.status = CellStatus.TO_GUESS;
                sourceCell.setBackground(Cell.BG_TO_GUESS);
                sourceCell.paint();
                resetAllCellColors();
                return;
            }
            int numberIn;
            try {
                numberIn = Integer.parseInt(sourceCell.getText());
            } catch (NumberFormatException ex) {
                sourceCell.setText(""); // Clear invalid input
                sourceCell.setBackground(Cell.BG_WRONG_GUESS);
                JOptionPane.showMessageDialog(null, "Please enter a number between 1 and 9.");
                return;
            }
            if (numberIn < 1 || numberIn > 9) {
                sourceCell.setText(""); // Clear invalid input
                sourceCell.setBackground(Cell.BG_WRONG_GUESS);
                JOptionPane.showMessageDialog(null, "Please enter a number between 1 and 9.");
                return;
            }
            // Check for conflicts
            boolean hasConflict = checkForConflicts(sourceCell.row, sourceCell.col, numberIn);
            if (hasConflict) {
                sourceCell.status = CellStatus.WRONG_GUESS;
                incorrectGuess();
            } else {
                sourceCell.status = CellStatus.CORRECT_GUESS;
                sourceCell.setBackground(Cell.BG_CORRECT_GUESS); // Set background for correct guess
                resetAllCellColors();
                highlightSameNumbers(sourceCell.number);
            }
            // Re-paint the cell based on its status
            sourceCell.paint();

            if (isSolved()) {
                AudioPlayer.playAudioInBackground("src/Image/Audio/benar.wav");
                int response = JOptionPane.showOptionDialog(
                        null,
                        "Congratulations! You have solved the puzzle!",
                        "Solved!",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null,
                        new String[] { "New Game" },
                        "New Game");
                if (response == JOptionPane.OK_OPTION) {
                    showDifficultyDialog();
                } else {
                    System.exit(0);
                }
            }
        }
    }

    public void showDifficultyDialog() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Dimension fixedSize = new Dimension(500, 625);
        frame.setSize(fixedSize);
        frame.setMinimumSize(fixedSize);
        frame.setMaximumSize(fixedSize);
        frame.setPreferredSize(fixedSize);
        frame.setResizable(false); // Disable resizing

        frame.setLocationRelativeTo(null);

        // Create a panel with a background image
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon icon = new ImageIcon("TictactoeExtends\\src\\Sudoku\\Image\\BG2.png");
                g.drawImage(icon.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        // Difficulty buttons
        String[] options = { "Easy", "Normal", "Hard", "Extreme", "Insane" };
        String[] imagePaths = {
                "TictactoeExtends\\src\\Sudoku\\Image\\Difficulty\\Easy.png",
                "TictactoeExtends\\src\\Sudoku\\Image\\Difficulty\\Normal.png",
                "TictactoeExtends\\src\\Sudoku\\Image\\Difficulty\\Hard.png",
                "TictactoeExtends\\src\\Sudoku\\Image\\Difficulty\\Extreme.png",
                "TictactoeExtends\\src\\Sudoku\\Image\\Difficulty\\Insane.png"
        };

        panel.add(Box.createVerticalStrut(30));
        for (int i = 0; i < options.length; i++) {
            ImageIcon sudokuIcon = new ImageIcon(imagePaths[i]);
            Image scaledSudoku = sudokuIcon.getImage().getScaledInstance(300, 300, java.awt.Image.SCALE_SMOOTH);
            ImageIcon scaledSudokuI = new ImageIcon(scaledSudoku);

            JButton button = new JButton();
            button.setIcon(scaledSudokuI);
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            button.addActionListener(new DifficultyButtonListener(options[i], frame));

            // Make the button look like the image itself
            button.setContentAreaFilled(false);
            button.setBorderPainted(false);
            button.setFocusPainted(false);

            Dimension buttonSize = new Dimension(300, 100);
            button.setPreferredSize(buttonSize);
            button.setMinimumSize(buttonSize);
            button.setMaximumSize(buttonSize);

            panel.add(Box.createVerticalStrut(0)); // jarak antar button
            panel.add(button);
        }

        frame.add(panel);
        frame.setVisible(true); // Show the GameBoardPanel
        frame.revalidate();
        frame.repaint();
    }

    private class DifficultyButtonListener implements ActionListener {
        private final String difficulty;
        private final JFrame frame;

        public DifficultyButtonListener(String difficulty, JFrame frame) {
            this.difficulty = difficulty;
            this.frame = frame;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            int cellsToGuess;
            switch (difficulty) {
                case "Easy":
                    cellsToGuess = 15;
                    break;
                case "Normal":
                    cellsToGuess = 25;
                    break;
                case "Hard":
                    cellsToGuess = 35;
                    break;
                case "Extreme":
                    cellsToGuess = 40;
                    break;
                case "Insane":
                    cellsToGuess = 45;
                    break;
                default:

                    return;
            }
            // Start a new game with the selected difficulty
            // newGame(cellsToGuess); // Uncomment and implement this method in your game
            // logic
            newGame(cellsToGuess); // Start a new game with the selected difficulty
            frame.dispose(); // Close the difficulty selection dialog
            show();
            frame.revalidate();
        }
    }

    public JPanel createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // File Menu
        JMenu fileMenu = new JMenu("Game");
        JMenuItem newGameItem = new JMenuItem("New Game");
        JMenuItem resetGameItem = new JMenuItem("Reset Game");
        JMenuItem exitItem = new JMenuItem("Exit");

        newGameItem.addActionListener(e -> showDifficultyDialog());
        resetGameItem.addActionListener(e -> resetGame());
        exitItem.addActionListener(e -> System.exit(0));

        fileMenu.add(newGameItem);
        fileMenu.add(resetGameItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        // Options Menu
        JMenu optionsMenu = new JMenu("Options");
        JMenuItem hintButton = new JMenuItem("Hint");
        hintButton.addActionListener(e -> enableHintMode());
        optionsMenu.add(hintButton);

        // Help Menu
        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> JOptionPane.showMessageDialog(this, "Sudoku Game made by JakartaSentris!"));
        helpMenu.add(aboutItem);

        menuBar.add(fileMenu);
        menuBar.add(optionsMenu);
        menuBar.add(helpMenu);

        // Create a panel to hold the menu bar and status bar
        JPanel menuPanel = new JPanel(new BorderLayout());
        menuPanel.add(menuBar, BorderLayout.CENTER);

        // Initialize and add the status bar to the east
        statusBar = new JTextField("Lives remaining: " + remainingLives + " ");
        statusBar.setEditable(false);
        Font boldFont = statusBar.getFont().deriveFont(Font.BOLD);
        statusBar.setFont(boldFont);
        menuPanel.add(statusBar, BorderLayout.EAST);

        return menuPanel;
    }

    public boolean checkForConflicts(int row, int col, int number) {
        boolean conflict = false;
        // Check row and column
        for (int i = 0; i < SudokuConstants.GRID_SIZE; i++) {
            if (cells[row][i].number == number && i != col && cells[row][i].status == CellStatus.GIVEN) {
                conflict = true;
            }
            if (cells[i][col].number == number && i != row && cells[row][i].status == CellStatus.GIVEN) {
                conflict = true;
            }
        }
        // Check sub-grid
        int subGridRowStart = (row / SudokuConstants.SUBGRID_SIZE) * SudokuConstants.SUBGRID_SIZE;
        int subGridColStart = (col / SudokuConstants.SUBGRID_SIZE) * SudokuConstants.SUBGRID_SIZE;
        for (int r = subGridRowStart; r < subGridRowStart + SudokuConstants.SUBGRID_SIZE; r++) {
            for (int c = subGridColStart; c < subGridColStart + SudokuConstants.SUBGRID_SIZE; c++) {
                if (cells[r][c].number == number && (r != row || c != col)) {
                    conflict = true;
                }
            }
        }
        // If conflict is detected, highlight the entire row and column
        if (conflict) {
            for (int i = 0; i < SudokuConstants.GRID_SIZE; i++) {
                cells[row][i].setBackground(Cell.BG_ALREADY_HAVE_NUMBER); // Highlight entire row
                cells[i][col].setBackground(Cell.BG_ALREADY_HAVE_NUMBER); // Highlight entire column
            }
            for (int r = subGridRowStart; r < subGridRowStart + SudokuConstants.SUBGRID_SIZE; r++) {
                for (int c = subGridColStart; c < subGridColStart + SudokuConstants.SUBGRID_SIZE; c++) {
                    cells[r][c].setBackground(Cell.BG_ALREADY_HAVE_NUMBER); // Highlight entire subgrid
                    if (cells[r][c].number == number && (cells[r][c].status == CellStatus.GIVEN
                            || cells[r][c].status == CellStatus.CORRECT_GUESS
                            || cells[r][c].status == CellStatus.HINT)) {
                        cells[r][c].setBackground(Cell.BG_WRONG_GUESS);
                    }
                }
            }
            for (int i = 0; i < SudokuConstants.GRID_SIZE; i++) {
                if (cells[row][i].number == number && (cells[row][i].status == CellStatus.GIVEN
                        || cells[row][i].status == CellStatus.CORRECT_GUESS
                        || cells[row][i].status == CellStatus.HINT)) {
                    cells[row][i].setBackground(Cell.BG_WRONG_GUESS);
                }
                if (cells[i][col].number == number && (cells[i][col].status == CellStatus.GIVEN
                        || cells[i][col].status == CellStatus.CORRECT_GUESS
                        || cells[i][col].status == CellStatus.HINT)) {
                    cells[i][col].setBackground(Cell.BG_WRONG_GUESS);
                }
            }
        }
        return conflict;
    }

    public void resetAllCellColors() {
        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                cells[row][col].paint(); // Repaint each cell to its default color based on its status
            }
        }
    }

    public void resetGame() {
        resetRemainingLives();
        // Logic to reset the game
        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                if (!puzzle.isGiven[row][col]) {
                    // Reset only the cells that are not given
                    cells[row][col].reset();
                }
            }
        }
        resetAllCellColors();
    }

    public void incorrectGuess() {
        AudioPlayer.playAudioInBackground("TictactoeExtends\\src\\Sudoku\\Image\\Audio\\salah.wav");
        remainingLives--;
        updateRemainingLives();
        if (remainingLives <= 0) {
            gameOverPage();
        }
    }

    public void updateRemainingLives() {
        statusBar.setText("Lives remaining: " + remainingLives + "  ");
        Font boldFont = statusBar.getFont().deriveFont(Font.BOLD);
        statusBar.setFont(boldFont);
    }

    public void resetRemainingLives() {
        remainingLives = MAX_LIVES;
        statusBar.setText("Lives remaining: " + remainingLives + "  ");
        Font boldFont = statusBar.getFont().deriveFont(Font.BOLD);
        statusBar.setFont(boldFont);
    }

    public void highlightSameNumbers(int number) {
        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                if (cells[row][col].number == number &&
                        (cells[row][col].status == CellStatus.CORRECT_GUESS ||
                                cells[row][col].status == CellStatus.GIVEN ||
                                cells[row][col].status == CellStatus.HINT)) {
                    cells[row][col].setBackground(Cell.BG_HIGHLIGHT);
                    cells[row][col].setForeground(Cell.FONT_WHITE);
                } else {
                    cells[row][col].paint(); // Reset to default color based on status
                }
            }
        }
    }

    public void giveHint() {
        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                cells[row][col].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        Cell sourceCell = (Cell) e.getSource();
                        if (hintMode && sourceCell.status == CellStatus.TO_GUESS) {
                            // Reveal the correct number
                            sourceCell.setText(String.valueOf(puzzle.numbers[sourceCell.row][sourceCell.col]));
                            sourceCell.status = CellStatus.HINT;
                            sourceCell.paint();
                            remainingLives--;
                            updateRemainingLives();
                            hintMode = false; // Disable hint mode after use
                        }
                    }
                });
            }
        }
    }

    public void enableHintMode() {
        hintMode = true;
        JOptionPane.showMessageDialog(this, "Click on a cell to reveal its correct number.");
        giveHint();
    }

    public void welcomeDialog() {
        AudioPlayer.playAudioInBackground("src\\Image\\Audio\\bgmusic.wav");
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Dimension fixedSize = new Dimension(500, 625);
        frame.setSize(fixedSize);
        frame.setMinimumSize(fixedSize);
        frame.setMaximumSize(fixedSize);
        frame.setPreferredSize(fixedSize);
        frame.setResizable(false); // Disable resizing

        frame.setLocationRelativeTo(null);

        // Create a panel with a background image
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon icon = new ImageIcon("TictactoeExtends\\src\\Sudoku\\Image\\BG1.png");
                g.drawImage(icon.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        panel.add(Box.createVerticalGlue());

        ImageIcon sudokuIcon = new ImageIcon("TictactoeExtends\\src\\Sudoku\\Image\\Logo.png");
        Image ScaledSudoku = sudokuIcon.getImage().getScaledInstance(300, 300, java.awt.Image.SCALE_SMOOTH);
        ImageIcon scaledSudokuI = new ImageIcon(ScaledSudoku);

        // Create a JLabel with the scaled icon
        JLabel SudokuLabel = new JLabel(scaledSudokuI);
        SudokuLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add the label to the panel
        panel.add(SudokuLabel);

        ImageIcon playButton = new ImageIcon("TictactoeExtends\\src\\Sudoku\\Image\\Play.png");
        Image tempScaled = playButton.getImage().getScaledInstance(200, 200, java.awt.Image.SCALE_SMOOTH);
        ImageIcon scaledPlay = new ImageIcon(tempScaled);

        JButton button = new JButton();
        button.setIcon(scaledPlay);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showDifficultyDialog();
                frame.setVisible(false);
            }
        });

        // Make the button look like the image itself
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);

        Dimension buttonSize = new Dimension(200, 200);
        button.setPreferredSize(buttonSize);
        button.setMinimumSize(buttonSize);
        button.setMaximumSize(buttonSize);

        panel.add(button);

        frame.add(panel);
        panel.add(Box.createVerticalStrut(40)); // Adjust the value as needed
        frame.setVisible(true);
    }

    public void placeComponents(JPanel panel, JFrame frame) {
        panel.setLayout(null);

        // Membuat label selamat datang
        JLabel welcomeLabel = new JLabel("Welcome to the Game!");
        welcomeLabel.setBounds(130, 30, 200, 25);
        panel.add(welcomeLabel);

        // Membuat tombol untuk memulai game
        JButton startButton = new JButton("Start Game");
        startButton.setBounds(150, 80, 100, 25);
        panel.add(startButton);

        // Menambahkan aksi ketika tombol ditekan
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Menghapus panel dari frame
                frame.remove(panel);
                frame.revalidate();
                frame.repaint();
                showDifficultyDialog();
            }
        });
    }

    public void gameOverPage() {
        AudioPlayer.stopAudio();
        AudioPlayer.playAudio("TictactoeExtends\\src\\Sudoku\\Image\\Audio\\gameover.wav");
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Dimension fixedSize = new Dimension(500, 625);
        frame.setSize(fixedSize);
        frame.setMinimumSize(fixedSize);
        frame.setMaximumSize(fixedSize);
        frame.setPreferredSize(fixedSize);
        frame.setResizable(false);

        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon icon = new ImageIcon("TictactoeExtends\\src\\Sudoku\\Image\\GameOver\\Cover.png");
                g.drawImage(icon.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        panel.add(Box.createVerticalGlue());

        // Create ImageIcon for Reset button
        ImageIcon resetIcon = new ImageIcon("TictactoeExtends\\src\\Sudoku\\Image\\GameOver\\Reset.png");
        Image scaledResetImage = resetIcon.getImage().getScaledInstance(200, 200, java.awt.Image.SCALE_SMOOTH);
        ImageIcon scaledResetIcon = new ImageIcon(scaledResetImage);

        // Create Reset button
        JButton resetButton = new JButton();
        resetButton.setIcon(scaledResetIcon);
        resetButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        resetButton.setContentAreaFilled(false);
        resetButton.setBorderPainted(false);
        resetButton.setFocusPainted(false);

        // Set size for Reset button
        Dimension resetButtonSize = new Dimension(200, 200);
        resetButton.setPreferredSize(resetButtonSize);
        resetButton.setMinimumSize(resetButtonSize);
        resetButton.setMaximumSize(resetButtonSize);

        resetButton.addActionListener(e -> {
            resetGame();
            frame.dispose();
        });

        // Create ImageIcon for New Game button
        ImageIcon gameIcon = new ImageIcon("TictactoeExtends\\src\\Sudoku\\Image\\GameOver\\NewGame.png");
        Image scaledNewGameImage = gameIcon.getImage().getScaledInstance(200, 200, java.awt.Image.SCALE_SMOOTH);
        ImageIcon scaledgameIcon = new ImageIcon(scaledNewGameImage);

        // Create New Game button
        JButton gameButton = new JButton();
        gameButton.setIcon(scaledgameIcon);
        gameButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        gameButton.setContentAreaFilled(false);
        gameButton.setBorderPainted(false);
        gameButton.setFocusPainted(false);

        gameButton.addActionListener(e -> {
            showDifficultyDialog();
            frame.dispose();
        });

        // Set size for New Game button
        gameButton.setPreferredSize(resetButtonSize);
        gameButton.setMinimumSize(resetButtonSize);
        gameButton.setMaximumSize(resetButtonSize);

        // Adjust vertical struts to ensure proper spacing
        panel.add(Box.createVerticalStrut(300)); // Adjust the initial spacing as needed
        panel.add(resetButton);
        panel.add(Box.createVerticalStrut(-100)); // Increase this value to ensure New Game is not covered
        panel.add(gameButton);

        frame.add(panel);
        frame.setVisible(true);
    }
}
