package com.company.fxml;

import com.example.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.util.StringConverter;
import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

@ThreadSafe
public class HistogramController implements Initializable {

	private final ObservableList<XYChart.Series<String, Double>> barChartData = FXCollections.observableArrayList();
	@GuardedBy("marksAndSeriesLock")
	private final XYChart.Series<String, Double> series = new XYChart.Series<>();
	@GuardedBy("marksAndSeriesLock")
	//private final int[] marksCount = {0, 0, 0, 0, 0, 0};
	private final Object marksAndSeriesLock = new Object();
	private final CategoryAxis xAxis = new CategoryAxis();
	private final NumberAxis yAxis = new NumberAxis();
	private final Label label = new Label("Distribution of marks");
	private final ArrayList<String> xAxisLabels = new ArrayList<String>(6) {{
		add("2.0");
		add("3.0");
		add("3.5");
		add("4.0");
		add("4.5");
		add("5.0");
	}};
	@FXML
	private BarChart<String, Double> histogram;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		series.setName("Marks");
		xAxis.setLabel("Mark");
		yAxis.setLabel("Count");
		yAxis.setTickLabelFormatter(new StringConverter<Number>() {
			@Override
			public String toString(Number object) {
				return String.valueOf(object.intValue());
			}

			@Override
			public Number fromString(String string) {
				return null;
			}
		});

		((ValueAxis<Double>) histogram.getYAxis()).setTickLabelFormatter(new StringConverter<Double>() {
			@Override
			public String toString(Double object) {
				return String.valueOf(object.intValue());
			}

			@Override
			public Double fromString(String string) {
				return null;
			}
		});

		synchronized (marksAndSeriesLock) {
			for (int i = 0; i < 6; i++) {
				series.getData().add(new XYChart.Data(xAxisLabels.get(i), 0));
			}

			barChartData.addAll(series);
		}

		histogram.setData(barChartData);
	}

	public void setStudents(ObservableList<Student> students){
		int tmp;
		final int[] marksCount = {0, 0, 0, 0, 0, 0};

		synchronized (marksAndSeriesLock) {
			for (Student s : students){
				if ((tmp = xAxisLabels.indexOf(String.valueOf(s.getMark()))) >= 0) {
					marksCount[tmp]++;
				}
			}

			for (int i = 0; i < marksCount.length; i++) {
				series.getData().get(i).setYValue(Double.valueOf(marksCount[i]));
			}
		}

	}
}