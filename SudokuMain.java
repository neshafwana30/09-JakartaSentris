import javax.swing.JFrame;

public class SudokuMain extends JFrame {
    private static final long serialVersionUID = 1L;

    GameBoardPanel board = new GameBoardPanel();

    public SudokuMain() {
        board.welcomeDialog();
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> new SudokuMain());

    }

}