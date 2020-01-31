package edu.doane.sudoku.view;

import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * JavaFX Pane representing a single cell in the desktop SuDoKu game.
 *
 * @author Mark M. Meysenburg
 * @version 1/10/2020
 */
public class UICell extends BorderPane implements EventHandler<MouseEvent> {

    /**
     * Alias for background color of cell.
     */
    private Background background;

    /**
     * Background fill for the cell in its normal state.
     */
    private static Background normalBackground = new Background(
            new BackgroundFill(Color.LIGHTGRAY, null, null));

    /**
     * Background fill for the cell when it's selected for number entry.
     */
    private static Background selectedBackground = new Background(
            new BackgroundFill(Color.LAVENDERBLUSH, null, null));

    /**
     * Background fill for the cell when it's selected for notes entry.
     */
    private static Background notesBackground = new Background(
            new BackgroundFill(Color.LIGHTPINK, null, null));

    /**
     * Array holding notes status for numbers 1 through 9.
     */
    private boolean[] notes;

    /**
     * String holding number displayed in the cell
     */
    private String num;

    /**
     * Flag indicating notes mode or normal number mode.
     */
    private boolean notesMode;

    /**
     * Flag indicating if this cell holds a given or not.
     */
    private boolean isGiven;

    /**
     * True if the mouse pointer is over this cell and we're not in notes mode.
     */
    private boolean isSelected;

    /**
     * Label showing the number held in the cell.
     */
    private Label lblNumber;

    /**
     * Labels holding notes.
     */
    private Label[] lblNotes;

    /**
     * Set up an empty, no-notes, un-selected cell.
     */
    public UICell() {
        super();

        // create and initialize the notes array; location 0 is not used
        notes = new boolean[10];

        // create and initialize the notes labels
        lblNotes = new Label[10];
        for (int i = 0; i < lblNotes.length; i++) {
            lblNotes[i] = new Label(Integer.toString(i));
            lblNotes[i].setFont(Font.font("Arial", 12));
            lblNotes[i].setTextFill(Color.RED);
            lblNotes[i].setVisible(false);
        }
        GridPane left = new GridPane();
        RowConstraints row1, row2, row3;
        row1 = new RowConstraints(); row1.setPercentHeight(33.33);
        row2 = new RowConstraints(); row2.setPercentHeight(33.33);
        row3 = new RowConstraints(); row3.setPercentHeight(33.33);
        left.getRowConstraints().addAll(row1, row2, row3);
        left.add(lblNotes[3], 0, 0);
        left.add(lblNotes[2], 0, 1);
        left.add(lblNotes[1], 0, 2);

        GridPane top = new GridPane();
        ColumnConstraints col1, col2, col3, col4, col5;
        col1 = new ColumnConstraints(); col1.setPercentWidth(20);
        col2 = new ColumnConstraints(); col2.setPercentWidth(20);
        col3 = new ColumnConstraints(); col3.setPercentWidth(20);
        col4 = new ColumnConstraints(); col4.setPercentWidth(20);
        col5 = new ColumnConstraints(); col5.setPercentWidth(20);
        top.getColumnConstraints().addAll(col1, col2, col3);
        top.add(lblNotes[4], 1, 0);
        top.add(lblNotes[5], 2, 0);
        top.add(lblNotes[6], 3, 0);

        GridPane right = new GridPane();
        right.getRowConstraints().addAll(row1, row2, row3);
        right.add(lblNotes[7], 0, 0);
        right.add(lblNotes[8], 0, 1);
        right.add(lblNotes[9], 0, 2);

        setLeft(left);
        setTop(top);
        setRight(right);

        // set initial background and number value
        background = normalBackground;
        setBackground(background);

        num = "0";
        lblNumber = new Label(num);
        lblNumber.setFont(Font.font("ArialBold", 20));
        lblNumber.setTextFill(Color.BLUE);
        setCenter(lblNumber);

        // set initial flags for our modes
        notesMode = false;
        isSelected = false;
        isGiven = false;

        // the class handles its own mouse entry and exit events, to set the
        // background fill color and selectedness of the cell
        setOnMouseEntered(this);
        setOnMouseExited(this);
    }

    /**
     * Turn on notes-entry mode.
     */
    public void setNotesMode() {
        notesMode = true;
    }

    /**
     * Turn off notes-entering mode.
     */
    public void setNormalMode() {
        notesMode = false;
    }

    /**
     * Is this cell selected? I.e., is the mouse pointer over the cell?
     *
     * @return True if the cell is selected, false otherwise.
     */
    public boolean isSelected() {
        return isSelected;
    }

    /**
     * Set the number to display in this cell. Do nothing if the cell is a
     * given.
     *
     * @param number Number in [0, 9] to place in the cell. 0 means erase
     * the current number, [1, 9] means place value in cell.
     */
    public void setNumber(char number) {
        if (!isGiven) {
            if(number == '0') {
                num = "";
            } else {
                num = Character.toString(number);
            }
            lblNumber.setText(num);
        }
    }

    /**
     * Toggle the note status for a number in the cell.
     *
     * @param number Note to set/unset in the cell.
     */
    public void toggleNote(char number) {
        int i = Integer.parseInt(Character.toString(number));
        notes[i] = !notes[i];
        lblNotes[i].setVisible(notes[i]);
    }

    /**
     * Place a number in the cell as a given.
     *
     * @param number Given to place in the cell, as a char.
     */
    public void setGiven(char number) {
        isGiven = true;
        num = Character.toString(number);
        lblNumber.setTextFill(Color.BLACK);
        lblNumber.setText(num);
    }

    /**
     * Remove the "given" status of the cell.
     */
    public void unsetGiven() {
        isGiven = false;
        lblNumber.setTextFill(Color.BLUE);
    }

    /**
     * Remove all notes from the cell.
     */
    public void clearAllNotes() {
        for(int i = 0; i < notes.length; i++) {
            notes[i] = false;
            lblNotes[i].setVisible(false);
        }
    }

    /**
     * Remove the number from a cell.
     */
    public void clearNumber() {
        num = "";
        lblNumber.setText(num);
    }

    @Override
    public void handle(MouseEvent event) {
        if(event.getEventType().equals(MouseEvent.MOUSE_ENTERED)) {
            isSelected = true;
            if(notesMode) {
                setBackground(notesBackground);
            } else {
                setBackground(selectedBackground);
            }
        } else if(event.getEventType().equals(MouseEvent.MOUSE_EXITED)) {
            isSelected = false;
            setBackground(normalBackground);
        }
    }
}
