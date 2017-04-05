package com.company.loggers;

import com.example.Student;

import com.company.GUI.CustomLogTab;

public class GUILogger implements Logger {
    private final CustomLogTab customLogTab;

    public GUILogger(CustomLogTab customLogTab) {
        this.customLogTab = customLogTab;
    }

    @Override
    public void log(String status, Student student) {
        customLogTab.addData(status, student);
    }
}