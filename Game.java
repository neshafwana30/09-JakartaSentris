import java.util.Scanner;

public class Game {
    private final Board board;

    public Game(Board board) {
        this.board = board;
    }

    public void startGame() {
        System.out.println("Selamat Datang di Permainan Sudoku!");

        while (!board.isFull()) {
            board.printBoard();
            getUserInput();
        }

        System.out.println("Permainan selesai! Anda telah mengisi semua sel.");
    }

    private void getUserInput() {
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("Masukkan baris (0-8): ");
        int row = scanner.nextInt();
        
        System.out.print("Masukkan kolom (0-8): ");
        int col = scanner.nextInt();
        
        System.out.print("Masukkan nilai (1-9): ");
        int value = scanner.nextInt();

        if (isValidMove(row, col, value)) {
            board.setCell(row, col, value);
            System.out.println("Nilai berhasil dimasukkan.");
        } else {
            System.out.println("Gerakan tidak valid! Coba lagi.");
        }
    }

    private boolean isValidMove(int row, int col, int value) {
        return board.getCell(row, col) == 0 && value >= 1 && value <= 9;
    }
}
