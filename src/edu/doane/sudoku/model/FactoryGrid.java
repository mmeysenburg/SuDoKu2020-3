package edu.doane.sudoku.model;

import java.util.LinkedList;

/**
 * Class representing a 9x9 grid of cells, during the creation of a SuDoKu game.
 *
 * @author Mark M. Meysenburg
 * @version 05/26/2014
 */
public final class FactoryGrid extends GameGrid {

    /**
     * Construct a new grid, with all cells set to 0 (blank).
     */
    public FactoryGrid() {
        super();
    } // default constructor

    /**
     * Construct a new grid, initialized as per the string parameter.
     *
     * @param gameData 81-character representing an initial SuDoKu game. Each
     *                 character is a digit in [0, 9]. 0 means empty cell, [1, 9] is a given at
     *                 the start of a game.
     * @throws IllegalArgumentException If the string is not of the correct
     *                                  length, or if it contains illegal characters (not in [0, 9])
     */
    public FactoryGrid(String gameData) throws IllegalArgumentException {
        super(gameData);
    }

    /**
     * Determine if the specified number could be set in the specified location.
     * "Settability" is based on the constraints on the cell.
     *
     * @param row    Row of the cell to test, in [0, 8].
     * @param col    Column of the cell to test, in [0, 8].
     * @param number Number to test, in [1, 9].
     * @return true if the number can be set in the specified location, false
     * otherwise.
     * @throws IllegalArgumentException if row or col is not in [0, 8], or if
     *                                  number is not in [1, 9].
     */
    public boolean canSetNumber(int row, int col, int number) throws IllegalArgumentException {
        if (row < 0 || row > 8 || col < 0 || col > 8 || number < 1 || number > 9) {
            throw new IllegalArgumentException("Illegal parameters to "
                    + "Grid.canSetNumber(): (" + row + ", " + col
                    + ", " + number + ")");
        }
        return grid[row][col].canSetNumber(number);
    }

    /**
     * Place a number into the cell at the specified location. If the number in
     * the cell is a given, do nothing. If the cell has an existing (non- given)
     * number, unset it and then set the new one.
     *
     * @param row    Row of the cell to set, in [0, 8].
     * @param col    Column of the cell to set, in [0, 8].
     * @param number Number to set, in [1, 9].
     * @return true if the number in the specified cell was changed, false
     * otherwise.
     * @throws IllegalArgumentException if row or col is not in [0, 8], or if
     *                                  number is not in [1, 9].
     */
    public boolean setNumber(int row, int col, int number) throws IllegalArgumentException {
        if (super.setNumber(row, col, number)) {
            rippleConstraints(row, col, number);
            return true;
        }

        return false;
    }

    /**
     * Unset the number at the specified location. If the number in that cell is
     * a given, do nothing.
     *
     * @param row Row of the cell to unset, in [0, 8].
     * @param col Column of the cell to unset, in [0, 8].
     * @return true if the number was unset, false otherwise.
     * @throws IllegalArgumentException if row or col is not in [0, 8].
     */
    public boolean unsetNumber(int row, int col) throws IllegalArgumentException {
        int currentNum = getNumber(row, col);
        if (super.unsetNumber(row, col)) {
            unRippleConstraints(row, col, currentNum);
            return true;
        }
        return false;
    }

    /**
     * Does the cell at the specified location have a specified constraint?
     *
     * @param row    Row of the cell to test, in [0, 8].
     * @param col    Column of the cell to test, in [0, 8].
     * @param number Number to test, in [1, 9].
     * @return true if the cell is a given, false otherwise.
     * @throws IllegalArgumentException if row or col is not in [0, 8].
     */
    public boolean isConstraint(int row, int col, int number) throws IllegalArgumentException {
        if (row < 0 || row > 8 || col < 0 || col > 8 || number < 1 || number > 9) {
            throw new IllegalArgumentException("Illegal parameters to "
                    + "Grid.isConstraint(): (" + row + ", " + col
                    + ", " + number + ")");
        }
        return grid[row][col].isConstraint(number);
    }

