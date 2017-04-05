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

   /*private final int marksCount[] = {
            0, //2.0
            0, //3.0
            0, //3.5
            0, //4.0
            0, //4.5
            0, //5.0
    };*/


    final CustomBarChart customBarChart;
    final CustomTableView customTableView;
    private final CustomLogTab customLogTab = new CustomLogTab();
    private final CopyOnWriteArrayList<Student> students = new CopyOnWriteArrayList<>();

    /*public double maxMarkCount(){

        double max = -1;

        for (int i = 1; i < marksCount.length; i++) {
            if (marksCount[i] > max) {
                max = marksCount[i];
            }
        }
        return max;
    }*/

    public CustomTabPane() {
        super();

        customTableView = new CustomTableView();
        customBarChart = new CustomBarChart();

        TableView table = new TableView();

        TableColumn markColumn = new TableColumn("Mark");
        TableColumn firstNameColumn = new TableColumn("First Name");
        TableColumn lastNameColumn = new TableColumn("Last Name");
        TableColumn ageColumn = new TableColumn("Age");

        table.getColumns().addAll(markColumn, firstNameColumn, lastNameColumn, ageColumn);

        this.getTabs().addAll(customTableView, customLogTab, customBarChart);
    }

    public CustomLogTab getCustomLogTab() {
        return customLogTab;
    }


    public void addStudent(Student student) {
        students.add(student);

        customBarChart.setData(getChartData());
        customTableView.setData(students);
    }

    public void removeStudent(Student student) {
        students.remove(student);

        customBarChart.setData(getChartData());
    }


    private ObservableList<XYChart.Series<String, Double>> getChartData() {
        final int marksCount[] = {
                0, //2.0
                0, //3.0
                0, //3.5
                0, //4.0
                0, //4.5
                0, //5.0
        };

        String[] xAxisLabels = {"2.0", "3.0", "3.5", "4.0", "4.5", "5.0"};

        for (Student st : students) {
            if(st.getMark() == 2.0)
                marksCount[0]++;
            if(st.getMark() == 3.0)
                marksCount[1]++;
            if(st.getMark() == 3.5)
                marksCount[2]++;
            if(st.getMark() == 4.0)
                marksCount[3]++;
            if(st.getMark() == 4.5)
                marksCount[4]++;
            if(st.getMark() == 5.0)
                marksCount[5]++;
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