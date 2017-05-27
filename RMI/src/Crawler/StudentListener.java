package Crawler;

import Student.*;

import java.io.Serializable;
import java.rmi.RemoteException;

@FunctionalInterface
public interface StudentListener extends Serializable{
    void handle(Student student) throws RemoteException;
}
