package com.company.loggers;

import com.example.Student;

import com.company.events.CrawlerEventType;
import com.company.fxml.MainController;

public class GUILogger implements Logger {
    private final MainController mainController;


    public GUILogger(MainController mainController) {
        this.mainController = mainController;
    }

    @Override
    public void log(CrawlerEventType crawlerEventType, Student student) {
        if (CrawlerEventType.ADD.equals(crawlerEventType)) {
            mainController.addStudent(student);
        } else if (CrawlerEventType.DELETE.equals(crawlerEventType)) {
            mainController.removeStudent(student);
        }
    }
}
