package com.company;

import com.example.Student;

public class CrawlerEvent {
    private final CrawlerEventType type;
    private final Student student;
    private final long iteration;

    public CrawlerEvent(CrawlerEventType type, Student student, long iteration) {
        this.type = type;
        this.student = student;
        this.iteration = iteration;
    }

    public long getIteration() {
        return iteration;
    }

    public CrawlerEventType getType() {
        return type;
    }

    public Student getStudent() {
        return student;
    }
}