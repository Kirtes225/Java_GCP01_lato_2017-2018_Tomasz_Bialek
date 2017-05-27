package Loggers;

import Client.Client;
import Student.Student;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class GUILogger extends UnicastRemoteObject implements Logger {

    public GUILogger() throws RemoteException {
    }
    @Override
    public synchronized void log(String status, Student student) throws RemoteException{
        if(String.valueOf(status).equals("ADDED ")) {
            Client.crawlerController.addStudent(student);
            Client.crawlerController.updateChartAdd(student.getMark());
            Client.crawlerController.addedStudentInfo(student);
        }
        if(String.valueOf(status).equals("DELETE ")){
            Client.crawlerController.removeStudent(student);
            Client.crawlerController.updateChartRemove(student.getMark());
            Client.crawlerController.removedStudentInfo(student);
        }
    }
}
