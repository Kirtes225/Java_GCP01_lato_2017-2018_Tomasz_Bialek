package com.company.loggers;

import com.company.events.CrawlerEventType;
import com.example.Student;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ParallelLogger implements Logger {
    private final Logger[] loggers;
    private BlockingQueue<StatusStudent> logs = new LinkedBlockingQueue<>();

    public ParallelLogger(Logger[] loggers) {
        this.loggers = loggers;

        new Thread(new Runnable() {
            @Override
            public void run() {
                StatusStudent tmp;

                try {
                    while ((tmp = logs.take()) != null) {
                        for (Logger logger : loggers) {
                            logger.log(tmp.crawlerEventType, tmp.student);
                        }

                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }
/*
	public StatusStudent getLog() throws InterruptedException {
		return logs.take();
	}*/

    @Override
    public void log(CrawlerEventType crawlerEventType, Student student) {
        logs.add(new StatusStudent(crawlerEventType, student));
    }


    private class StatusStudent {
        private final CrawlerEventType crawlerEventType;
        private final Student student;

        public StatusStudent(CrawlerEventType crawlerEventType, Student student) {
            this.crawlerEventType = crawlerEventType;
            this.student = student;
        }
    }
/*AnInterface.class.isAssignableFrom(anInstance.getClass());*/

    public void cancel(){
        for (Logger l : loggers){
            if (Closeable.class.isAssignableFrom(l.getClass())){
                try {
                    ((Closeable) l).close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
