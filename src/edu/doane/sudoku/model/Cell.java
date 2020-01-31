package edu.doane.sudoku.model;

/**
 * Cell: class representing a single cell out of 81 in a SuDoKu grid. It
 * contains a number in [0, 9]. 0 means no number, 1 - 9 represents the
 * number in the cell. Numbers can be "given," or not. The cell knows
 * constraints -- what numbers can't be in it, based on the others in the
 * grid. The cell can also hold notes, booleans for each of the numbers
 * [1, 9].
 *
 * @author Mark M. Meysenburg
 * @version 05/24/2014
 */
public final class Cell {
    /**
     * Constraints placed on this cell by others in the grid. The value
     * at index i represents the number of constraints on the cell for that
     * number. If constraints[i] == 0, then i is a valid number to place
     * in the cell; if constraints[i] != 0, then i is not a valid number
     * for the cell.
     */
    private final int[] constraints;
    /**
     * Notes added to this cell by a player. If notes[i] is true, i is
     * set as a note for this cell; if notes[i] is false, i is not set as a
     * note for the cell.
     */
    private final boolean[] notes;
    /**
     * Number contained by the cell, in [0, 9]. 0 means empty.
     */
    private int number;
    /**
     * Does this cell contain a "given" number (i.e., one provided
     * at the start of the game)?
     */
    private boolean isGiven;

    /**
     * Default constructor for Cell. Create a cell with no number, no
     * constraints, no notes, and not a given.
     */
    public Cell() {
        number = 0;
        isGiven = false;
        constraints = new int[10];    // note array element 0 is unused
        notes = new boolean[10];    // ditto
    }

    /**
     * Initializing constructor for Cell. Create a cell with the specified
     * number, set as a given; no notes or constraints.
     *
     * @param number Number in [1, 9].
     */
    public Cell(int number) throws IllegalArgumentException {
        if (number < 1 || number > 9) {
            throw new IllegalArgumentException("Illegal number in "
                    + "Cell(number) constructor: " + number);
        }

        isGiven = true;
        constraints = new int[10];    // note array element 0 is unused
        notes = new boolean[10];    // ditto
        setNumber(number);
    }

    /**
     * Get the number for this cell.
     *
     * @return the number, in [0, 9]. 0 means empty.
     */
    public int getNumber() {
        return number;
    }

    /**
     * Set the number for this cell.
     *
     * @param number the number to set, in [1, 9]. To clear the cell,
     *               call unsetNumber().
     * @throws IllegalArgumentException if the number is not in [1, 9].
     */
    public final void setNumber(int number) throws IllegalArgumentException {
        if (number < 1 || number > 9) {
            throw new IllegalArgumentException("Illegal number in "
                    + "Cell.setNumber(): " + number);
        }

        this.number = number;
    }

    /**
     * Was this cell a given in the game, or was it an initial blank?
     *
     * @return true if the cell was a given, false otherwise.
     */
    public boolean isGiven() {
        return isGiven;
    }

    /**
     * Set the given status of this cell. True means the cell was a given
     * at the beginning of a game (i.e., had a number), false means it was
     * blank.
     *
     * @param isGiven true to mark the cell as a given, false to make it
     *                non-given.
     */
    public void setIsGiven(boolean isGiven) {
        this.isGiven = isGiven;
    }

    /**
     * Unset the number for this cell. Only unsets if the number in the cell
     * is not a given.
     */
    public void unsetNumber() {
        if (!isGiven())
            number = 0;
    }

    /**
     * Is the specified number a constraint on this cell?
     *
     * @param number Number in [1, 9] to check for.
     * @return true if the number is a constraint on this cell (it could not
     * legally be set here by the player), or false if it is not a constraint.
     * @throws IllegalArgumentException if the number is not in the range
     *                                  [1, 9].
     */
    public boolean isConstraint(int number) throws IllegalArgumentException {
        if (number < 1 || number > 9) {
            throw new IllegalArgumentException("Illegal number in "
                    + "Cell.isConstraint(): " + number);
        }

        return constraints[number] != 0;
    }

