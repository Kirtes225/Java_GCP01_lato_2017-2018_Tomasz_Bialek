package Loggers;

import Student.*;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Logger extends Remote {
    void log(String status, Student student) throws RemoteException;
}