    /**
     * Is this grid locked (i.e., not complete, but no valid numbers to set)?
     *
     * @return true if the grid is locked, false otherwise.
     */
    public boolean isLocked() {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (grid[row][col].isLocked()) {
                    return true;
                }
            } // col
        } // row
        return false;
    }

    /**
     * Remove all non-given numbers from this grid. Also removes constraints
     * that come from givens, and notes from all the cells.
     */
    public void clearGrid() {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (!isGiven(row, col)) {
                    unsetNumber(row, col);
                } // if

                for (int number = 1; number <= 9; number++) {
                    grid[row][col].removeNote(number);
                }
            } // col
        } // row
    }

    /**
     * Set the game data for this grid. Any existing numbers are unset, and all
     * existing constraints and notes are also erased. New constraints are set
     * based on the new numbers.
     *
     * @param data 81-character string with the game data for the grid. 0 means
     *             an empty cell, or [1, 9] for numbers in the grid.
     * @throws IllegalArgumentException If the string is not of the correct
     *                                  length, or if it contains illegal characters (not in [0, 9])
     */
    public void setGameData(String data) throws IllegalArgumentException {
        super.setGameData(data);

        // add constraints
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                int number = grid[row][col].getNumber();
                if (number != 0) {
                    rippleConstraints(row, col, number);
                }
            }
        }
    }

    /**
     * Ripple a constraint to a cell's row, column, and block. We do this when
     * the number in a cell changes.
     *
     * @param row    Row of the cell to ripple from, in [0, 8].
     * @param col    Column of the cell to ripple from, in [0, 8].
     * @param number Number to ripple, in [1, 9].
     * @throws IllegalArgumentException if row or col is not in [0, 8], or if
     *                                  number is not in [1, 9].
     */
    private void rippleConstraints(int row, int col, int number) throws IllegalArgumentException {
        if (row < 0 || row > 8 || col < 0 || col > 8 || number < 1 || number > 9) {
            throw new IllegalArgumentException("Illegal parameters to "
                    + "Grid.rippleConstraints(): (" + row + ", " + col
                    + ", " + number + ")");
        }

        int rStart = (row / 3) * 3;
        int cStart = (col / 3) * 3;

        // row
        for (int c = 0; c < 9; c++) {
            // don't add to cells in this block, so we don't overadd
            if (!(c >= cStart && c < (cStart + 3))) {
                grid[row][c].addConstraint(number);
            }
        }

        // column
        for (int r = 0; r < 9; r++) {
            // don't add to cells in this block, so we don't overadd
            if (!(r >= rStart && r < (rStart + 3))) {
                grid[r][col].addConstraint(number);
            }
        }

        // block
        for (int r = rStart; r < rStart + 3; r++) {
            for (int c = cStart; c < cStart + 3; c++) {
                grid[r][c].addConstraint(number);
            } // for c
        } // for r
    }

    /**
     * Unripple a constraint to a cell's row, column, and block. We do this when
     * the number in a cell changes, if there was a number already in the cell.
     *
     * @param row    Row of the cell to unripple from, in [0, 8].
     * @param col    Column of the cell to unripple from, in [0, 8].
     * @param number Number to unripple, in [1, 9].
     * @throws IllegalArgumentException if row or col is not in [0, 8], or if
     *                                  number is not in [1, 9].
     */
    private void unRippleConstraints(int row, int col, int number) throws IllegalArgumentException {
        if (row < 0 || row > 8 || col < 0 || col > 8 || number < 1 || number > 9) {
            throw new IllegalArgumentException("Illegal parameters to "
                    + "Grid.unRippleConstraints(): (" + row + ", " + col
                    + ", " + number + ")");
        }

        int rStart = (row / 3) * 3;
        int cStart = (col / 3) * 3;

        // row
        for (int c = 0; c < 9; c++) {
            // don't remove from cells in this block, so we don't overremove
            if (!(c >= cStart && c < (cStart + 3))) {
                grid[row][c].removeConstraint(number);
            }
        }

        // column
        for (int r = 0; r < 9; r++) {
            // don't remove from cells in this block, so we don't overremove
            if (!(r >= rStart && r < (rStart + 3))) {
                grid[r][col].removeConstraint(number);
            }
        }

        // block
        for (int r = rStart; r < rStart + 3; r++) {
            for (int c = cStart; c < cStart + 3; c++) {
                grid[r][c].removeConstraint(number);
            } // for c
        } // for r
    }

    /**
     * If the cell knows what number it should contain, based on constraints,
     * return that number.
     *
     * @param row Row of the cell to deduce.
     * @param col Column of the cell to deduce.
     * @return The number the cell should contain, or 0 if the number can't be
     * deduced from constraints.
     * @throws IllegalArgumentException If row or col is not in the range [0,
     *                                  8].
     */
    public int deduceNumber(int row, int col) throws IllegalArgumentException {
        if (row < 0 || row > 8 || col < 0 || col > 8) {
            throw new IllegalArgumentException("Illegal parameters to "
                    + "Grid.deduceNumber(): (" + row + ", " + col + ")");
        }

        return grid[row][col].deduceNumber();
    }

    /**
     * Add a constraint to a given cell in the grid.
     *
     * @param row    Row of the cell to add constraint to
     * @param col    Column of the cell to add constraint to
     * @param number Number to add as a constraint
     * @throws IllegalArgumentException If row or col is not in the range [0,
     *                                  8], or if number is not in [1, 9]
     */
    public void addConstraint(int row, int col, int number) throws IllegalArgumentException {
        if (row < 0 || row > 8 || col < 0 || col > 8 || number < 1 || number > 9) {
            throw new IllegalArgumentException("Illegal parameters to "
                    + "Grid.addConstraint(): (" + row + ", " + col + ","
                    + number + ")");
        }

        grid[row][col].addConstraint(number);
    }

    /**
     * Get a list of constraints for one of the cells in the grid.
     *
     * @param row row of the cell to examine, in [1, 9]
     * @param col column of the cell to examine, in [1, 9]
     * @return list of the constraints for the specified cell
     * @throws IllegalArgumentException if either row or col is not in [1, 9]
     */
    public LinkedList<Integer> getConstraints(int row, int col) throws IllegalArgumentException {
        if (row < 0 || row > 8 || col < 0 || col > 8) {
            throw new IllegalArgumentException("Illegal parameters to "
                    + "Grid.getConstraints(): (" + row + ", " + col + ")");
        }
        LinkedList<Integer> constraints = new LinkedList<>();

        for (int number = 1; number <= 9; number++) {
            if (grid[row][col].isConstraint(number)) {
                constraints.add(number);
            }
        }
        return constraints;
    }
}
