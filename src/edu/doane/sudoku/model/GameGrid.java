package edu.doane.sudoku.model;

import java.util.TreeSet;

/**
 * Class representing a 9x9 grid of cells, during the playing of a SuDoKu game.
 *
 * @author Mark M. Meysenburg
 * @version 05/26/2014
 */
public class GameGrid {

    /**
     * 2d, 9x9 array of cells for the grid.
     */
    protected Cell[][] grid;

    /**
     * Construct a new grid, with all cells set to 0 (blank).
     */
    public GameGrid() {
        createBlankArray();
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
    public GameGrid(String gameData) throws IllegalArgumentException {
        createBlankArray();
        setGameData(gameData);
    }


    /**
     * Place a number into the cell at the specified location. If the number in
     * the cell is a given, do nothing. If the cell has an existing (non-given)
     * number, unset it and then set the new one.
     *
     * @param row    Row of the cell to set, in [0, 8].
     * @param col    Column of the cell to set, in [0, 8].
     * @param number Number to set, in [0, 9].
     * @return true if the number in the specified cell was changed, false
     * otherwise.
     * @throws IllegalArgumentException if row or col is not in [0, 8], or if
     *                                  number is not in [0, 9].
     */
    public boolean setNumber(int row, int col, int number) throws IllegalArgumentException {
        if (row < 0 || row > 8 || col < 0 || col > 8 || number < 1 || number > 9) {
            throw new IllegalArgumentException("Illegal parameters to "
                    + "Grid.setNumber(): (" + row + ", " + col
                    + ", " + number + ")");
        }

        int currentNumber = getNumber(row, col);
        if (currentNumber == 0) {
            // if the cell is empty, just set the number
            grid[row][col].setNumber(number);

            return true;
        } else {
            // if not empty, and not a given, unset then set
            if (!isGiven(row, col)) {
                unsetNumber(row, col);
                grid[row][col].setNumber(number);

                return true;
            }
        }

        return false;
    }

    /**
     * Get the number stored in the specified location.
     *
     * @param row Row of the cell to get, in [0, 8].
     * @param col Column of the cell to get, in [0, 8].
     * @return an integer in [0, 9]. 0 means the cell is empty; [1, 9] if the
     * cell has a number.
     * @throws IllegalArgumentException if row or col is not in [0, 8].
     */
    public int getNumber(int row, int col) throws IllegalArgumentException {
        if (row < 0 || row > 8 || col < 0 || col > 8) {
            throw new IllegalArgumentException("Bad coordinates to "
                    + "Grid.getNumber(): (" + row + ", " + col + ")");
        }

        return grid[row][col].getNumber();
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
        if (row < 0 || row > 8 || col < 0 || col > 8) {
            throw new IllegalArgumentException("Bad coordinates to "
                    + "Grid.unsetNumber(): (" + row + ", " + col + ")");
        }

        int currentNum = getNumber(row, col);
        if (!isGiven(row, col) && (currentNum != 0)) {
            grid[row][col].unsetNumber();
            return true;
        }

        return false;
    }

    /**
     * Is the cell at the specified location a given?
     *
     * @param row Row of the cell to test, in [0, 8].
     * @param col Column of the cell to test, in [0, 8].
     * @return true if the cell is a given, false otherwise.
     * @throws IllegalArgumentException if row or col is not in [0, 8].
     */
    public boolean isGiven(int row, int col) throws IllegalArgumentException {
        if (row < 0 || row > 8 || col < 0 || col > 8) {
            throw new IllegalArgumentException("Bad coordinates to "
                    + "Grid.isGiven(): (" + row + ", " + col + ")");
        }

        return grid[row][col].isGiven();
    }

    /**
     * Is this grid complete (i.e., all filled in)?
     *
     * @return true if the grid is complete, false otherwise.
     */
    public boolean isComplete() {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (grid[row][col].getNumber() == 0) {
                    return false;
                }
            } // col
        } // row

