package edu.doane.sudoku.controller;

/**
 * Interface for the controller in the SuDoKu MVC architecture.
 *
 * @author Mark M. Meysenburg
 * @version 12/15/2015
 */
public interface SuDoKuController {

    /**
     * Play a number at the specified location.
     *
     * @param row    Row to place the number in.
     * @param col    Column to place the number in.
     * @param number Number to place in the specified location.
     */
    void playNumber(int row, int col, int number);

    /**
     * Remove the number from the specified location. If the number there is a
     * given, or there is no number yet, do nothing.
     *
     * @param row Row to place the number in.
     * @param col Column to place the number in.
     */
    void removeNumber(int row, int col);

    /**
     * Set / unset a note at the specified location.
     *
     * @param row    Row to set the note in.
     * @param col    Column to set the note in.
     * @param number Note to toggle in the specified location.
     */
    void setNote(int row, int col, int number);

    /**
     * Request a game of a specified difficulty.
     *
     * @param difficulty String specifying the difficulty of game to fetch.
     */
    void requestGame(String difficulty);

    /**
     * Shut the game down in an orderly fashion.
     */
    void shutDown();

    /**
     * Cause the view to display the "About Doane SuDoKu" modal dialog.
     */
    void displayAbout();

    /**
     * Remove everything from the view grid.
     */
    void clearViewGrid();

    /**
     * Remove all non-given numbers, and all notes, from both the
     * model and view grids.
     */
    void resetGrids();
}
