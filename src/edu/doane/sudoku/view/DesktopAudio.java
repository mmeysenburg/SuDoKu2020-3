package edu.doane.sudoku.view;

import javafx.scene.control.Alert;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.net.URISyntaxException;

/**
 * Singleton class providing simple audio for the desktop SuDoKu application.
 *
 * @author Mark M. Meysenburg
 * @version 01/10/2020
 */
public class DesktopAudio {

    /**
     * Reference to the one instance of the audio player that's allowed to exist.
     */
    private static DesktopAudio instance = null;

    /**
     * Media for celebrating a win.
     */
    private MediaPlayer celebrateMedia;

    /**
     * Media for clearing the grid.
     */
    private MediaPlayer clearGridMedia;

    /**
     * Media for erasing a number.
     */
    private MediaPlayer eraseNumberMedia;

    /**
     * Media for starting a new game.
     */
    private MediaPlayer newGameMedia;

    /**
     * Media for toggling a note.
     */
    private MediaPlayer noteToggleMedia;

    /**
     * Media for playing a number.
     */
    private MediaPlayer playNumberMedia;

    /**
     * Private default constructor prevents users from instantiating.
     */
    private DesktopAudio() {
        try {
            // celebration media
            Media media1 = new Media(ClassLoader.getSystemResource("resources/audio/celebrate.wav").toURI().toString());
            celebrateMedia = new MediaPlayer(media1);
            celebrateMedia.setAutoPlay(false);

            // clear grid media
            Media media2 = new Media(ClassLoader.getSystemResource("resources/audio/clear-grid.wav").toURI().toString());
            clearGridMedia = new MediaPlayer(media2);
            clearGridMedia.setAutoPlay(false);

            // erase number media
            Media media3 = new Media(ClassLoader.getSystemResource("resources/audio/erase-number.wav").toURI().toString());
            eraseNumberMedia = new MediaPlayer(media3);
            eraseNumberMedia.setAutoPlay(false);

            // new game media
            Media media4 = new Media(ClassLoader.getSystemResource("resources/audio/new-game.wav").toURI().toString());
            newGameMedia = new MediaPlayer(media4);
            newGameMedia.setAutoPlay(false);

            // note toggle media
            Media media5 = new Media(ClassLoader.getSystemResource("resources/audio/note-toggle.wav").toURI().toString());
            noteToggleMedia = new MediaPlayer(media5);
            noteToggleMedia.setAutoPlay(false);

            // play number media
            Media media6 = new Media(ClassLoader.getSystemResource("resources/audio/play-number.wav").toURI().toString());
            playNumberMedia = new MediaPlayer(media6);
            playNumberMedia.setAutoPlay(false);

        } catch (Exception exception) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Media Error");
            alert.setHeaderText(null);
            alert.setContentText("Unable to load audio media.");
        }
    }

    /**
     * Get the instance to the DesktopAudio object.
     *
     * @return The instance of the DesktopAudio object.
     */
    public static DesktopAudio getInstance() {
        if(instance == null) {
            instance = new DesktopAudio();
        }

        return instance;
    }

    /**
     * Play the win game sound.
     */
    public void playCelebrate() {
        if(playNumberMedia != null) {
            playNumberMedia.stop();
            playNumberMedia.setStartTime(Duration.seconds(0));
            playNumberMedia.play();
        }
    }

    /**
     * Play the clear grid sound.
     */
    public void playClearGrid() {
        if(clearGridMedia != null) {
            clearGridMedia.stop();
            clearGridMedia.setStartTime(Duration.seconds(0));
            clearGridMedia.play();
        }
    }

    /**
     * Play the erase number sound.
     */
    public void playEraseNumber() {
        if(eraseNumberMedia != null) {
            eraseNumberMedia.stop();
            eraseNumberMedia.setStartTime(Duration.seconds(0));
            eraseNumberMedia.play();
        }
    }

    /**
     * Play the new game sound.
     */
    public void playNewGame() {
        if(newGameMedia != null) {
            newGameMedia.stop();
            newGameMedia.setStartTime(Duration.seconds(0));
            newGameMedia.play();
        }
    }

    /**
     * Play the note toggling sound.
     */
    public void playNoteToggle() {
        if(noteToggleMedia != null) {
            noteToggleMedia.stop();
            noteToggleMedia.setStartTime(Duration.seconds(0));
            noteToggleMedia.play();
        }
    }

    /**
     * Play the play number sound.
     */
    public void playPlayNumber() {
        if(playNumberMedia != null) {
            playNumberMedia.stop();
            playNumberMedia.setStartTime(Duration.seconds(0));
            playNumberMedia.play();
        }
    }
}
