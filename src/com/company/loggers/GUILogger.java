package com.company.loggers;

import com.example.Student;

import com.company.GUI.LogTab;

public class GUILogger implements Logger {
    private final LogTab logTab;

    public GUILogger(LogTab logTab) {
        this.logTab = logTab;
    }

    @Override
    public void log(String status, Student student) {
        logTab.addData(status, student);
    }
}