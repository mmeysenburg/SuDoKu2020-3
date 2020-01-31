package edu.doane.sudoku.view;

/**
 * Interface for instances of the view (UI) in the Doane SuDoKu MVC architecture.
 *
 * @author Mark M. Meysenburg
 * @version 12/15/2015
 */
public interface SuDoKuUI {

    /**
     * Clear the grid, reset the timer, etc., in preparation for a new game.
     *
     * @param newGame true if the grid is being cleared for a new game, false
     *                otherwise.
     */
    void clearGrid(boolean newGame);

    /**
     * Set the difficulties for games that can be produced by the system.
     *
     * @param difficulties Array of strings with the difficulty names.
     */
    void setDifficulties(String[] difficulties);

    /**
     * Set a given at the specified location.
     *
     * @param row    Row of the given to set.
     * @param col    Column of the given to set.
     * @param number Given value to place in the specified location.
     */
    void setGiven(int row, int col, int number);

    /**
     * Set a number at the specified location.
     *
     * @param row    Row of the number to set.
     * @param col    Column of the number to set.
     * @param number Number value to place in the specified location.
     */
    void setNumber(int row, int col, int number);

    /**
     * Toggle a number at the specified location.
     *
     * @param row    Row of the note to toggle
     * @param col    Column of the note to toggle
     * @param number Note value to toggle
     */
    void toggleNote(int row, int col, int number);

    /**
     * Set the time value to be displayed on the UI.
     *
     * @param value Time value to display, in h:mm:ss format.
     */
    void setTimerValue(String value);

    /**
     * Celebrate a completed game!
     *
     * @param id   ID number of the completed game.
     * @param time String holding the amount of time taken to win.
     */
    void celebrate(int id, String time);

    /**
     * Confirm that the player really wants to exit the game.
     *
     * @return True if the player wants to quit, false otherwise.
     */
    boolean confirmExit();

    /**
     * Confirm that the player really wants to begin a new game. Any game currently
     * in progress is abandoned.
     *
     * @return True if the player wants to begin a new game, false otherwise.
     */
    boolean confirmNewGame();

    /**
     * Display the modal "About Doane SuDoKu" dialog box.
     */
    void displayAbout();
}
