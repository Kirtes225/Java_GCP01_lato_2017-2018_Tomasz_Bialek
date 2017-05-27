package RMI;

import Crawler.StudentListener;
import Enums.ExtremumMode;
import Enums.OrderMode;
import Student.Student;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface InterfaceRMICrawlerProxy extends Remote
{
    void studentAddedListener(StudentListener listener)throws RemoteException;
    void studentRemovedListener(StudentListener listener)throws RemoteException;
    void run() throws RemoteException;

    List<Student> orderStudents(OrderMode mode) throws RemoteException;
    double orderByMark(ExtremumMode mode)throws RemoteException;
    int orderByAge(ExtremumMode mode)throws RemoteException;
    String orderByFirstName(ExtremumMode mode) throws RemoteException;
    String orderBySecondName(ExtremumMode mode) throws RemoteException;


    void postAllStudents(List<Student> l)throws RemoteException;
    void postAddedStudents(List<Student> l)throws RemoteException;
    void postRemovedStudents(List<Student> l)throws RemoteException;
    void postChangedStudents(List<Student> l)throws RemoteException;

    List<Student> getStudents()throws RemoteException;
    void setStudents(List<Student> students)throws RemoteException;
}
