package ch.fhnw.ip12.gui.manager;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;

/**
 * Is used to play different sounds
 */
public enum SoundManager {
  COUNTDOWN("/sounds/countdown"), GAME_WON("/sounds/gameWon"), NO_ANSWER_SELECTED("/sounds/noAnswerSelected"),
  RIGHT_ANSWER("/sounds/rightAnswer"), SELECT_BUTTON("/sounds/selectButton"), WRONG_ANSWER("/sounds/wrongAnswer"),
  GAME_LOST("/sounds/gameLost"), BACKGROUND_MUSIC("/sounds/backgroundMusic", true, 0.1f);

  private static final boolean PLAY_JAVAX = false; // whether to play the sound using javax or javafx library
  private String path;
  private boolean loop = false;
  private float volume = 1.0f;

  /**
   * constructs a new instance of the soundmanager
   * 
   * @param path   the path to the soundfile
   * @param loop   if it should loop the sound
   * @param volume the volume of the played sounds (from 0.1 to 1.0)
   */
  private SoundManager(String path, boolean loop, float volume) {
    this.path = path;
    this.loop = loop;
    this.volume = volume;
  }

  private SoundManager(String path) {
    this.path = path;
  }

  public String getPath() {
    return path;
  }

  /**
   * Plays the sound
   */
  public void getMusic() {
    if (PLAY_JAVAX) {
      playJavaX();
    } else {
      playJavaFX();
    }
  }

  /**
   * use javax to play the sound
   */
  private void playJavaX() {
    System.out.println("SoundManager.playSound()");
    try {
      Line.Info linfo = new Line.Info(Clip.class);
      Clip clip = (Clip) AudioSystem.getLine(linfo);
      clip.open(AudioSystem.getAudioInputStream(new File("src/main/resources" + path + ".wav")));
      if (loop) {
        clip.loop(Clip.LOOP_CONTINUOUSLY);
      }
      if (clip.isRunning()) {
        clip.stop();
      }
      clip.setFramePosition(0);
      clip.start();
      System.out.println("start sound");
    } catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
      System.out.println(e.getMessage());
    }
  }

  /**
   * use javafx to play the sound
   */
  private void playJavaFX() {
    try {
      Media hit = new Media(getClass().getResource(this.path + ".mp3").toURI().toString());
      AudioClip audio = new AudioClip(hit.getSource());
      audio.setVolume(volume);
      if (loop) {
        audio.setCycleCount(AudioClip.INDEFINITE);
      }
      audio.play();
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
  }

}