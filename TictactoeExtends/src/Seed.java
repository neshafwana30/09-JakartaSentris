import java.awt.Image;
import java.io.File;

import javax.swing.ImageIcon;

/**
 * This enum is used by:
 * 1. Player: takes value of CROSS or NOUGHT
 * 2. Cell content: takes value of CROSS, NOUGHT, or NO_SEED.
 *
 * We also attach a display image icon (text or image) for the items.
 * and define the related variable/constructor/getter.
 * To draw the image:
 * g.drawImage(content.getImage(), x, y, width, height, null);
 *
 * Ideally, we should define two enums with inheritance, which is,
 * however, not supported.
 */
public enum Seed { // to save as "Seed.java"
    CROSS("X", "TictactoeExtends\\src\\Image\\Cross.gif"), // displayName, imageFilename
    NOUGHT("O", "TictactoeExtends\\src\\Image\\Not.gif"),
    NO_SEED(" ", "TictactoeExtends\\src\\Image\\Not.gif");

    // Private variables
    private String displayName;
    private Image img = null;

    // Constructor (must be private)
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

    // Public getters
    public String getDisplayName() {
        return displayName;
    }

    public Image getImage() {
        return img;
    }

    public static void main(String[] args) {
        // Example of accessing the CROSS seed
        Seed crossSeed = Seed.CROSS;
        System.out.println("Display Name: " + crossSeed.getDisplayName());
        Image crossImage = crossSeed.getImage();

        // Example of accessing the NOUGHT seed
        Seed noughtSeed = Seed.NOUGHT;
        System.out.println("Display Name: " + noughtSeed.getDisplayName());
        Image noughtImage = noughtSeed.getImage();

        // Example of accessing the NO_SEED
        Seed noSeed = Seed.NO_SEED;
        System.out.println("Display Name: " + noSeed.getDisplayName());
        Image noSeedImage = noSeed.getImage();
    }
}
