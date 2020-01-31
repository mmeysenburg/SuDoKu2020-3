package edu.doane.sudoku.model;

/**
 * Class representing a SuDoKu game. This includes the given grid and the
 * solved grid.
 *
 * @author Mark M. Meysenburg
 * @version 12/28/2015
 */
public class Game {

    /**
     * Game id.
     */
    private int id;

    /**
     * Initial game grid.
     */
    private GameGrid initial;

    /**
     * Solved game grid.
     */
    private GameGrid solved;

    /**
     * Create a new game, with the specified initial and solved grids.
     *
     * @param initial Initial game grid.
     * @param solved  Solved game grid.
     */
    public Game(int id, GameGrid initial, GameGrid solved) {
        this.id = id;
        this.initial = initial;
        this.solved = solved;
    }

    /**
     * Get the initial grid for this game.
     *
     * @return Reference to a GameGrid object holding the initial
     * configuration for the game.
     */
    public GameGrid getInitial() {
        return initial;
    }

    /**
     * Get the solved grid for this game.
     *
     * @return Reference to a GameGrid object holding the solved
     * configuration for the game.
     */
    public GameGrid getSolved() {
        return solved;
    }

    /**
     * Get the ID number of this game.
     *
     * @return ID number of this game.
     */
    public int getID() {
        return id;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(id);
        sb.append(' ');
        sb.append(initial.getGameData());
        sb.append(' ');
        sb.append(solved.getGameData());

        return sb.toString();
    }
}
