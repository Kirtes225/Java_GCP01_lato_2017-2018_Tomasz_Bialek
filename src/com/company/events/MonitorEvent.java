package com.company.events;

import com.example.Student;

public class MonitorEvent extends AbstractCrawlerEvent {

	public MonitorEvent(CrawlerEventType type, Student student) {
		super(type, student);
	}
}
