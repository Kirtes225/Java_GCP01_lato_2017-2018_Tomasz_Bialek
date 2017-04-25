package com.company;

import com.company.events.CrawlerEvent;
import com.company.events.CrawlerEventType;
import com.company.events.CrawlerListener;
import com.example.Student;
import com.example.StudentsParser;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class Crawler {
    private final List<CrawlerListener> studentAddedListeners = Collections.synchronizedList(new ArrayList<>());
    private final List<CrawlerListener> studentRemovedListeners = Collections.synchronizedList(new ArrayList<>());
    private final List<CrawlerListener> studentNoChangeListeners = Collections.synchronizedList(new ArrayList<>());
    private final List<CrawlerListener> iterationStartedListeners = Collections.synchronizedList(new ArrayList<>());
    private final List<CrawlerListener> iterationFinishedListeners = Collections.synchronizedList(new ArrayList<>());
    private final URL url;
    private final String outputDirectory = "C:\\Users\\Tomek\\Desktop\\Java-students\\";
    private final Object iterationLock = new Object();
    private long iteration = 0;
    private List<Student> data = Collections.synchronizedList(new ArrayList<>());
    private AtomicBoolean keepGoing = new AtomicBoolean(true);

    public Crawler(URL url) {
        this.url = url;
    }

    public AtomicBoolean getKeepGoing() {
        return keepGoing;
    }

    public void addIterationFinishedListeners(CrawlerListener crawlerListener) {
        iterationFinishedListeners.add(crawlerListener);
    }

    public void run() throws Exception {

        if (url == null) throw new CrawlerException("Url is null");

        while (keepGoing.get()) {
            synchronized (iterationLock) {
                listenersCall(CrawlerEventType.ITERATION_START, null, iteration); // Wywołanie listenerów
            }

            File tmpFile = new File (outputDirectory, "crawler"+iteration);
            FileUtils.copyURLToFile(url, tmpFile); // Dane url -> plik

            List<Student> previousData = new ArrayList<>(data);
            data = StudentsParser.parse(tmpFile); // Plik z danymi parsowanie -> data

            data.sort(
                    (a, b) -> (a.getLastName() + a.getFirstName()).compareToIgnoreCase(b.getLastName() + b.getFirstName()));

            if (previousData != null && data != null) {
                List<Student> added = getAdded(previousData, data);
                List<Student> removed = getRemoved(previousData, data);

                synchronized (iterationLock) {
                    if (added.size() == 0 && removed.size() == 0) {
                        for (Student s : data) {
                            listenersCall(CrawlerEventType.NO_CHANGE, s, iteration);
                        }
                    } else {
                        for (Student s : added) {
                            listenersCall(CrawlerEventType.ADD, s, iteration);
                        }

                        for (Student s : removed) {
                            listenersCall(CrawlerEventType.DELETE, s, iteration);
                        }
                    }
                }
            }

            Thread.sleep(500); //

            synchronized (iterationLock) {
                iteration++; // Inkrementacja licznika iteracji
                listenersCall(CrawlerEventType.ITERATION_END, null, iteration);
            }
        }
    }

    public synchronized void cancel() {
        keepGoing.set(false);
    }

    public List<Student> extractStudents(OrderMode mode) {
        List<Student> result = new LinkedList<>();

        Collections.copy(result, data);

        switch (mode) {
            case AGE:
                result.sort(Comparator.comparingInt(Student::getAge));
                break;

            case MARK:
                result.sort(Comparator.comparingDouble(Student::getMark));
                break;

            case FIRST_NAME:
                result.sort(Comparator.comparing(Student::getFirstName));
                break;

            case LAST_NAME:
                result.sort(Comparator.comparing(Student::getLastName));
                break;
        }

        return result;
    }

    public double extractMark(ExtremumMode mode) {
        switch (mode) {
            case MAX:
                return data.stream().mapToDouble(Student::getMark).max().getAsDouble();
            case MIN:
                return data.stream().mapToDouble(Student::getMark).min().getAsDouble();

            default:
                return data.stream().mapToDouble(Student::getMark).max().getAsDouble();
        }
    }

    public int extractAge(ExtremumMode mode) {
        if (data.size() == 0) return 0;

        switch (mode) {
            case MAX:
                return data.stream().mapToInt(Student::getAge).max().getAsInt();
            case MIN:
                return data.stream().mapToInt(Student::getAge).min().getAsInt();

            default:
                return data.stream().mapToInt(Student::getAge).max().getAsInt();
        }
    }

    private void listenersCall(CrawlerEventType type, Student student, long iteration) {

        switch (type) {
            case ADD:
                for (CrawlerListener crawlerListener : studentAddedListeners)
                    crawlerListener.actionPerformed(new CrawlerEvent(type, student, iteration));
                break;

            case DELETE:
                for (CrawlerListener crawlerListener : studentRemovedListeners)
                    crawlerListener.actionPerformed(new CrawlerEvent(type, student, iteration));
                break;

            case NO_CHANGE:
                for (CrawlerListener crawlerListener : studentNoChangeListeners)
                    crawlerListener.actionPerformed(new CrawlerEvent(type, student, iteration));
                break;

            case ITERATION_START:
                for (CrawlerListener crawlerListener : iterationStartedListeners)
                    crawlerListener.actionPerformed(new CrawlerEvent(type, null, iteration));
                break;

            case ITERATION_END:
                for (CrawlerListener crawlerListener : iterationFinishedListeners)
                    crawlerListener.actionPerformed(new CrawlerEvent(type, null, iteration));
                break;
        }
    }

    private List<Student> getAdded(List<Student> prevData, List<Student> currentData) {
        List<Student> result = new LinkedList<>();

        for (Student s : currentData) {
            if (!prevData.contains(s)) {
                result.add(s);
            }
        }

        return result;
    }

    private List<Student> getRemoved(List<Student> prevData, List<Student> currentData) {
        List<Student> result = new LinkedList<>();

        for (Student s : prevData) {
            if (!currentData.contains(s)) {
                result.add(s);
            }
        }

        return result;

    }

    public void addStudentAddedListener(CrawlerListener crawlerListener) {
        studentAddedListeners.add(crawlerListener);
    }

    public void addStudentRemovedListener(CrawlerListener crawlerListener) {
        studentRemovedListeners.add(crawlerListener);
    }

    public void addStudentNoChangeListener(CrawlerListener crawlerListener) {
        studentNoChangeListeners.add(crawlerListener);
    }

    public void addIterationStartedListener(CrawlerListener crawlerListener) {
        iterationStartedListeners.add(crawlerListener);
    }
}