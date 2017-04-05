package com.company.GUI;

import com.example.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;

import java.text.SimpleDateFormat;
import java.util.Date;


public class CustomLogTab extends Tab {

    private final ListView<String> listView;

    private final ObservableList<String> observableList;

    public CustomLogTab() {
        this.listView = new ListView<>();
        this.observableList = FXCollections.observableArrayList();
        this.listView.setItems(observableList);
        this.setContent(this.listView);
        this.setText("Log");
        this.setClosable(false);
    }

    public synchronized void addData(String status, Student student) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss.SSS");

        Date now = new Date();
        String strDate = simpleDateFormat.format(now);


        observableList.add(strDate + "  [" + status + "]  " +
                student.getFirstName() + "  " + student.getLastName());
    }
}