    /**
     * Add the specified number as a constraint to this cell.
     *
     * @param number Number in [1, 9] to add as a constraint to this cell.
     * @throws IllegalArgumentException if the number is not in the range
     *                                  [1, 9]
     */
    public void addConstraint(int number) throws IllegalArgumentException {
        if (number < 1 || number > 9) {
            throw new IllegalArgumentException("Illegal number in "
                    + "Cell.addConstraint(): " + number);
        }

        constraints[number]++;
    }

    /**
     * Remove the specified number as a constraint from this cell. This
     * may not remove all the constraints for the number on this cell, but
     * it decreases the number of constraints for the number by one.
     *
     * @param number Number in [1, 9] to remove as a constraint from this
     *               cell.
     * @throws IllegalArgumentException if the number is not n the range
     *                                  [1, 9].
     */
    public void removeConstraint(int number) throws IllegalArgumentException {
        if (number < 1 || number > 9) {
            throw new IllegalArgumentException("Illegal number in "
                    + "Cell.removeConstraint(): " + number);
        }

        if (constraints[number] > 0)
            constraints[number]--;

    }

    /**
     * If the cell knows what number it should contain, based on constraints,
     * return that number.
     *
     * @return The number the cell should contain, or 0 if the number can't
     * be deduced from constraints.
     */
    public int deduceNumber() {
        // don't deduce things that are already set
        if (getNumber() != 0) return 0;

        int num = 0;
        int numConstraints = 0;

        for (int i = 1; i <= 9; i++) {
            if (!isConstraint(i))
                num = i;
            else
                numConstraints++;
        }

        return (numConstraints == 8) ? num : 0;
    }

    /**
     * Determine if this cell is locked. The cell is locked if it has no
     * number set, but all the numbers in [1, 9] are constraints.
     *
     * @return true if the cell is locked, false otherwise.
     */
    public boolean isLocked() {
        if (number == 0) {
            for (int i = 1; i <= 9; i++) {
                if (!isConstraint(i))
                    return false;
            } // for i

            return true;
        } // if cell is blank
        return false;
    }

    /**
     * Add a note to this cell.
     *
     * @param number Number in [1, 9] to be added as a note to the cell.
     * @throws IllegalArgumentException if the number is not in [1, 9].
     */
    public void setNote(int number) throws IllegalArgumentException {
        if (number < 1 || number > 9) {
            throw new IllegalArgumentException("Illegal number in "
                    + "Cell.setNote(): " + number);
        }
        notes[number] = true;
    }

    /**
     * Remove a note from this cell.
     *
     * @param number Number in [1, 9] to be removed as a note from this cell.
     * @throws IllegalArgumentException if the number is not in [1, 9].
     */
    public void removeNote(int number) throws IllegalArgumentException {
        if (number < 1 || number > 9) {
            throw new IllegalArgumentException("Illegal number in "
                    + "Cell.removeNote(): " + number);
        }
        notes[number] = false;
    }

    /**
     * Get a boolean array representing the notes set on this cell. If
     * element i is true, that number is a note; if element i is false,
     * that number is not set as a note. Element 0 of the array is
     * unused.
     *
     * @return boolean array containing the notes.
     */
    public boolean[] getNotes() {
        return java.util.Arrays.copyOf(notes, notes.length);
    }

    /**
     * Remove all data from this cell: number, constraints, and notes are
     * all removed.
     */
    public void resetAll() {
        number = 0;
        isGiven = false;
        for (int i = 0; i < constraints.length; i++) {
            constraints[i] = 0;
        }
        for (int i = 0; i < notes.length; i++) {
            notes[i] = false;
        }
    }

    /**
     * Determine if it is possible to set a number in this cell. True if
     * it's not filled in, not a given, and not prohibited by constraints.
     *
     * @param number Number to check for, in [1, 9].
     * @return True if that number is legal for this cell, false otherwise.
     * @throws IllegalArgumentException If number isn't in [1, 9].
     */
    public boolean canSetNumber(int number) throws IllegalArgumentException {
        if (number < 1 || number > 9) {
            throw new IllegalArgumentException("Illegal number in "
                    + "Cell.canSetNumber(): " + number);
        }
        return !isGiven() && (getNumber() == 0) && !isConstraint(number);
    }

} // Cell
