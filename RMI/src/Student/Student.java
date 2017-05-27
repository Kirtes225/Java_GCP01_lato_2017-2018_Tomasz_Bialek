package Student;

import java.io.Serializable;
import java.util.Comparator;

public class Student implements Serializable {

    private double mark;
    private String firstName, lastName;
    private int age;

    public Student(){
        this.mark = 0.;
        this.firstName = "";
        this.lastName = "";
        this.age = 0;
    }
    public Student(double mark, String firstName, String lastName, int age){
        this.mark = mark;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }

    public double getMark() {
        return mark;
    }
    public void setMark(double mark) {
        this.mark = mark;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student)) return false;

        Student student = (Student) o;

        if (Double.compare(student.mark, mark) != 0) return false;
        if (age != student.age) return false;
        if (firstName != null ? !firstName.equals(student.firstName) : student.firstName != null) return false;
        return lastName != null ? lastName.equals(student.lastName) : student.lastName == null;

    }
    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(mark);
        result = (int) (temp ^ (temp >>> 32));
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + age;
        return result;
    }

    @Override
    public String toString() {
        return  mark + " " + firstName + " " + lastName + " " + age;
    }

    public static class Comparators{
        public static final Comparator<Student> AGE = (Student s1, Student s2) -> Integer.compare(s1.age, s2.age);
        public static final Comparator<Student> MARK = (Student s1, Student s2) -> Double.compare(s1.mark, s2.mark);
        public static final Comparator<Student> FIRST_NAME = (Student s1, Student s2) -> s1.firstName.compareTo(s2.firstName);
        public static final Comparator<Student> SECOND_NAME = (Student s1, Student s2) -> s1.lastName.compareTo(s2.lastName);
    }
}

