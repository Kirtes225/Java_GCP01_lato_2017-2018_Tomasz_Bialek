package com.company.events;

import com.example.Student;

public abstract class AbstractCrawlerEvent {
	private final CrawlerEventType type;
	private final Student student;

	public AbstractCrawlerEvent(CrawlerEventType type, Student student) {

		this.type = type;
		this.student = student;
	}

	public CrawlerEventType getType() {
		return type;
	}

	public Student getStudent() {
		return student;
	}
}
