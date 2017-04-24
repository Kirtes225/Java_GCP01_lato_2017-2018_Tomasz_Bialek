package com.company.loggers;

import com.example.Student;

import com.company.events.CrawlerEventType;

public class ConsoleLogger implements Logger {

    public ConsoleLogger() {
    }

    @Override
    public void log(CrawlerEventType crawlerEventType, Student student) {
        if (student == null) System.out.println(crawlerEventType);
        else System.out.println(crawlerEventType + " : " + student.toString());
    }
}
