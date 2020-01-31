package edu.doane.sudoku.view;

import javafx.scene.layout.*;
import javafx.scene.paint.Color;

/**
 * JavaFX Pane representing a 3x3 block of cells in the desktop UI.
 *
 * @author Mark M. Meysenburg
 * @version 1/10/2020
 */
public class UIBlock extends GridPane {
    public UIBlock() {
        super();

        // configure 3 columns, 3 rows, equally distributed
        ColumnConstraints col1, col2, col3;
        col1 = new ColumnConstraints(); col1.setPercentWidth(33.33);
        col2 = new ColumnConstraints(); col2.setPercentWidth(33.33);
        col3 = new ColumnConstraints(); col3.setPercentWidth(33.33);
        getColumnConstraints().addAll(col1, col2, col3);

        RowConstraints row1, row2, row3;
        row1 = new RowConstraints(); row1.setPercentHeight(33.33);
        row2 = new RowConstraints(); row2.setPercentHeight(33.33);
        row3 = new RowConstraints(); row3.setPercentHeight(33.33);
        getRowConstraints().addAll(row1, row2, row3);

        setBackground(new Background(new BackgroundFill(
                Color.DARKSLATEGRAY, null, null)));
        setHgap(2);
        setVgap(2);
    }
}
