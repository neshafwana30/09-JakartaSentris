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

    // Method untuk memutar audio
    public static void playAudio(String filePath) {
        try {
            // Lokasi file audio
            File audioFile = new File(filePath);

            // Membuat AudioInputStream
            try (AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile)) {

                // Membuat Clip untuk memainkan suara
                clip = AudioSystem.getClip();
                clip.open(audioStream);

                // Memulai pemutaran audio
                clip.start();

                // Tunggu hingga pemutaran selesai
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
}