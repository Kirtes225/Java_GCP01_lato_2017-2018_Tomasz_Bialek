package com.company.events;

import com.example.Student;

public class CrawlerEvent extends AbstractCrawlerEvent {
	private final long iteration;

	public CrawlerEvent(CrawlerEventType type, Student student, long iteration) {
		super(type, student);
		this.iteration = iteration;
	}

	public long getIteration() {
		return iteration;
	}

}


