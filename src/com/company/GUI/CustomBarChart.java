package com.company.GUI;

import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

public class CustomBarChart extends Tab {
    private final Label label = new Label("Distribution of marks");

    private final CategoryAxis xAxis = new CategoryAxis();
    private final NumberAxis yAxis = new NumberAxis(/*0, maxMarkCount(), 1*/);
    private final BarChart chart = new BarChart(xAxis, yAxis);


    public CustomBarChart() {
        super();

        this.yAxis.setTickUnit(1);
        //this.yAxis.

        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().add(label);
        vBox.getChildren().add(chart);

        this.setContent(vBox);
        this.setText("Histogram");
        this.setClosable(false);

        this.xAxis.setLabel("Marks");

        /*this.xAxis.setCategories(FXCollections.<String>observableArrayList(
                "2.0", "3.0", "3.5", "4.0", "4.5", "5.0"
        ));*/

        this.yAxis.setLabel("Count");
        this.yAxis.setTickLabelFormatter(new StringConverter<Number>() {
            @Override
            public String toString(Number object) {
                return String.valueOf(object.intValue());
            }

            @Override
            public Number fromString(String string) {
                return null;
            }
        });

    }

    public synchronized void setData(ObservableList<XYChart.Series<String, Double>> observableList) {
        this.chart.setData(observableList);
    }
}
