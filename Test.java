import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class Test {
    private JTextField statusBar;

    public Test() {
        JFrame frame = new JFrame("Test Application");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        // Create and add the status bar
        statusBar = createStatusBar();
        frame.add(statusBar, BorderLayout.SOUTH);

        // Create and add the menu bar
        JMenuBar menuBar = createMenuBar();
        frame.setJMenuBar(menuBar);

        frame.setVisible(true);
    }

    private JTextField createStatusBar() {
        JTextField statusBar = new JTextField("Ready");
        statusBar.setEditable(false);
        Font boldFont = statusBar.getFont().deriveFont(Font.BOLD);
        statusBar.setFont(boldFont);
        return statusBar;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Test());
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // Create a File menu
        JMenu fileMenu = new JMenu("File");
        JMenuItem newItem = new JMenuItem("New");
        JMenuItem openItem = new JMenuItem("Open");
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));

        fileMenu.add(newItem);
        fileMenu.add(openItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        // Add the File menu to the menu bar
        menuBar.add(fileMenu);

        // You can add more menus here if needed

        return menuBar;
    }
}
