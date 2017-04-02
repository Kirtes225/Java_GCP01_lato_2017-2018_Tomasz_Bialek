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

public class Histogram extends Tab {
    private final CategoryAxis xAxis = new CategoryAxis();
    private final NumberAxis yAxis = new NumberAxis();
    private final BarChart chart = new BarChart(xAxis, yAxis);
    private final Label label = new Label("Distribution of marks");

    public Histogram() {
        super();

        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().add(label);
        vBox.getChildren().add(chart);

        this.setContent(vBox);
        this.setText("Histogram");
        this.setClosable(false);

        this.xAxis.setLabel("Marks");
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
