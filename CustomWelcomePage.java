import Sudoku.SudokuMain;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class CustomWelcomePage extends JFrame {
    private Image backgroundImage;

    public CustomWelcomePage() {
        setSize(600, 600);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        try {
            backgroundImage = ImageIO.read(new File("TictactoeExtends\\src\\Image\\WelcomePageCustom\\BG.png"));
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


        setLayout(new BorderLayout());
        add(mainPanel, BorderLayout.CENTER);

        setVisible(true);

        new SudokuMain();
    }

    public static void main(String[] args) {
        new CustomWelcomePage();
    }

    public void difficultyPage() {
        JFrame difficultyFrame = new JFrame("Difficulty Page");
        difficultyFrame.setSize(600, 600);
        difficultyFrame.setResizable(false);
        difficultyFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Image backgroundImage = null;
        Image easyButtonImage = null;
        Image hardButtonImage = null;
        Image mediumButtonImage = null;
        Image homeButtonImage = null;

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

        JButton easyButton = new JButton();
        if (easyButtonImage != null) {
            easyButton.setIcon(new javax.swing.ImageIcon(easyButtonImage));
        }
        easyButton.setBounds(165, 270, 255, 69);
        easyButton.setContentAreaFilled(false);
        easyButton.setBorderPainted(false);
        easyButton.setFocusPainted(false);
        easyButton.addActionListener(e -> {
            new GameMain(true, Difficulty.EASY);
            difficultyFrame.dispose();
        });

        JButton mediumButton = new JButton();
        if (mediumButtonImage != null) {
            mediumButton.setIcon(new javax.swing.ImageIcon(mediumButtonImage));
        }
        mediumButton.setBounds(165, 270 + 69 + 5, 255, 69);
        mediumButton.setContentAreaFilled(false);
        mediumButton.setBorderPainted(false);
        mediumButton.setFocusPainted(false);
        mediumButton.addActionListener(e -> {
            new GameMain(true, Difficulty.MEDIUM);
            difficultyFrame.dispose();
        });

        JButton hardButton = new JButton();
        if (hardButtonImage != null) {
            hardButton.setIcon(new javax.swing.ImageIcon(hardButtonImage));
        }
        hardButton.setBounds(165, 270 + 2 * (69 + 5), 255, 69);
        hardButton.setContentAreaFilled(false);
        hardButton.setBorderPainted(false);
        hardButton.setFocusPainted(false);
        hardButton.addActionListener(e -> {
            new GameMain(true, Difficulty.HARD);
            difficultyFrame.dispose();
        });

        JButton homeButton = new JButton();
        if (homeButtonImage != null) {
            homeButton.setIcon(new javax.swing.ImageIcon(homeButtonImage));
        }
        homeButton.setBounds(10, 480, 70, 70);
        homeButton.setContentAreaFilled(false);
        homeButton.setBorderPainted(false);
        homeButton.setFocusPainted(false);
        homeButton.addActionListener(e -> {
            new CustomWelcomePage(); 
            difficultyFrame.dispose();
        });

        mainPanel.add(easyButton);
        mainPanel.add(mediumButton);
        mainPanel.add(hardButton);
        mainPanel.add(homeButton);

        difficultyFrame.add(mainPanel);
        difficultyFrame.setVisible(true);
    }
}