package com.company.loggers;


import com.example.Student;

import com.company.events.CrawlerEventType;

public interface Logger {
    //void log(String status, Student student);
    void log(CrawlerEventType crawlerEventType, Student student);
}
