package com.company;

import com.example.Student;
import com.example.StudentsParser;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.net.URL;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Crawler {
    private List<CrawlerListener> studentAddedListeners = new LinkedList<>();

    private URL url;
    private String dirFile;
    private List<Student> currentData = new LinkedList<>();
    private int iteration = 0;

    public Crawler(URL url){
        this.url = url;
        this.dirFile = "C:\\Users\\Tomek\\Desktop\\Semestr czwarty\\Programowanie w Java\\students";
    }

    public void addStudentAddedListener(CrawlerListener crawlerListener) {
        studentAddedListeners.add(crawlerListener);
    }

    private List<CrawlerListener> studentRemovedListeners = new LinkedList<>();
    public void addStudentRemovedListener(CrawlerListener crawlerListener) {
        studentRemovedListeners.add(crawlerListener);
    }

    private List<CrawlerListener> studentNoChangeListeners = new LinkedList<>();
    public void addStudentNoChangeListener(CrawlerListener crawlerListener) {
        studentNoChangeListeners.add(crawlerListener);
    }

    private List<CrawlerListener> iterationStartedListeners = new LinkedList<>();
    public void addIterationStartedListener(CrawlerListener crawlerListener) {
        iterationStartedListeners.add(crawlerListener);
    }

    private List<CrawlerListener> iterationFinishedListeners = new LinkedList<>();
    public void addIterationFinishedListeners(CrawlerListener crawlerListener) {
        iterationFinishedListeners.add(crawlerListener);
    }

    public void run() throws Exception {

        if (url == null) throw new CrawlerException("Incorrect URL");

        while (true) {
            listenersCall(CrawlerEventType.ITERATION_START, null, iteration);

            File tmp = new File(dirFile + String.valueOf(iteration));
            FileUtils.copyURLToFile(url, tmp);

            List<Student> previousData = currentData;
            currentData = StudentsParser.parse(tmp);
            currentData.sort(
                    (a, b) -> (a.getLastName() + a.getFirstName()).compareToIgnoreCase(b.getLastName() + b.getFirstName()));

            if (previousData != null && currentData != null) {
                List<Student> added = getAdded(previousData, currentData);
                List<Student> removed = getAdded(currentData, previousData);

                if (added.size() == 0 && removed.size() == 0) {
                    for (Student s : currentData) {
                        listenersCall(CrawlerEventType.NO_CHANGES, s, iteration);
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

            TimeUnit.SECONDS.sleep(10);

            iteration++;
            listenersCall(CrawlerEventType.ITERATION_END, null, iteration);
        }
    }

   @SuppressWarnings("Duplicates")
    public List<Student> extractStudents(OrderMode mode) {
        List<Student> result = new LinkedList<>();

        for (Student s : currentData) {
            try {
                result.add(s.clone());
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
                System.out.println("Clone ERROR!");
            }
        }


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

    @SuppressWarnings("Duplicates")
    public double extractMark(ExtremumMode mode) {
        List<Student> result = new LinkedList<>();

        for (Student s : currentData) {
            try {
                result.add(s.clone());
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
                System.out.println("Clone ERROR!");
            }
        }


        result.sort(Comparator.comparingDouble(Student::getMark));

        if (mode.equals(ExtremumMode.MAX)) return result.get(result.size() - 1).getMark();

        return result.get(0).getMark();
    }

    @SuppressWarnings("Duplicates")
    public int extractAge(ExtremumMode mode) {
        List<Student> result = new LinkedList<>();

        for (Student s : currentData) {
            try {
                result.add(s.clone());
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
                System.out.println("Clone ERROR!");
            }
        }


        result.sort(Comparator.comparingInt(Student::getAge));

        if (mode.equals(ExtremumMode.MAX)) return result.get(result.size() - 1).getAge();

        return result.get(0).getAge();
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

            case NO_CHANGES:
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
            System.out.println("Function getAdded() ERROR");
        }

        return result;
    }
}
