package com.company.GUI;

import com.example.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.concurrent.CopyOnWriteArrayList;

public class CustomTableView extends Tab {

    private final TableColumn markColumn;
    private final TableColumn firstNameColumn;
    private final TableColumn lastNameColumn;
    private final TableColumn ageColumn;
    private final TableView table;
    private ObservableList<Student> students;

    public CustomTableView() {
        students = FXCollections.observableArrayList();

        this.setText("Students");
        this.setClosable(false);

        table = new TableView();

        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        markColumn = new TableColumn("Mark");
        firstNameColumn = new TableColumn("First Name");
        lastNameColumn = new TableColumn("Last Name");
        ageColumn = new TableColumn("Age");

        markColumn.setCellValueFactory(new PropertyValueFactory<Student, String>("mark"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<Student, String>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<Student, String>("lastName"));
        ageColumn.setCellValueFactory(new PropertyValueFactory<Student, String>("age"));

        table.getColumns().addAll(markColumn, firstNameColumn, lastNameColumn, ageColumn);

        this.setContent(this.table);
    }

    public synchronized void setData(CopyOnWriteArrayList<Student> newStudents) {
        students = FXCollections.observableArrayList(newStudents);
        this.table.setItems(students);
    }
}