        return true;
    }

    /**
     * Remove all non-given numbers from this grid. Also removes notes from all
     * the cells.
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
     * Get the game data for this grid, as a 81-character string. Game data
     * means the numbers in the grid. 0 means empty cell, [1, 9] for numbers in
     * the grid.
     *
     * @return 81-character string with the grid's game data.
     */
    public String getGameData() {
        StringBuilder buf = new StringBuilder();

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                buf.append(grid[row][col].getNumber());
            } // col
        } // row

        return buf.toString();
    }

    /**
     * Set the game data for this grid. Any existing numbers are unset, and all
     * existing notes are also erased.
     *
     * @param data 81-character string with the game data for the grid. 0 means
     *             an empty cell, or [1, 9] for numbers in the grid.
     * @throws IllegalArgumentException If the string is not of the correct
     *                                  length, or if it contains illegal characters (not in [0, 9])
     */
    public void setGameData(String data) throws IllegalArgumentException {
        if (data.length() != 81) {
            throw new IllegalArgumentException("Illegal parameter to "
                    + "Grid.setGameData(); data is not 81 characters: "
                    + data.length());
        }

        // dump any existing data
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid.length; col++) {
                grid[row][col].resetAll();
            } // for col
        } // for row

        // set new data
        int k = 0;
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid.length; col++) {
                int number = data.charAt(k) - '0';

                if (number < 0 || number > 9) {
                    throw new IllegalArgumentException("Illegal character in "
                            + "Grid.setGameData(): " + data.charAt(k));
                }

                if (number != 0) {
                    setNumber(row, col, number);
                    grid[row][col].setIsGiven(true);
                } // if non-blank

                k++;
            } // for col
        } // for row
    }

    /**
     * Get a string representation of this grid.
     *
     * @return String representation of this grid.
     */
    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                int number = grid[i][j].getNumber();
                if (number == 0) {
                    buf.append(".");
                } else {
                    buf.append(number);
                }
                buf.append(" ");
                if (j == 2 || j == 5) {
                    buf.append("| ");
                }
            }
            if (i == 2 || i == 5) {
                buf.append("\n----------------------");
            }
            buf.append("\n");
        }

        return buf.toString();
    }

    /**
     * Allocate the 2d, 9x9 array of cells for this grid. All of the cells are
     * blank.
     */
    protected void createBlankArray() {
        grid = new Cell[9][9];
        for (int row = 0; row < grid.length; row++) {
            grid[row] = new Cell[9];
            for (int col = 0; col < grid[row].length; col++) {
                grid[row][col] = new Cell();
            } // for col
        } // for row
    }

    /**
     * Add a note to the specified cell in the grid.
     *
     * @param row    row to add a note to, in [0, 8].
     * @param col    column to add a note to, in [0, 8].
     * @param number number to add as a note, in [1, 9].
     * @throws IllegalArgumentException if row or column is not in [0, 8], or if
     *                                  number is not in [1, 9].
     */
    public void setNote(int row, int col, int number) throws IllegalArgumentException {
        if (row < 0 || row > 8 || col < 0 || col > 8 || number < 1 || number > 9) {
            throw new IllegalArgumentException("Illegal parameters to "
                    + "Grid.setNote(): (" + row + ", " + col
                    + ", " + number + ")");
        }

        grid[row][col].setNote(number);
    }

    /**
     * Remove a note from the specified cell in the grid.
     *
     * @param row    row to remove note from, in [0, 8].
     * @param col    column to remove note from, in [0, 8].
     * @param number number to remove as a note, in [1, 9].
     * @throws IllegalArgumentException if row or column is not in [0, 8], or if
     *                                  number is not in [1, 9].
     */
    public void removeNote(int row, int col, int number) throws IllegalArgumentException {
        if (row < 0 || row > 8 || col < 0 || col > 8 || number < 1 || number > 9) {
            throw new IllegalArgumentException("Illegal parameters to "
                    + "Grid.removeNote(): (" + row + ", " + col
                    + ", " + number + ")");
        }

        grid[row][col].removeNote(number);
    }

    /**
     * Get the notes from the specified cell in the grid.
     *
     * @param row row to get notes from, in [0, 8].
     * @param col column to get notes from, in [0, 8].
     * @return array of booleans representing the notes for the specified cell.
     * @throws IllegalArgumentException if row or col is not in [0, 8].
     */
    public boolean[] getNotes(int row, int col) throws IllegalArgumentException {
        if (row < 0 || row > 8 || col < 0 || col > 8) {
            throw new IllegalArgumentException("Illegal parameters to "
                    + "Grid.getNotes(): (" + row + ", " + col + ")");
        }

        return grid[row][col].getNotes();
    }

    /**
     * Make sure this is a valid, filled in grid.
     *
     * @return true if it's a valid, completely filled grid; false otherwise.
     */
    public boolean validate() {
        if (!isComplete()) {
            return false;
        }

        TreeSet<Integer> targets = new TreeSet<>();
        for (int i = 1; i <= 9; i++) {
            targets.add(i);
        }

        // each row complete?
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                targets.remove(grid[row][col].getNumber());
            }

            if (!targets.isEmpty()) {
                return false;
            }
            targets.clear();
            for (int i = 1; i <= 9; i++) {
                targets.add(i);
            }
        } // for row

        // each column complete?
        for (int col = 0; col < 9; col++) {
            for (int row = 0; row < 9; row++) {
                targets.remove(grid[row][col].getNumber());
            }

            if (!targets.isEmpty()) {
                return false;
            }
            targets.clear();
            for (int i = 1; i <= 9; i++) {
                targets.add(i);
            }
        } // for col

        // each block complete?
        for (int rStart = 0; rStart < 9; rStart += 3) {
            for (int cStart = 0; cStart < 9; cStart += 3) {
                for (int row = rStart; row < rStart + 3; row++) {
                    for (int col = cStart; col < cStart + 3; col++) {
                        targets.remove(grid[row][col].getNumber());
                    } // for col
                } // for row

                if (!targets.isEmpty()) {
                    return false;
                }

                targets.clear();
                for (int i = 1; i <= 9; i++) {
                    targets.add(i);
                }
            } // for cStart
        } // for rStart
        return true;
    }
}