package edu.doane.sudoku.controller;

import edu.doane.sudoku.view.SuDoKuUI;

/**
 * Interface for timers in SuDoKu applications.
 *
 * @author Mark M. Meysenburg
 * @version 12/30/2015
 */
public interface SuDoKuTimer {

    /**
     * Start the timer ticking.
     */
    void startTimer();

    /**
     * Stop the timer. If startTimer() is subsequently called, the timer
     * resumes from its last value.
     */
    void stopTimer();

    /**
     * Reset the timer. Resets the timer to 0:00:00.
     */
    void resetTimer();

    /**
     * Set the view that will reflect this timer.
     *
     * @param view SuDoKuUI view that reflections this timer's value.
     */
    void setView(SuDoKuUI view);

    /**
     * Get a string representation of the timer.
     *
     * @return String representation of the current timer, in
     * hh:mm:ss format.
     */
    String toString();
}
