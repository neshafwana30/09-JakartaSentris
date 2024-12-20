package Sudoku;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JTextField;

public class Cell extends JTextField {
    private static final long serialVersionUID = 1L; 

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

    public Cell(int row, int col) {
        super(); // JTextField
        this.row = row;
        this.col = col;

        super.setHorizontalAlignment(JTextField.CENTER);
        super.setFont(FONT_NUMBERS);
        
    }

    public void newGame(int number, boolean isGiven) {
        this.number = number;
        status = isGiven ? CellStatus.GIVEN : CellStatus.TO_GUESS;
        paint(); 
    }

    public void paint() {
        if (status == CellStatus.GIVEN) {
            super.setText(number + "");
            super.setEditable(false);
            super.setBackground(BG_GIVEN);
            super.setForeground(FG_GIVEN);
        } else if (status == CellStatus.TO_GUESS) {
            super.setText("");
            super.setEditable(true);
            super.setBackground(BG_TO_GUESS);
            super.setForeground(FG_NOT_GIVEN);
        } else if (status == CellStatus.HINT) {
            super.setText(number + "");
            super.setEditable(false);
            super.setBackground(BG_HINT);
            super.setForeground(FG_HINT);
        } else if (status == CellStatus.CORRECT_GUESS) { 
            super.setBackground(BG_CORRECT_GUESS);
            super.setForeground(FONT_WHITE);
        } else if (status == CellStatus.WRONG_GUESS) { 
            super.setBackground(BG_WRONG_GUESS);
            super.setForeground(FONT_WHITE);
        }
    }

    public void reset() {
        setText(""); 
        status = CellStatus.TO_GUESS; 
        setBackground(Cell.BG_TO_GUESS);
        paint(); 
    }
}