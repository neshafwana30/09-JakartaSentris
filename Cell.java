import java.awt.Color;
import java.awt.Font;
import javax.swing.JTextField;

/**
 * The Cell class model the cells of the Sudoku puzzle, by customizing
 * (subclass)
 * the javax.swing.JTextField to include row/column, puzzle number and status.
 */
public class Cell extends JTextField {
    private static final long serialVersionUID = 1L; // to prevent serial warning

    // Define named constants for JTextField's colors and fonts
    // to be chosen based on CellStatus
    public static final Color BG_GIVEN = new Color(219, 237, 248);
    public static final Color FG_GIVEN = new Color(62, 115, 167);
    public static final Color BG_HINT = new Color(219, 237, 248);
    public static final Color FG_HINT = new Color(113, 162, 210);
    public static final Color FG_NOT_GIVEN = new Color(198, 221, 230);
    public static final Color BG_TO_GUESS = new Color(122, 172, 221);
    public static final Color BG_CORRECT_GUESS = new Color(150, 214, 170);
    public static final Color BG_WRONG_GUESS = new Color(255, 182, 193);
    public static final Color BG_ALREADY_HAVE_NUMBER = new Color(173, 216, 230);
    public static final Color BG_HIGHLIGHT = new Color(174, 150, 214);
    public static final Color FONT_WHITE = Color.WHITE;
    public static final Font FONT_NUMBERS = new Font("ARIAL", Font.BOLD, 28);

    // Define properties (package-visible)
    /** The row and column number [0-8] of this cell */
    int row, col;
    /** The puzzle number [1-9] for this cell */
    int number;
    /** The status of this cell defined in enum CellStatus */
    CellStatus status;

    /** Constructor */
    public Cell(int row, int col) {
        super(); // JTextField
        this.row = row;
        this.col = col;

        // Inherited from JTextField: Beautify all the cells once for all
        super.setHorizontalAlignment(JTextField.CENTER);
        super.setFont(FONT_NUMBERS);
        // super.setToolTipText("Input number 1-9 only"); // Set tooltip tex
    }

    /** Reset this cell for a new game, given the puzzle number and isGiven */
    public void newGame(int number, boolean isGiven) {
        this.number = number;
        status = isGiven ? CellStatus.GIVEN : CellStatus.TO_GUESS;
        paint(); // paint itself
    }

    /** This Cell (JTextField) paints itself based on its status */
    public void paint() {
        if (status == CellStatus.GIVEN) {
            // Inherited from JTextField: Set display properties
            super.setText(number + "");
            super.setEditable(false);
            super.setBackground(BG_GIVEN);
            super.setForeground(FG_GIVEN);
        } else if (status == CellStatus.TO_GUESS) {
            // Inherited from JTextField: Set display properties
            super.setText("");
            super.setEditable(true);
            super.setBackground(BG_TO_GUESS);
            super.setForeground(FG_NOT_GIVEN);
        } else if (status == CellStatus.HINT) {
            super.setText(number + "");
            super.setEditable(false);
            super.setBackground(BG_HINT);
            super.setForeground(FG_HINT);
        } else if (status == CellStatus.CORRECT_GUESS) { // from TO_GUESS
            super.setBackground(BG_CORRECT_GUESS);
            super.setForeground(FONT_WHITE);
        } else if (status == CellStatus.WRONG_GUESS) { // from TO_GUESS
            super.setBackground(BG_WRONG_GUESS);
            super.setForeground(FONT_WHITE);
        }
    }

    public void reset() {
        setText(""); // Clear the cell's text
        status = CellStatus.TO_GUESS; // Reset the status
        setBackground(Cell.BG_TO_GUESS); // Reset the background color
        paint(); // Repaint the cell
    }
}