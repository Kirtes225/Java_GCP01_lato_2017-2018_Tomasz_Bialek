package com.example;

public class Student implements Cloneable{
    private double mark;
    private String firstName;
    private String lastName;
    private int age;

    public double getMark() {
        return this.mark;
    }

    public void setMark(double mark) {
        this.mark = mark;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return this.age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;

        result = prime * result + age;
        result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
        result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());

        long temp;
        temp = Double.doubleToLongBits(mark);
        result = prime * result + (int) (temp ^ (temp >>> 32));

        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (obj == null)
            return false;

        if (getClass() != obj.getClass())
            return false;

        Student other = (Student) obj;

        if (age != other.age)
            return false;

        if (firstName == null) {
            if (other.firstName != null)
                return false;
        } else if (!firstName.equals(other.firstName))
            return false;

        if (lastName == null) {
            if (other.lastName != null)
                return false;
        } else if (!lastName.equals(other.lastName))
            return false;

        return Double.doubleToLongBits(mark) == Double.doubleToLongBits(other.mark);
    }

    public Student clone() throws CloneNotSupportedException {
        return (Student) super.clone();
    }

    @Override
    public String toString() {
        return "Student{" +
                "mark=" + mark +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", age=" + age +
                '}';
    }
}