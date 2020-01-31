package edu.doane.sudoku.model;

/**
 * Class representing the time elapsed in a SuDoKu game.
 *
 * @author Mark M. Meysenburg
 * @version 12/16/2015
 */
public class ElapsedTime {

    /**
     * Number of seconds, in [0, 59].
     */
    private int seconds;

    /**
     * Number of minutes in [0, 59].
     */
    private int minutes;

    /**
     * Number of hours, starting at 0.
     */
    private int hours;

    /**
     * Create a new ElapsedTime object, set to 0:00:00.
     */
    public ElapsedTime() {
        reset();
    }

    /**
     * Reset the time to 0:00:00.
     */
    public final void reset() {
        seconds = 0;
        minutes = 0;
        hours = 0;
    }

    /**
     * Increment the time by one second. If necessary, roll over the
     * minutes and hours values.
     */
    public void tick() {
        seconds++;
        if (seconds == 60) {
            minutes++;
            seconds = 0;
            if (minutes == 60) {
                hours++;
                minutes = 0;
            }
        }
    }

    @Override
    /**
     * Get a string representation of the time, in the format "h:mm:ss".
     */
    public String toString() {
        return String.format("%d:%02d:%02d", hours, minutes, seconds);
    }
}
