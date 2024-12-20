import java.awt.Image;
import java.io.File;
import javax.swing.ImageIcon;

public enum Seed {
    CROSS("X", "TictactoeExtends\\src\\Image\\cross.png"),
    NOUGHT("O", "TictactoeExtends\\src\\Image\\n" + //
            "ought.png"),
    NO_SEED(" ", "TictactoeExtends\\src\\Image\\nought.png");

    private String displayName;
    private Image img = null;

    private Seed(String name, String imageFilename) {
        this.displayName = name;
        if (imageFilename != null) {
            File imgFile = new File(imageFilename);
            ImageIcon icon = null;
            if (imgFile.exists()) {
                icon = new ImageIcon(imageFilename);
            } else {
                System.err.println("Couldn't find file " + imageFilename);
            }
            img = icon.getImage();
        }
    }

    public String getDisplayName() {
        return displayName;
    }

    public Image getImage() {
        return img;
    }
}