package com.company;

import com.example.Student;
import com.example.StudentsParser;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.net.URL;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class Crawler {
    private final List<CrawlerListener> studentAddedListeners = new LinkedList<>();
    private final List<CrawlerListener> studentRemovedListeners = new LinkedList<>();
    private final List<CrawlerListener> studentNoChangeListeners = new LinkedList<>();
    private final List<CrawlerListener> iterationStartedListeners = new LinkedList<>();
    private final List<CrawlerListener> iterationFinishedListeners = new LinkedList<>();
    private final URL url;
    private final String outputDirectory;
    private List<Student> currentData = new LinkedList<>();
    private long iteration = 0;

    public Crawler(URL url, String outputDirectory) {
        this.url = url;
        this.outputDirectory = outputDirectory;
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

    public void addIterationFinishedListeners(CrawlerListener crawlerListener) {
        iterationFinishedListeners.add(crawlerListener);
    }

    public void run() throws Exception {
        synchronized (this) {
            if (url == null) throw new CrawlerException("Url is null");

            int keepGoing = 2;
            while (keepGoing > 0) {
                listenersCall(CrawlerEventType.ITERATION_START, null, iteration); // Wywołanie listenerów

                File tmpFile = new File(outputDirectory + String.valueOf(iteration)); // Tworzymy obiekt pliku "tmp"
                FileUtils.copyURLToFile(url, tmpFile); // Wczytujemy dane z url to pliku

                List<Student> previousData = currentData;
                currentData = StudentsParser.parse(tmpFile); // Parsujemy dane z pliku do currentData
                currentData.sort(
                        (a, b) -> (a.getLastName() + a.getFirstName()).compareToIgnoreCase(b.getLastName() + b.getFirstName())); // Zawsze posortowane dane w liście ułatwią późniejsze porównywanie zmian

                if (previousData != null && currentData != null) {
                    List<Student> added = getAdded(previousData, currentData);
                    List<Student> removed = getAdded(currentData, previousData);

                    if (added.size() == 0 && removed.size() == 0) { // Jeśli wielkości list currentData i previousData są takie same, to mamy pewność, że żaden student nie został dodani ani usunięty
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

                Thread.sleep(1 * 1000); // Usypiamy wątek na określony czas (milisekundy)

                iteration++; // Inkrementacja licznika iteracji
                keepGoing--;
                listenersCall(CrawlerEventType.ITERATION_END, null, iteration);
            }
        }
    }

    @SuppressWarnings("Duplicates")
    public List<Student> extractStudents(OrderMode mode) {
        List<Student> result = new LinkedList<>();
        synchronized (this) {
            for (Student s : currentData) {
                try {
                    result.add(s.clone());
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                    System.out.println("Clone ERROR!");
                }
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
        synchronized (this) {
            for (Student s : currentData) {
                try {
                    result.add(s.clone());
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                    System.out.println("Clone ERROR!");
                }
            }
        }

        result.sort(Comparator.comparingDouble(Student::getMark));

        if (mode.equals(ExtremumMode.MAX)) return result.get(result.size() - 1).getMark();

        return result.get(0).getMark();
    }

    @SuppressWarnings("Duplicates")
    public int extractAge(ExtremumMode mode) {
        List<Student> result = new LinkedList<>();
        synchronized (this) {
            for (Student s : currentData) {
                try {
                    result.add(s.clone());
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                    System.out.println("Clone ERROR!");
                }
            }
        }

        result.sort(Comparator.comparingInt(Student::getAge));

        if (mode.equals(ExtremumMode.MAX)) return result.get(result.size() - 1).getAge();

        return result.get(0).getAge();
    }

    private void listenersCall(CrawlerEventType type, Student student, long iteration) {
        synchronized (this) {
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
}
