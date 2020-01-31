package edu.doane.sudoku.view;

import javafx.scene.layout.*;
import javafx.scene.paint.Color;

/**
 * Custom Pane representing a 3x3 grid of blocks in the desktop SuDoKu app.
 *
 * @author Mark M. Meysenburg
 * @version 1/11/2020
 */public class UIGrid extends GridPane {
    public UIGrid() {
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
                Color.BLACK, null, null)));
        setHgap(4);
        setVgap(4);
    }
}
