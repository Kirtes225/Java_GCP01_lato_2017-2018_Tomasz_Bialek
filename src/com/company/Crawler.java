package com.company;

import com.example.Student;
import com.example.StudentsParser;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.net.URL;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

public class Crawler {
    private final List<CrawlerListener> studentAddedListeners = new CopyOnWriteArrayList<>();
    private final List<CrawlerListener> studentRemovedListeners = new CopyOnWriteArrayList<>();
    private final List<CrawlerListener> studentNoChangeListeners = new CopyOnWriteArrayList<>();
    private final List<CrawlerListener> iterationStartedListeners = new CopyOnWriteArrayList<>();
    private final List<CrawlerListener> iterationFinishedListeners = new CopyOnWriteArrayList<>();
    private final URL url;
    private final String outputDirectory = "C:\\Users\\Tomek\\Desktop\\Java-students\\students";
    private final Object iterationLock = new Object();
    private long iteration = 0;
    private List<Student> currentData = new CopyOnWriteArrayList<>();

    public Crawler(URL url) {
        this.url = url;
    }

    public void addIterationFinishedListeners(CrawlerListener crawlerListener) {
        iterationFinishedListeners.add(crawlerListener);
    }

    public void run() throws Exception {

        if (url == null) throw new CrawlerException("Incorrect URL");

        while (true) {
            synchronized (iterationLock) {
                listenersCall(CrawlerEventType.ITERATION_START, null, iteration); // Wywołanie listenerów
            }

            File tmpFile = new File(outputDirectory + String.valueOf(iteration));

            FileUtils.copyURLToFile(url, tmpFile);

            List<Student> previousData = currentData;
            currentData = StudentsParser.parse(tmpFile);
            currentData.sort(
                    (a, b) -> (a.getLastName() + a.getFirstName()).compareToIgnoreCase(b.getLastName() + b.getFirstName()));

            if (previousData != null && currentData != null) {
                List<Student> added = getAdded(previousData, currentData);
                List<Student> removed = getAdded(currentData, previousData);

                synchronized (iterationLock) {
                    if (added.size() == 0 && removed.size() == 0) {
                        for (Student s : currentData) {
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

            TimeUnit.SECONDS.sleep(10);

            synchronized (iterationLock) {
                iteration++;
                listenersCall(CrawlerEventType.ITERATION_END, null, iteration);
            }

        }
    }

    public List<Student> extractStudents(OrderMode mode) {
        List<Student> result = new LinkedList<>();

        Collections.copy(result, currentData);

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
                return currentData.stream().mapToDouble(Student::getMark).max().getAsDouble();
            case MIN:
                return currentData.stream().mapToDouble(Student::getMark).min().getAsDouble();

            default:
                return currentData.stream().mapToDouble(Student::getMark).max().getAsDouble();
        }
    }

    public int extractAge(ExtremumMode mode) {
        if (currentData.size() == 0) return 0;

        switch (mode) {
            case MAX:
                return currentData.stream().mapToInt(Student::getAge).max().getAsInt();
            case MIN:
                return currentData.stream().mapToInt(Student::getAge).min().getAsInt();

            default:
                return currentData.stream().mapToInt(Student::getAge).max().getAsInt();
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

    private List<Student> getAdded(List<Student> a, List<Student> b) {
        List<Student> result = new LinkedList<>();

        try {
            for (Student s : b) {
                result.add(s.clone());
            }

            result.removeAll(a);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            System.out.println("getAdded() ERROR");
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
