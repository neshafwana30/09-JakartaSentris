import Sudoku.AudioPlayer;

public enum SoundEffect {
    CLICK("TictactoeExtends\\src\\audio\\click.wav"),
    EXPLODE("TictactoeExtends\\src\\audio\\seriii.wav"),
    DIE("TictactoeExtends\\src\\audio\\kalah.wav"),
    WIN("TictactoeExtends\\src\\audio\\win.wav");

    public static enum Volume {
        MUTE, LOW, MEDIUM, HIGH
    }

    public static Volume volume = Volume.LOW;

    private String soundFileName;

    private SoundEffect(String soundFileName) {
        this.soundFileName = soundFileName;
    }

    public void play() {
        if (volume != Volume.MUTE) {
            AudioPlayer.playAudio(soundFileName);
        }
    }

    static void initGame() {
        values(); 
    }
}