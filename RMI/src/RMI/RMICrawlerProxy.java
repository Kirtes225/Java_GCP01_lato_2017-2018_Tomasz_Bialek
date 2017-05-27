package RMI;
import Crawler.*;
import Enums.ExtremumMode;
import Enums.OrderMode;
import Student.Student;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class RMICrawlerProxy extends UnicastRemoteObject implements InterfaceRMICrawlerProxy, Serializable
{
    private Crawler crawler;

    @Override
    public void run(){
        try {
            crawler.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public RMICrawlerProxy() throws RemoteException, MalformedURLException {
        try {
             crawler = new Crawler(new URL("http://home.agh.edu.pl/~ggorecki/IS_Java/students.txt"));
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void studentAddedListener(StudentListener listener) {
        crawler.studentAddedListener(listener);
    }

    @Override
    public void studentRemovedListener(StudentListener listener) {
        crawler.studentRemovedListener(listener);
    }

    @Override
    public List<Student> orderStudents(OrderMode orderMode) {
        return crawler.orderStudents(orderMode);
    }

    @Override
    public double orderByMark(ExtremumMode extremumMode) {
        return crawler.orderByMark(extremumMode);
    }

    @Override
    public int orderByAge(ExtremumMode extremumMode) {
        return crawler.orderByAge(extremumMode);
    }

    @Override
    public String orderByFirstName(ExtremumMode extremumMode){
        return crawler.orderByFirstName(extremumMode);

    }

    @Override
    public String orderBySecondName(ExtremumMode extremumMode){
        return crawler.orderBySecondName(extremumMode);
    }

    @Override
    public List<Student> getStudents() {
        return crawler.getStudents();
    }


    @Override
    public void setStudents(List<Student> studentList) {
        crawler.setStudents(studentList);
    }


    @Override
    public void postChangedStudents(List<Student> studentList) {
        crawler.postChangedStudents(studentList);
    }

    @Override
    public void postRemovedStudents(List<Student> studentList) {
        crawler.postRemovedStudents(studentList);
    }

    @Override
    public void postAddedStudents(List<Student> studentList) {
        crawler.postAddedStudents(studentList);
    }

    @Override
    public void postAllStudents(List<Student> studentList) {
        crawler.postAllStudents(studentList);
    }
}
