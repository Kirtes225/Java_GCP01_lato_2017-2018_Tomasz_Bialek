package com.company;

import com.example.Student;

public class LoggerConsole implements Logger {

    public LoggerConsole() {
    }

    @Override
    public void log(String status, Student student) {
        if (student == null) System.out.println(status);
        else System.out.println(status + " : " + student.toString());
    }
}