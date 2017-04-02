package com.company.GUI;

import com.example.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.concurrent.CopyOnWriteArrayList;

public class CustomTabPane extends TabPane {

    final Histogram histogram;
    final StudentsTab studentsTab;
    private final LogTab logTab = new LogTab();
    private final CopyOnWriteArrayList<Student> students = new CopyOnWriteArrayList<>();

    public CustomTabPane() {
        super();

        studentsTab = new StudentsTab();
        histogram = new Histogram();

        TableView table = new TableView();

        TableColumn markColumn = new TableColumn("Mark");
        TableColumn firstNameColumn = new TableColumn("First Name");
        TableColumn lastNameColumn = new TableColumn("Last Name");
        TableColumn ageColumn = new TableColumn("Age");

        table.getColumns().addAll(markColumn, firstNameColumn, lastNameColumn, ageColumn);

        this.getTabs().addAll(studentsTab, logTab, histogram);
    }

    public LogTab getLogTab() {
        return logTab;
    }

    public Histogram getHistogram() {
        return histogram;
    }

    public void addStudent(Student student) {
        students.add(student);

        histogram.setData(getChartData());
        studentsTab.setData(students);
    }

    public void removeStudent(Student student) {
        students.remove(student);

        histogram.setData(getChartData());
    }


    private ObservableList<XYChart.Series<String, Double>> getChartData() {
        final int[] marksCount = {
                0, //2.0
                0, //3.0
                0, //3.5
                0, //4.0
                0, //4.5
                0};//5.0

        String[] xAxisLabels = {"2.0", "3.0", "3.5", "4.0", "4.5", "5.0"};

        for (Student s : students) {
            switch ((int) s.getMark() * 10) {
                case 20:
                    marksCount[0]++;
                    break;
                case 30:
                    marksCount[1]++;
                    break;
                case 35:
                    marksCount[2]++;
                    break;
                case 40:
                    marksCount[3]++;
                    break;
                case 45:
                    marksCount[4]++;
                    break;
                case 50:
                    marksCount[5]++;
                    break;
                default:
                    break;
            }
        }

        final ObservableList<XYChart.Series<String, Double>> answer = FXCollections.observableArrayList();
        final XYChart.Series<String, Double> series = new XYChart.Series<String, Double>();
        series.setName("Marks");

        for (int i = 0; i < marksCount.length; i++) {
            series.getData().add(new XYChart.Data(xAxisLabels[i], marksCount[i]));
        }

        answer.addAll(series);
        return answer;
    }

}