package com.company;

public class MonitorException extends Exception {
    private String msg;

    public MonitorException(String msg) {
        super(msg);
    }
}
