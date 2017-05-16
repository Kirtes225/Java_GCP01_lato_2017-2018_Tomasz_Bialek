package com.company.Client;

public interface ClientEventChat {
    void messageReceived(String msg);
    void disconnectedFromTheServer();
}
