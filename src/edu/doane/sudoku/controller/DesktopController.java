package edu.doane.sudoku.controller;

import edu.doane.sudoku.model.Game;
import edu.doane.sudoku.model.GameGrid;
import edu.doane.sudoku.persistence.Persistence;
import edu.doane.sudoku.view.SuDoKuUI;

/**
 * Implementation of the controller interface for the desktop app.
 *
 * @author Mark M. Meysenburg
 * @version 12/28/2015
 */
public class DesktopController implements SuDoKuController {

    /**
     * Reference to the view being controlled by this controller.
     */
    private SuDoKuUI view;

    /**
     * Reference to the SuDoKuTimer used by the app.
     */
    private SuDoKuTimer timer;

    /**
     * Game currently being played.
     */
    private Game game;

    /**
     * GameGrid currently being played.
     */
    private GameGrid grid;

    /**
     * Flag indicating whether we've celebrated a win for this game
     * or not.
     */
    private boolean celebrated;

    /**
     * Construct a new instance of this controller.
     *
     * @param view  SuDoKuUI view to be controlled.
     * @param timer SuDoKuTimer object to keep track of game time
     */
    public DesktopController(SuDoKuUI view, SuDoKuTimer timer) {
        // "wire up" the MVC references
        this.view = view;
        this.timer = timer;
        timer.setView(view);

        // when constructed, i.e., on app start, load the next game we 
        // have
        setNextGame();

        // ... and start the clock!
        timer.startTimer();

        // we haven't won before we play any numbers!
        celebrated = false;
    }

    @Override
    public void playNumber(int row, int col, int number) {
        // if the requested number isn't a given...
        if (!grid.isGiven(row, col)) {
            // get any existing number
            int n = grid.getNumber(row, col);
            // if there was a number there, 
            if (n != 0) {
                // unset then set the number
                grid.unsetNumber(row, col);
                grid.setNumber(row, col, number);
                view.setNumber(row, col, number);
                // did we win yet?
                didWin();
            } else {
                // no number there, so just set and check for win
                grid.setNumber(row, col, number);
                view.setNumber(row, col, number);
                didWin();
            } // if n != 0
        } // if not given
    }

    @Override
    public void removeNumber(int row, int col) {
        // only remove a number from the cell if it isn't a given
        if (!grid.isGiven(row, col)) {
            grid.unsetNumber(row, col);
            view.setNumber(row, col, 0);
        }
    }

    /**
     * Determine if the player won the game. If the player won, do some
     * celebration; otherwise, do nothing.
     */
    private void didWin() {
        // we win if the grid is complete, valid, and we haven't
        // celebrated yet
        if (grid.isComplete() && grid.validate() && !celebrated) {
            // celebrate! and stop the timer
            celebrated = true;
            timer.stopTimer();
            view.celebrate(game.getID(), timer.toString());
        }
    }

    @Override
    public void requestGame(String difficulty) {
        // pause timer
        timer.stopTimer();

        // confirm new game desire
        if (view.confirmNewGame()) {
            // move on to next game, reset celbration flag and timer
            setNextGame();
            celebrated = false;
            timer.resetTimer();
        }
        // start timer again
        timer.startTimer();
    }

    private void setNextGame() {
        // get the next game from our local store
        Persistence db = Persistence.getInstance();
        game = db.getNextGame();

        // set the initial grid in the model
        grid = game.getInitial();

        // get rid of everything on the view grid
        view.clearGrid(true);

        // put givens for new game into view
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (grid.isGiven(i, j)) {
                    view.setGiven(i, j, grid.getNumber(i, j));
                } // if given
            } // for j
        } // for i

    }

    @Override
    public void setNote(int row, int col, int number) {
        // fetch the notes from the current model cell
        boolean[] notes = grid.getNotes(row, col);

        // toggle the note in the model
        if (notes[number]) {
            grid.removeNote(row, col, number);

        } else {
            grid.setNote(row, col, number);
        }

        // toggle the note in the view
        view.toggleNote(row, col, number);
    }

    @Override
    public void shutDown() {
        // pause timer
        timer.stopTimer();

        // really exit?
        if (view.confirmExit()) {
            // if so, shut down
            System.exit(0);
        }

        // if not, restart timer (if we were playing)
        if (!celebrated) {
            timer.startTimer();
        }
    }

    @Override
    public void displayAbout() {
        // stop timer
        timer.stopTimer();

        // show about box
        view.displayAbout();

        // restart timer after box is closed (if we are still
        // playing)
        if (!celebrated) {
            timer.startTimer();
        }
    }

    @Override
    public void clearViewGrid() {
        view.clearGrid(false);
    }

    @Override
    public void resetGrids() {
        // first zap everything on the view
        view.clearGrid(false);

        // then remove non-given numbers
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (!grid.isGiven(row, col)) {
                    grid.unsetNumber(row, col);
                }
            }
        }

        // finally, display the givens on the view
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (grid.isGiven(i, j)) {
                    view.setGiven(i, j, grid.getNumber(i, j));
                }
            }
        }
    }

}
