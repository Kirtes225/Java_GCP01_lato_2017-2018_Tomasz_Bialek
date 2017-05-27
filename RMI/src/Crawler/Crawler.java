package Crawler;

import Enums.ExtremumMode;
import Enums.OrderMode;
import Student.Student;
import Student.StudentParser;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class Crawler implements Serializable {

    private List<Student> students;
    private List<Student> tmp;
    private int iteration = 1;
    private URL url;
    private String dir = "C:\\Users\\Tomek\\Desktop\\Java-students\\students.txt";

    private List<IterationListener> iterationStartedListeners = new ArrayList<>();
    private List<IterationListener> iterationComplitedListeners = new ArrayList<>();

    private List<StudentListener> studentAddedListeners = new ArrayList<>();
    private List<StudentListener> studentRemovedListener = new ArrayList<>();

    public Crawler(URL pathURL) {
        this.url = pathURL;
    }

    public void studentAddedListener(StudentListener listener) {
        studentAddedListeners.add(listener);
    }

    public void studentRemovedListener(StudentListener listener) {
        studentRemovedListener.add(listener);
    }

    public void run() throws Exception {

        if (url == null) throw new CrawlerException("WRONG URL");

        students = StudentParser.parse(url);
        File file = new File(dir);
        tmp = StudentParser.parse(file);

        while (true) {
            FileUtils.copyURLToFile(url, file);

            System.out.print("Iteration: " + iteration + "\n");
            for (IterationListener iterationListener : iterationStartedListeners) {
                iterationListener.handle(iteration);
            }

            if (tmp == null) {
                for (Student student : students) {
                    for (StudentListener studentListener : studentAddedListeners) {
                        studentListener.handle(student);
                    }
                }
            }

            if (students.equals(tmp)) {
                System.out.println("NOTHING CHANGED");
            } else if (!students.equals(tmp)) {
                if (students.size() > tmp.size()) {
                    students.removeAll(tmp);
                    for (Student student : students) {
                        for (StudentListener studentListener : studentAddedListeners) {
                            studentListener.handle(student);
                        }
                    }
                } else if (students.size() < tmp.size()) {
                    tmp.removeAll(students);
                    for (Student student : students) {
                        for (StudentListener studentListener : studentRemovedListener) {
                            studentListener.handle(student);
                        }
                    }
                } else if (students.size() == tmp.size()) {
                    students.removeAll(tmp);
                    postChangedStudents(students);
                }
            }

            if (tmp != null) {
                System.out.println("MARK: [" + orderByMark(ExtremumMode.MAX) + "," + orderByMark(ExtremumMode.MIN) + "]");
                System.out.println("AGE: [" + orderByAge(ExtremumMode.MAX) + "," + orderByAge(ExtremumMode.MIN) + "]");
                System.out.println("Ordered by " + OrderMode.AGE.toString().toLowerCase() + ":");
                students = orderStudents(OrderMode.AGE);
                postAllStudents(students);
            }

            for (IterationListener iterationListener : iterationComplitedListeners) {
                iterationListener.handle(iteration);
            }
            iteration++;

            tmp = StudentParser.parse(file);
            students = StudentParser.parse(url);

            Thread.sleep(10 * 1000);
        }
    }

    public List<Student> orderStudents(OrderMode mode) {
        tmp = students;
        if (mode == OrderMode.MARK) {
            tmp.sort(Student.Comparators.MARK);
        } else if (mode == OrderMode.AGE) {
            tmp.sort(Student.Comparators.AGE);
        } else if (mode == OrderMode.FIRST_NAME) {
            tmp.sort(Student.Comparators.FIRST_NAME);
        } else if (mode == OrderMode.SECOND_NAME) {
            tmp.sort(Student.Comparators.SECOND_NAME);
        }
        return tmp;
    }


    public double orderByMark(ExtremumMode mode) {
        tmp = orderStudents(OrderMode.MARK);
        if (mode == ExtremumMode.MAX) {
            return tmp.get(0).getMark();
        } else {
            return tmp.get(tmp.size() - 1).getMark();
        }
    }

    public int orderByAge(ExtremumMode mode) {
        tmp = orderStudents(OrderMode.AGE);
        if (mode == ExtremumMode.MAX) {
            return tmp.get(0).getAge();
        } else {
            return tmp.get(tmp.size() - 1).getAge();
        }
    }

    public String orderByFirstName(ExtremumMode mode) {
        tmp = orderStudents(OrderMode.FIRST_NAME);
        if (mode == ExtremumMode.MAX) {
            return tmp.get(0).getFirstName();
        } else {
            return tmp.get(tmp.size() - 1).getFirstName();
        }
    }

    public String orderBySecondName(ExtremumMode mode) {
        tmp = orderStudents(OrderMode.SECOND_NAME);
        if (mode == ExtremumMode.MAX) {
            return tmp.get(0).getFirstName();
        } else {
            return tmp.get(tmp.size() - 1).getFirstName();
        }
    }

    public void postAllStudents(List<Student> studentList) {
        for (Student student : studentList) {
            System.out.println(student.getMark() + " " +
                    student.getFirstName() + " " + student.getLastName() + " " + student.getAge());
        }
    }

    public void postAddedStudents(List<Student> studentList) {
        for (Student student : studentList) {
            System.out.println("ADDED: " + student.getMark() + " " +
                    student.getFirstName() + " " + student.getLastName() + " " + student.getAge());
        }
    }

    public void postRemovedStudents(List<Student> studentList) {
        for (Student student : studentList) {
            System.out.println("REMOVED " + student.getMark() + " " +
                    student.getFirstName() + " " + student.getLastName() + " " + student.getAge());
        }
    }

    public void postChangedStudents(List<Student> studentList) {
        for (Student student : studentList) {
            System.out.println("DATA CHANGED: " + student.getMark() + " " +
                    student.getFirstName() + " " + student.getLastName() + " " + student.getAge());
        }
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }
}
