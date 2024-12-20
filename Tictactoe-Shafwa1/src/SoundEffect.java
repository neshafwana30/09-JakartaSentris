public enum SoundEffect {
    CLICK("NULL"),
    EXPLODE("NULL"),
    DIE("NULL"),
    WIN("NULL");

    public static enum Volume {
        MUTE, LOW, MEDIUM, HIGH
    }

    public static Volume volume = Volume.LOW;

    private String soundFileName;

    private SoundEffect(String soundFileName) {
        this.soundFileName = soundFileName;
    }

    static void initGame() {
        values(); // calls the constructor for all the elements
    }
}