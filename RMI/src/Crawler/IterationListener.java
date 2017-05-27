package Crawler;

import java.io.Serializable;
import java.rmi.RemoteException;

@FunctionalInterface
public interface IterationListener extends Serializable {
    void handle(int iteration) throws RemoteException;
}