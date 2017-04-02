package com.company.loggers;

import com.example.Student;

public class ConsoleLogger implements Logger {

    public ConsoleLogger() {
    }

    @Override
    public void log(String status, Student student) {
        if (student == null) System.out.println(status);
        else System.out.println(status + " : " + student.toString());
    }
}
