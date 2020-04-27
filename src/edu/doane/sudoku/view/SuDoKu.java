package edu.doane.sudoku.view;

import edu.doane.sudoku.controller.DesktopController;
import edu.doane.sudoku.controller.DesktopTimer;
import edu.doane.sudoku.controller.SuDoKuController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import java.util.Optional;

/**
 * User interface for the desktop SuDoKu application.
 *
 * @author Mark M. Meysenburg
 * @version 1/10/2020
 */
public class SuDoKu extends Application implements SuDoKuUI {

    /**
     * Controller used by the app.
     */
    private SuDoKuController controller;

    /**
     * 9x9 array of UICells, displaying the numbers / notes of the game.
     */
    private UICell[][] cells;

    /**
     * Pane holding a 3x3 grid of blocks, each of which holds a 3x3 block of
     * cells.
     */
    private UIGrid grid;

    /**
     * 3x3 grid of blocks that go on the grid.
     */
    private UIBlock[][] blocks;

    /**
     * Menu bar with Game and Help menus.
     */
    private MenuBar mnuBar;

    /**
     * Status bar, with timer and notes mode strings.
     */
    private UIStatusBar statusBar;

    @Override
    public void start(Stage primaryStage) throws Exception {

        // set up the menus
        configureMenus();

        // set up the game grid
        configureGrid();

        // set up the status bar
        statusBar = new UIStatusBar();

        StackPane root = new StackPane();
        root.setAlignment(Pos.CENTER);

        // configure main UI
        Scene scene = new Scene(new VBox(root), 800, 800);
        grid.setPrefSize(800, 800);
        grid.setMaxSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        ((VBox) scene.getRoot()).getChildren().addAll(mnuBar, grid, statusBar);

        primaryStage.setTitle("Doane SuDoKu");
        primaryStage.setScene(scene);
        primaryStage.show();

        // create and connect controller and timer
        controller = new DesktopController(this, new DesktopTimer(), statusBar);

        // configure key events
        scene.setOnKeyPressed(new UIKeyHandler(cells, controller, statusBar));

        Platform.setImplicitExit(false);

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                event.consume();
                if (confirmExit() == true) {
                    Platform.exit();
                }
            }
        });
    }

    /**
     * Create the game grid (UIGrid, UIBlocks, UICells).
     */
    private void configureGrid() {
        buildCells();
        buildGrid();
    }

    /**
     * Create the 9x9 array of UICells displaying the numbers / notes in the
     * game.
     */
    private void buildCells() {
        cells = new UICell[9][9];
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                cells[row][col] = new UICell();
            }
        }
    }

    /**
     * Configure one of the JPanels that holds a 3x3 block of cells.
     *
     * @param row Row of the block
     * @param col Column of the block
     */
    private void buildBlock(int row, int col) {
        blocks[row][col] = new UIBlock();
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                blocks[row][col].add(cells[row * 3 + r][col * 3 + c], c, r, 1, 1);
            }
        }
    }

    /**
     * Create the Panes that hold the cells and provide the lines between the
     * cells and blocks of cells.
     */
    private void buildGrid() {
        grid = new UIGrid();

        blocks = new UIBlock[3][3];
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                buildBlock(row, col);
                grid.add(blocks[row][col], col, row, 1, 1);
            }
        }
    }

    /**
     * Create menu bar, menus, and menu items
     */
    private void configureMenus() {
        Menu mnuGame, mnuHelp, mnuTheme;
        MenuItem mtmNewGame, mtmClearGrid , mtmExit, mtmAbout, mtmDark, mtmLight;

        mnuGame = new Menu("_Game");
        mtmNewGame = new MenuItem("_New game");
        mtmNewGame.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                controller.requestGame(null);
            }
        });

        mtmClearGrid = new MenuItem("_Clear grid");
        mtmClearGrid.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) { controller.resetGrids(); }
        });

        mtmExit = new MenuItem("E_xit");
        mtmExit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                controller.shutDown();
            }
        });

        mnuGame.getItems().addAll(mtmNewGame, mtmClearGrid,
                new SeparatorMenuItem(), mtmExit);

        mnuHelp = new Menu("_Help");
        mtmAbout = new MenuItem("_About");
        mtmAbout.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                controller.displayAbout();
            }
        });

        mnuHelp.getItems().addAll(mtmAbout);

        mnuTheme = new Menu("Theme");
        mtmDark = new MenuItem("Dark Mode");
        mtmDark.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                for (int row = 0; row < 9; row++) {
                    for (int col = 0; col < 9; col++) {
                        cells[row][col].setDarkMode();
                    }
                }
            }
        });

        mtmLight = new MenuItem("Light Mode");
        mtmLight.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                for (int row = 0; row < 9; row++) {
                    for (int col = 0; col < 9; col++) {
                        cells[row][col].setLightMode();
                    }
                }
            }
        });

        mnuTheme.getItems().addAll(mtmDark, mtmLight);

        mnuBar = new MenuBar(mnuGame, mnuHelp, mnuTheme);
    }

    /**
     * Application entry point.
     *
     * @param args command-line arguments. Ignored by this application
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Clear the grid, reset the timer, etc., in preparation for a new game.
     *
     * @param newGame true if the grid is being cleared for a new game, false
     *                otherwise.
     */
    @Override
    public void clearGrid(boolean newGame) {
        if (newGame) {
            DesktopAudio.getInstance().playNewGame();
        } else {
            DesktopAudio.getInstance().playClearGrid();
        }

        for(int row = 0; row < 9; row++) {
            for(int col = 0; col < 9; col++) {
                if(newGame) {
                    cells[row][col].unsetGiven();
                }
                cells[row][col].clearNumber();
                cells[row][col].clearAllNotes();
                UIKeyHandler.setPausedModeOff();
            }
        }
    }

    /**
     * Set the difficulties for games that can be produced by the system.
     *
     * @param difficulties Array of strings with the difficulty names.
     */
    @Override
    public void setDifficulties(String[] difficulties) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Set a given at the specified location.
     *
     * @param row    Row of the given to set.
     * @param col    Column of the given to set.
     * @param number Given value to place in the specified location.
     */
    @Override
    public void setGiven(int row, int col, int number) {
        char n = '0';
        switch(number) {
            case 1:
                n = '1'; break;
            case 2:
                n = '2'; break;
            case 3:
                n = '3'; break;
            case 4:
                n = '4'; break;
            case 5:
                n = '5'; break;
            case 6:
                n = '6'; break;
            case 7:
                n = '7'; break;
            case 8:
                n = '8'; break;
            case 9:
                n = '9'; break;
        }

        cells[row][col].setGiven(n);
    }

    /**
     * Set a given at the specified location.
     *
     * @param row    Row of the given to set.
     * @param col    Column of the given to set.
     * @param number Given value to place in the specified location.
     */
    public void resetColor(int row, int col, int number) {
        char n = '0';
        switch(number) {
            case 1:
                n = '1'; break;
            case 2:
                n = '2'; break;
            case 3:
                n = '3'; break;
            case 4:
                n = '4'; break;
            case 5:
                n = '5'; break;
            case 6:
                n = '6'; break;
            case 7:
                n = '7'; break;
            case 8:
                n = '8'; break;
            case 9:
                n = '9'; break;
        }

        cells[row][col].resetColor(n);
    }

    /**
     * Set a given at the specified location.
     *
     * @param row    Row of the given to set.
     * @param col    Column of the given to set.
     * @param number Given value to place in the specified location.
     */
    @Override
    public void hideGiven(int row, int col, int number) {
        char n = '0';
        switch(number) {
            case 1:
                n = '1'; break;
            case 2:
                n = '2'; break;
            case 3:
                n = '3'; break;
            case 4:
                n = '4'; break;
            case 5:
                n = '5'; break;
            case 6:
                n = '6'; break;
            case 7:
                n = '7'; break;
            case 8:
                n = '8'; break;
            case 9:
                n = '9'; break;
        }

        cells[row][col].hideGiven(n);
    }

    /**
     * Set a number at the specified location.
     *
     * @param row    Row of the number to set.
     * @param col    Column of the number to set.
     * @param number Number value to place in the specified location.
     */
    @Override
    public void setNumber(int row, int col, int number) {
        if(number >= 1 && number <= 9) {
            DesktopAudio.getInstance().playPlayNumber();
        } else if(number == 0) {
            DesktopAudio.getInstance().playEraseNumber();
        }

        char n = '0';
        switch(number) {
            case 0:
                n = '0'; break;
            case 1:
                n = '1'; break;
            case 2:
                n = '2'; break;
            case 3:
                n = '3'; break;
            case 4:
                n = '4'; break;
            case 5:
                n = '5'; break;
            case 6:
                n = '6'; break;
            case 7:
                n = '7'; break;
            case 8:
                n = '8'; break;
            case 9:
                n = '9'; break;
        }

        cells[row][col].setNumber(n);
    }

    /**
     * Toggle a number at the specified location.
     *
     * @param row    Row of the note to toggle
     * @param col    Column of the note to toggle
     * @param number Note value to toggle
     */
    @Override
    public void toggleNote(int row, int col, int number) {
        DesktopAudio.getInstance().playNoteToggle();
        char c = (char)('0' + number);
        cells[row][col].toggleNote(c);
    }

    /**
     * Set the time value to be displayed on the UI.
     *
     * @param value Time value to display, in h:mm:ss format.
     */
    @Override
    public void setTimerValue(String value) {
        statusBar.setTime(value);
    }

    /**
     * Celebrate a completed game!
     *
     * @param id   ID number of the completed game.
     * @param time String holding the amount of time taken to win.
     */
    @Override
    public void celebrate(int id, String time, int hints) {
        DesktopAudio.getInstance().playCelebrate();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Winner!");
        alert.setHeaderText(null);
        alert.setContentText("Congratulations! You won game #" +
                id + " in " + time + "! You used " + hints + " total hints!");
        alert.showAndWait();
    }

    /**
     * Confirm that the player really wants to exit the game.
     *
     * @return True if the player wants to quit, false otherwise.
     */
    @Override
    public boolean confirmExit() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Quit Doane SuDoKu?");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to exit?");

        Optional<ButtonType> result = alert.showAndWait();

        return result.get() == ButtonType.OK;
    }

    /**
     * Confirm that the player really wants to begin a new game. Any game currently
     * in progress is abandoned.
     *
     * @return True if the player wants to begin a new game, false otherwise.
     */
    @Override
    public boolean confirmNewGame() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("New game?");
        alert.setHeaderText(null);
        alert.setContentText("Do you want to start a new game?");

        Optional<ButtonType> result = alert.showAndWait();

        return result.get() == ButtonType.OK;
    }

    /**
     * Display the modal "About Doane SuDoKu" dialog box.
     */
    @Override
    public void displayAbout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About Doane SuDoKu");
        alert.setHeaderText(null);
        alert.setContentText("Doane SuDoku\nCopyright " +
                "\u00A9" + "2016, 2020");
        
        alert.showAndWait();
    }
}
