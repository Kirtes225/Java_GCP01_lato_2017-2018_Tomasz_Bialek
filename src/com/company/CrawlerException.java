package com.company;

public class CrawlerException extends Exception {
    private String msg;

    public CrawlerException(String msg){
        super(msg);
    }
}