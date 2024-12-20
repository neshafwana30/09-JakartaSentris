package Sudoku;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioPlayer {
    private static Clip clip;

    public static void playAudio(String filePath) {
        try {
            File audioFile = new File(filePath);

            try (AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile)) {

                clip = AudioSystem.getClip();
                clip.open(audioStream);

                clip.start();

                clip.addLineListener(event -> {
                    if (event.getType() == LineEvent.Type.STOP) {
                        clip.close();
                    }
                });
            }
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public static void stopAudio() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
            clip.close();
        }
    }

    public static void playAudioInBackground(String filePath) {
        new Thread(() -> playAudio(filePath)).start();
    }
}