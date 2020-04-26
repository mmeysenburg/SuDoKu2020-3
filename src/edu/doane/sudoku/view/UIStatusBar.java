package edu.doane.sudoku.view;

import javafx.geometry.HPos;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import edu.doane.sudoku.controller.SuDoKuController;

/**
 * Status bar for the Desktop SuDoKu game.
 *
 * @author Mark M. Meysenburg
 * @version 1/10/2020
 */
public class UIStatusBar extends GridPane {
    /**
     * Label displaying the elapsed time.
     */
    Label lblTimer;

    /**
     * Label displaying notes mode status.
     */
    Label lblNotesMode;

    /**
     * Label displaying paused mode status.
     */
    Label lblPausedMode;

    /**
     * Label displaying total hints used.
     */
    Label lblHints;

    /**
     * Total hints used.
     */
    Integer hints;

    /**
     * Reference to the controller used by the app.
     */
    private SuDoKuController controller;

    /**
     * Construct the status bar.
     */
    public UIStatusBar() {
        super();

        hints = 0;

        // 2 columns, equally distributed
        ColumnConstraints col1, col2;
        col1 = new ColumnConstraints();
        col1.setPercentWidth(50);
        col2 = new ColumnConstraints();
        col2.setPercentWidth(50);
        getColumnConstraints().addAll(col1, col2);

        lblTimer = new Label("0:00:00");
        add(lblTimer, 0, 0, 1, 1);
        setHalignment(lblTimer, HPos.CENTER);

        lblHints = new Label("(H)ints used: " + hints);
        add(lblHints, 0, 0, 1, 1);
        setHalignment(lblHints, HPos.RIGHT);

        lblNotesMode = new Label("(N)otes mode: off");
        add(lblNotesMode, 1, 0, 1, 1);
        setHalignment(lblNotesMode, HPos.RIGHT);

        lblPausedMode = new Label("(P)aused mode: off");
        add(lblPausedMode, 1, 0, 1,1);
        setHalignment(lblPausedMode, HPos.CENTER);
    }

    /**
     * Update total hints count.
     */
    public final void incrementHints(Integer hints) { lblHints.setText("(H)ints used:" + hints); }

    /**
     * Toggle notes mode on.
     */
    public final void setNotesMode() {
        lblNotesMode.setText("(N)otes mode: on");
    }

    /**
     * Toggle notes mode off.
     */
    public final void setNormalMode() {
        lblNotesMode.setText("(N)otes mode: off");
    }

    /**
     * Toggle Paused mode on.
     */
    public final void setPausedModeOn() { lblPausedMode.setText("(P)aused mode: on"); }

    /**
     * Toggle Paused mode off.
     */
    public final void setPausedModeOff() {
        lblPausedMode.setText("(P)aused mode: off");
    }

    /**
     * Set the time value displayed on the status bar.
     *
     * @param time Time value to set, in 0:00:00 format.
     */
    public void setTime(String time) {
        lblTimer.setText(time);
    }
}
