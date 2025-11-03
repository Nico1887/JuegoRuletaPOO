package logic;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Manages loading and playback of all game audio resources (.WAV format).
 */
public class SoundManager {

    private static Clip ambientClip;
    private static Clip spinClip;

    private static Clip loadClip(String resourcePath) throws Exception {
        InputStream audioStream = SoundManager.class.getResourceAsStream(resourcePath);
        if (audioStream == null) {
            throw new IOException("Audio resource not found at " + resourcePath);
        }

        Clip clip = AudioSystem.getClip();
        clip.open(AudioSystem.getAudioInputStream(audioStream));
        return clip;
    }

    // --- Control Methods ---

    public static void playAmbientLoop() {
        try {
            if (ambientClip == null) {
                ambientClip = loadClip("/resources/audio/ambient_casino.wav");
            }
            if (ambientClip.isRunning()) {
                ambientClip.stop();
            }
            ambientClip.setFramePosition(0);
            ambientClip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception e) {
            System.err.println("Error playing ambient loop.");
            e.printStackTrace();
        }
    }

    public static void playSpinStart() {
        try {
            if (spinClip == null) {
                spinClip = loadClip("/resources/audio/spin_loop.wav");
            }
            spinClip.setFramePosition(0);
            spinClip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception e) {
            System.err.println("Error playing spin start sound.");
            e.printStackTrace();
        }
    }

    public static void stopSpin() {
        if (spinClip != null && spinClip.isRunning()) {
            spinClip.stop();
        }
    }

    public static void playWin() {
        // Plays once, doesn't need to be saved globally
        try {
            Clip winClip = loadClip("/resources/audio/win_jingle.wav");
            winClip.start();
        } catch (Exception e) {
            System.err.println("Error playing win sound.");
        }
    }

    public static void playLose() {
        // Assuming the 'lose' sound is the error/generic sound
        try {
            Clip loseClip = loadClip("/resources/audio/lose_buzz.wav");
            loseClip.start();
        } catch (Exception e) {
            System.err.println("Error playing lose sound.");
        }
    }
}