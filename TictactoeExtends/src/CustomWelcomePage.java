import Sudoku.SudokuMain;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class CustomWelcomePage extends JFrame {
    private Image backgroundImage;
    private Image playButtonImage;
    private Image OthelloButtonImage;
    private Image easyButtonImage;
    private Image hardButtonImage;
    private Image mediumButtonImage;
    private Image homeButtonImage;
    private Image twoPlayerButtonImage;
    private Image vsComputerButtonImage;
    private Image noughtButtonImage;
    private Image crossButtonImage;
    private Image sudokuButtonImage; // Add image for Sudoku button

    public CustomWelcomePage() {
        setSize(600, 600);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Load the background image and button images
        try {
            backgroundImage = ImageIO.read(new File("TictactoeExtends\\src\\Image\\WelcomePageCustom\\BG.png"));
            playButtonImage = ImageIO.read(new File("TictactoeExtends\\src\\Image\\WelcomePageCustom\\PlayButton.png"));
            OthelloButtonImage = ImageIO
                    .read(new File("TictactoeExtends\\src\\Image\\WelcomePageCustom\\OthelloButton.png"));
            sudokuButtonImage = ImageIO
                    .read(new File("TictactoeExtends\\src\\Image\\WelcomePageCustom\\SudokuButton.png")); // Load Sudoku
                                                                                                          // button
                                                                                                          // image

            playButtonImage = playButtonImage.getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            OthelloButtonImage = OthelloButtonImage.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            sudokuButtonImage = sudokuButtonImage.getScaledInstance(120, 120, Image.SCALE_SMOOTH); // Scale Sudoku
                                                                                                   // button image

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Main panel with custom painting
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        mainPanel.setLayout(null); // Use null layout for absolute positioning

        // Create the play button
        JButton playButton = new JButton();
        if (playButtonImage != null) {
            playButton.setIcon(new ImageIcon(playButtonImage));
        }
        playButton.setBounds(220, 350, 150, 150);
        playButton.setContentAreaFilled(false);
        playButton.setBorderPainted(false);
        playButton.setFocusPainted(false);

        // Add action listener to the play button
        playButton.addActionListener(e -> {
            modeSelectionPage(); // Navigate to the difficulty page
            dispose(); // Close the welcome page
        });

        // Create the Othello button
        JButton othelloButton = new JButton();
        if (OthelloButtonImage != null) {
            othelloButton.setIcon(new ImageIcon(OthelloButtonImage));
        }
        othelloButton.setBounds(20, 450, 100, 100); // Set position and size of the button
        othelloButton.setContentAreaFilled(false);
        othelloButton.setBorderPainted(false);
        othelloButton.setFocusPainted(false);

        othelloButton.addActionListener(e -> {
            new Othello(); // Launch the Othello game
            dispose();
        });

        // Create the Sudoku button
        JButton sudokuButton = new JButton();
        if (sudokuButtonImage != null) {
            sudokuButton.setIcon(new ImageIcon(sudokuButtonImage));
        }
        sudokuButton.setBounds(460, 440, 120, 120); // Set position and size of the button
        sudokuButton.setContentAreaFilled(false);
        sudokuButton.setBorderPainted(false);
        sudokuButton.setFocusPainted(false);

        sudokuButton.addActionListener(e -> {
            new SudokuMain(); // Launch the Sudoku game
            dispose();
        });

        mainPanel.add(playButton);
        mainPanel.add(othelloButton);
        mainPanel.add(sudokuButton); // Add Sudoku button to the panel

        setLayout(new BorderLayout());
        add(mainPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    void modeSelectionPage() {
        JFrame modeSelectionFrame = new JFrame("Mode Selection Page");
        modeSelectionFrame.setSize(600, 600);
        modeSelectionFrame.setResizable(false);
        modeSelectionFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        try {
            backgroundImage = ImageIO.read(new File("TictactoeExtends\\src\\Image\\ModeSelection\\BGDP.png"));
            twoPlayerButtonImage = ImageIO.read(new File("TictactoeExtends\\src\\Image\\ModeSelection\\2player.png"));
            vsComputerButtonImage = ImageIO.read(new File("TictactoeExtends\\src\\Image\\ModeSelection\\VsComp.png"));
            homeButtonImage = ImageIO.read(new File("TictactoeExtends\\src\\Image\\ModeSelection\\home.png"));

            twoPlayerButtonImage = twoPlayerButtonImage.getScaledInstance(306, 80, Image.SCALE_SMOOTH);
            vsComputerButtonImage = vsComputerButtonImage.getScaledInstance(306, 85, Image.SCALE_SMOOTH);
            homeButtonImage = homeButtonImage.getScaledInstance(70, 70, Image.SCALE_SMOOTH);
        } catch (IOException e) {
            e.printStackTrace();
        }

        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        mainPanel.setLayout(null);

        JButton twoPlayerButton = new JButton();
        if (twoPlayerButtonImage != null) {
            twoPlayerButton.setIcon(new ImageIcon(twoPlayerButtonImage));
        }
        twoPlayerButton.setBounds(125, 270 + 85 + 10, 306, 85);
        twoPlayerButton.setContentAreaFilled(false);
        twoPlayerButton.setBorderPainted(false);
        twoPlayerButton.setFocusPainted(false);
        twoPlayerButton.addActionListener(e -> {
            new GameMain(false, null, Seed.CROSS); // Start game in 2 Player mode
            modeSelectionFrame.dispose();
        });

        JButton vsComputerButton = new JButton();
        if (vsComputerButtonImage != null) {
            vsComputerButton.setIcon(new ImageIcon(vsComputerButtonImage));
        }
        vsComputerButton.setBounds(125, 270 + 5, 306, 85);
        vsComputerButton.setContentAreaFilled(false);
        vsComputerButton.setBorderPainted(false);
        vsComputerButton.setFocusPainted(false);
        vsComputerButton.addActionListener(e -> {
            selectRolePage();
            modeSelectionFrame.dispose();
        });

        JButton homeButton = new JButton();
        if (homeButtonImage != null) {
            homeButton.setIcon(new ImageIcon(homeButtonImage));
        }
        homeButton.setBounds(10, 480, 70, 70);
        homeButton.setContentAreaFilled(false);
        homeButton.setBorderPainted(false);
        homeButton.setFocusPainted(false);
        homeButton.addActionListener(e -> {
            new CustomWelcomePage();
            modeSelectionFrame.dispose();
        });

        mainPanel.add(twoPlayerButton);
        mainPanel.add(vsComputerButton);
        mainPanel.add(homeButton);

        modeSelectionFrame.add(mainPanel);
        modeSelectionFrame.setVisible(true);
    }

    public void selectRolePage() {
        JFrame roleSelectionFrame = new JFrame("Select Role");
        roleSelectionFrame.setSize(600, 600);
        roleSelectionFrame.setResizable(false);
        roleSelectionFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        try {
            backgroundImage = ImageIO.read(new File("TictactoeExtends\\src\\Image\\rolePage\\bg.png"));
            noughtButtonImage = ImageIO.read(new File("TictactoeExtends\\src\\Image\\rolePage\\nought.png"));
            crossButtonImage = ImageIO.read(new File("TictactoeExtends\\src\\Image\\rolePage\\cross.png"));
            homeButtonImage = ImageIO.read(new File("TictactoeExtends\\src\\Image\\ModeSelection\\home.png"));

            noughtButtonImage = noughtButtonImage.getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            crossButtonImage = crossButtonImage.getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            homeButtonImage = homeButtonImage.getScaledInstance(70, 70, Image.SCALE_SMOOTH);
        } catch (IOException e) {
            e.printStackTrace();
        }

        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        mainPanel.setLayout(null);

        JButton noughtButton = new JButton();
        if (noughtButtonImage != null) {
            noughtButton.setIcon(new ImageIcon(noughtButtonImage));
        }
        noughtButton.setBounds(120, 300, 150, 150);
        noughtButton.setContentAreaFilled(false);
        noughtButton.setBorderPainted(false);
        noughtButton.setFocusPainted(false);
        noughtButton.addActionListener(e -> {
            difficultyPage(Seed.NOUGHT);
            roleSelectionFrame.dispose();
        });

        JButton crossButton = new JButton();
        if (crossButtonImage != null) {
            crossButton.setIcon(new ImageIcon(crossButtonImage));
        }
        crossButton.setBounds(325, 300, 150, 150);
        crossButton.setContentAreaFilled(false);
        crossButton.setBorderPainted(false);
        crossButton.setFocusPainted(false);
        crossButton.addActionListener(e -> {
            difficultyPage(Seed.CROSS);
            roleSelectionFrame.dispose();
        });

        JButton homeButton = new JButton();
        if (homeButtonImage != null) {
            homeButton.setIcon(new ImageIcon(homeButtonImage));
        }
        homeButton.setBounds(10, 480, 70, 70);
        homeButton.setContentAreaFilled(false);
        homeButton.setBorderPainted(false);
        homeButton.setFocusPainted(false);
        homeButton.addActionListener(e -> {
            new CustomWelcomePage();
            dispose();
        });

        mainPanel.add(noughtButton);
        mainPanel.add(crossButton);
        mainPanel.add(homeButton);

        roleSelectionFrame.add(mainPanel);
        roleSelectionFrame.setVisible(true);
    }

    public void difficultyPage(Seed playerSeed) {
        JFrame difficultyFrame = new JFrame("Difficulty Page");
        difficultyFrame.setSize(600, 600);
        difficultyFrame.setResizable(false);
        difficultyFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        try {
            backgroundImage = ImageIO.read(new File("TictactoeExtends\\src\\Image\\DifficultyPage\\BGDP.png"));
            easyButtonImage = ImageIO.read(new File("TictactoeExtends\\src\\Image\\DifficultyPage\\easy.png"));
            hardButtonImage = ImageIO.read(new File("TictactoeExtends\\src\\Image\\DifficultyPage\\hard.png"));
            mediumButtonImage = ImageIO.read(new File("TictactoeExtends\\src\\Image\\DifficultyPage\\med.png"));
            homeButtonImage = ImageIO.read(new File("TictactoeExtends\\src\\Image\\DifficultyPage\\home.png"));

            easyButtonImage = easyButtonImage.getScaledInstance(255, 69, Image.SCALE_SMOOTH);
            hardButtonImage = hardButtonImage.getScaledInstance(255, 69, Image.SCALE_SMOOTH);
            mediumButtonImage = mediumButtonImage.getScaledInstance(255, 69, Image.SCALE_SMOOTH);
            homeButtonImage = homeButtonImage.getScaledInstance(70, 70, Image.SCALE_SMOOTH);
        } catch (IOException e) {
            e.printStackTrace();
        }

        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        mainPanel.setLayout(null);

        // Create the easy button
        JButton easyButton = new JButton();
        if (easyButtonImage != null) {
            easyButton.setIcon(new javax.swing.ImageIcon(easyButtonImage));
        }
        easyButton.setBounds(165, 270, 255, 69);
        easyButton.setContentAreaFilled(false);
        easyButton.setBorderPainted(false);
        easyButton.setFocusPainted(false);
        easyButton.addActionListener(e -> {
            new GameMain(true, Difficulty.EASY, playerSeed); // Pass playerSeed
            difficultyFrame.dispose();
        });

        // Create the medium button
        JButton mediumButton = new JButton();
        if (mediumButtonImage != null) {
            mediumButton.setIcon(new javax.swing.ImageIcon(mediumButtonImage));
        }
        mediumButton.setBounds(165, 270 + 69 + 5, 255, 69);
        mediumButton.setContentAreaFilled(false);
        mediumButton.setBorderPainted(false);
        mediumButton.setFocusPainted(false);
        mediumButton.addActionListener(e -> {
            new GameMain(true, Difficulty.MEDIUM, playerSeed); // Pass playerSeed
            difficultyFrame.dispose(); // Close the difficulty frame
        });

        // Create the hard button
        JButton hardButton = new JButton();
        if (hardButtonImage != null) {
            hardButton.setIcon(new javax.swing.ImageIcon(hardButtonImage));
        }
        hardButton.setBounds(165, 270 + 2 * (69 + 5), 255, 69);
        hardButton.setContentAreaFilled(false);
        hardButton.setBorderPainted(false);
        hardButton.setFocusPainted(false);
        hardButton.addActionListener(e -> {
            new GameMain(true, Difficulty.HARD, playerSeed); // Pass playerSeed
            difficultyFrame.dispose(); // Close the difficulty frame
        });

        // Create the home button
        JButton homeButton = new JButton();
        if (homeButtonImage != null) {
            homeButton.setIcon(new javax.swing.ImageIcon(homeButtonImage));
        }
        homeButton.setBounds(10, 480, 70, 70);
        homeButton.setContentAreaFilled(false);
        homeButton.setBorderPainted(false);
        homeButton.setFocusPainted(false);
        homeButton.addActionListener(e -> {
            new CustomWelcomePage(); // Navigate back to the welcome page
            difficultyFrame.dispose(); // Close the difficulty frame
        });

        mainPanel.add(easyButton);
        mainPanel.add(mediumButton);
        mainPanel.add(hardButton);
        mainPanel.add(homeButton);

        difficultyFrame.add(mainPanel);
        difficultyFrame.setVisible(true);
    }

    public static void main(String[] args) {
        new CustomWelcomePage();
    }
}