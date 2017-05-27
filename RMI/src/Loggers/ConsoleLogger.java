package Loggers;

import Student.Student;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ConsoleLogger extends UnicastRemoteObject implements Logger {

    public ConsoleLogger() throws RemoteException {
    }

    @Override
    public void log(String status, Student student) throws RemoteException{
        if(student == null)
            System.out.print(status);
        else
            System.out.println(status + ": " + student.toString());
    }
}
