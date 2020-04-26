package edu.doane.sudoku.view;

import edu.doane.sudoku.controller.SuDoKuController;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;

/**
 * Class to handle keyboard input in the desktop SuDoKu game.
 *
 * @author Mark M. Meysenburg
 * @version 1/11/2020
 */
public class UIKeyHandler implements EventHandler<KeyEvent> {

    /**
     * Flag telling if we're in note entry mode or not.
     */
    private boolean notesMode;

    /**
     * Flag telling if the game is paused or not.
     */
    private static boolean pausedMode;

    /**
     * Reference to the cells in the user interface.
     */
    private final UICell[][] cells;

    /**
     * Reference to the status bar.
     */
    private final UIStatusBar pnlStatusBar;

    /**
     * Reference to the controller used by the app.
     */
    private SuDoKuController controller;

    /**
     * Construct the key handler.
     *
     * @param cells User interface cells
     * @param controller Reference to the application's controller
     * @param pnlStatusBar User interface status bar
     */
    public UIKeyHandler(UICell[][] cells, SuDoKuController controller, UIStatusBar pnlStatusBar) {
        this.cells = cells;
        this.controller = controller;
        this.pnlStatusBar = pnlStatusBar;

        notesMode = false;
        pausedMode = false;
    }

    public static void setPausedModeOff() {
        pausedMode = false;
    }

    @Override
    public void handle(KeyEvent event) {
        // get the character typed
        char c = event.getText().charAt(0);

        if (!controller.isGameOver()) {

            // and handle the input
            switch (c) {

                // n toggles notes mode
                case 'n':
                case 'N':
                    if (!pausedMode) {
                        notesMode = !notesMode;
                        setNotesOrNormal();
                    }

                    break;

                // p toggles paused mode
                case 'p':
                case 'P':
                    pausedMode = !pausedMode;
                    setPausedMode();

                    break;

                // h toggles giving hint
                case 'h':
                case 'H':
                    if (!pausedMode) {
                        controller.getHint();
                        pnlStatusBar.incrementHints(controller.getTotalHints());
                    }
                    break;

                // 1 - 9 sets number or note
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                    if (notesMode && !pausedMode) {
                        setNote(c);
                    } else if (!pausedMode) {
                        setNumber(c);
                    }
                    break;

                // space clears number from cell
                case ' ':
                    if (!notesMode && !pausedMode) {
                        setNumber(' ');
                    }
                    break;
            }
        }
    }

    /**
     * Cause a note to be set in the selected cell.
     *
     * @param c Character holding number value to set.
     */
    private void setNote(char c) {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (cells[row][col].isSelected()) {
                    // ask the controller to set the note
                    controller.setNote(row, col, Integer.parseInt(Character.toString(c)));

                    // get us out of the loop once the note has been set
                    break;
                }
            }
        }
    }

    /**
     * Toggle between notes and normal mode
     */
    private void setNotesOrNormal() {
        // update cells so they can display the correct image
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (notesMode) {
                    cells[row][col].setNotesMode();
                } else {
                    cells[row][col].setNormalMode();
                }
            }
        }
        // update status bar so it indicates notes mode status
        if (notesMode) {
            pnlStatusBar.setNotesMode();
        } else {
            pnlStatusBar.setNormalMode();
        }
    }

    /**
     * Toggle between paused and normal mode
     */
    public void setPausedMode() {

        // update status bar to indicate paused mode status
        if (pausedMode) {
            controller.pauseGame();
            pnlStatusBar.setPausedModeOn();
        } else {
            controller.resumeGame();
            pnlStatusBar.setPausedModeOff();
        }
    }

    /**
     * Cause a number to be set in the selected cell.
     *
     * @param c Character holding the number value to set.
     */
    private void setNumber(char c) {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (cells[row][col].isSelected()) {
                    if(c == ' ') {
                        // ask the controller to remove the number
                        controller.removeNumber(row, col);
                    } else {
                        // ask the controller to set the number
                        controller.playNumber(row, col, Integer.parseInt(Character.toString(c)));
                    }

                    // get us out of the loop once the number has been set
                    break;
                }
            }
        }
    }
}
