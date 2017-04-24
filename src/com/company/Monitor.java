package com.company;

import com.company.events.AbstractCrawlerEvent;
import com.company.events.CrawlerEventType;
import com.company.events.CrawlerListener;
import com.company.events.MonitorEvent;
import com.company.loggers.Logger;
import com.company.loggers.ParallelLogger;
import com.example.Student;

import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Monitor {
    private final ArrayList<URL> urls;
    private final ExecutorService executor;
    private final LinkedList<Crawler> crawlers;
    private final ParallelLogger parallelLogger;

    private final List<CrawlerListener> studentAddedListeners = new CopyOnWriteArrayList<>();
    private final List<CrawlerListener> studentRemovedListeners = new CopyOnWriteArrayList<>();

    public Monitor(ArrayList<URL> urls, int amountOfThreads, Logger[] loggers) throws MonitorException {

        if (urls.size() > amountOfThreads) {
            throw new MonitorException("Wrong amoutOfThreads");
        }

        this.urls = new ArrayList<>(urls); //copy
        executor = Executors.newFixedThreadPool(amountOfThreads);
        parallelLogger = new ParallelLogger(loggers);
        crawlers = new LinkedList<>();

        for (URL url : urls) {
            Crawler c = new Crawler(url);
            c.addStudentAddedListener(new CrawlerListener() {
                @Override
                public void actionPerformed(AbstractCrawlerEvent crawlerEvent) {
                    parallelLogger.log(crawlerEvent.getType(), crawlerEvent.getStudent());
                    callAddStudentListeners(crawlerEvent.getStudent());
                }
            });

            c.addStudentRemovedListener(new CrawlerListener() {
                @Override
                public void actionPerformed(AbstractCrawlerEvent crawlerEvent) {
                    parallelLogger.log(crawlerEvent.getType(), crawlerEvent.getStudent());
                    callRemoveStudentListeners(crawlerEvent.getStudent());
                }
            });

            crawlers.add(c);
        }
    }

    private void callAddStudentListeners(Student student) {
        for (CrawlerListener crawlerListener : studentAddedListeners) {
            crawlerListener.actionPerformed(new MonitorEvent(CrawlerEventType.ADD, student));
        }
    }

    private void callRemoveStudentListeners(Student student) {
        for (CrawlerListener crawlerListener : studentRemovedListeners) {
            crawlerListener.actionPerformed(new MonitorEvent(CrawlerEventType.DELETE, student));
        }
    }

    public void addStudentAddedListener(CrawlerListener crawlerListener) {
        studentAddedListeners.add(crawlerListener);
    }

    public void addStudentRemovedListener(CrawlerListener crawlerListener) {
        studentRemovedListeners.add(crawlerListener);
    }

    public synchronized void cancel() throws InterruptedException {
        executor.shutdown();

        for (Crawler c : crawlers) {
            c.cancel();
        }

        parallelLogger.cancel();

        while (!executor.isTerminated()){
            Thread.yield();
        }
    }

    public void run() {
        executor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    crawlers.getLast().run();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

}