package edu.doane.sudoku.controller;

import edu.doane.sudoku.model.ElapsedTime;
import edu.doane.sudoku.view.SuDoKuUI;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

/**
 * Timer for the desktop SuDoKu app. Uses the JavaFX Timeline class for keeping time.
 *
 * @author Mark M. Meysenburg
 * @version 1/10/2020
 */
public class DesktopTimer implements SuDoKuTimer {
    /**
     * JavaFX Timeline timer used by this class.
     */
    private Timeline timeline;

    /**
     * Elapsed time object to keep track of the amount of time elapsed.
     */
    private ElapsedTime elapsedTime;

    /**
     * SuDoKuUI view reflecting this timer's value.
     */
    private SuDoKuUI view;

    /**
     * Create a new DesktopTimer object, starting at 0:00:00, and ready to count up
     * by 1 second at a time.
     */
    public DesktopTimer() {
        elapsedTime = new ElapsedTime();

        timeline = new Timeline(new KeyFrame(Duration.millis(100), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                elapsedTime.tick();
                view.setTimerValue(elapsedTime.toString());
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
    }

    @Override
    public void startTimer() {
        timeline.play();
    }

    @Override
    public void stopTimer() {
        timeline.stop();
    }

    @Override
    public void resetTimer() {
        elapsedTime.reset();
    }

    @Override
    public void setView(SuDoKuUI view) {
        this.view = view;
    }

    @Override
    public String toString() {
        return elapsedTime.toString();
    }
}