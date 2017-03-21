package com.company;


import com.example.Student;

public class CrawlerEvent {
    private final CrawlerEventType type;
    private Crawler crawler;
    private Student student;
    private long iteration;

    public CrawlerEvent(CrawlerEventType type, Student student, long iteration) {
        this.type = type;
        this.student = student;
        this.iteration = iteration;
    }

    public CrawlerEvent(CrawlerEventType type) {
        this.type = type;
    }

    public CrawlerEvent(CrawlerEventType type, Crawler crawler) {
        this.type = type;
        this.crawler = crawler;
    }

    public CrawlerEvent(CrawlerEventType type, Crawler crawler, Student student) {
        this.type = type;
        this.crawler = crawler;
        this.student = student;
    }

    public CrawlerEvent(CrawlerEventType type, Student student) {
        this.type = type;
        this.student = student;
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
