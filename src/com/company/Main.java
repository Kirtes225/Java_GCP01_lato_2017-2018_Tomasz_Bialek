package com.company;

import java.net.URL;

public class Main {

    public static void main(String[] args) throws Exception {

        final Logger[] loggers = {new ConsoleLogger(), new MailLogger()};

        final Crawler crawler = new Crawler(new URL("http://home.agh.edu.pl/~ggorecki/IS_Java/students.txt"));
        crawler.addIterationStartedListener(crawlerEvent -> System.out.println("Iteration: " + crawlerEvent.getIteration()));
        crawler.addStudentAddedListener(crawlerEvent -> {
            for (Logger logger : loggers) {
                logger.log("ADDED: " + crawlerEvent.getStudent().getFirstName() + " " +
                                crawlerEvent.getStudent().getLastName(),
                        crawlerEvent.getStudent());
            }
        });

        crawler.addStudentNoChangeListener(crawlerEvent -> System.out.println(crawlerEvent.getStudent().toString() +
                " : " + crawlerEvent.getType()));

        try {

            crawler.run();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Unknown error");
        }

    }
}