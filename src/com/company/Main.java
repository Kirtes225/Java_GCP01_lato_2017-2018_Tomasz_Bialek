package com.company;

import com.company.events.AbstractCrawlerEvent;
import com.company.events.CrawlerListener;
import com.company.events.UserLoggingEvent;
import com.company.events.UserLoggingEventListener;
import com.company.fxml.LoginPanelController;
import com.company.fxml.MainController;
import com.company.loggers.CompressedLogger;
import com.company.loggers.GUILogger;
import com.company.loggers.Logger;
import com.company.loggers.TextLogger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

public class Main extends Application {
    public static final Date LAUNCH_DATE = new Date();

    private static String[] arguments;

    public static void main(String[] args) throws Exception {
        arguments = args;
        launch(args);
    }

    private void userLogged(Stage stage) {
        FXMLLoader mainScreenLoader = new FXMLLoader(Main.class.getResource("fxml/main.fxml"));
        try {
            mainScreenLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        MainController mainController = mainScreenLoader.getController();
        Parent root = mainScreenLoader.getRoot();

        stage.setScene(new Scene(root, 600, 480));
        stage.show();

        mainLogic(mainController);
    }

    private void mainLogic(MainController mainController) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                final Logger[] loggers = {new GUILogger(mainController), new TextLogger(new File("ss")), new CompressedLogger
                        ("zippp.zip")};

                ArrayList<URL> urls = new ArrayList<URL>() {{
                    try {
                        add(new URL("http://home.agh.edu.pl/~ggorecki/IS_Java/students.txt"));
                        add(new URL("http://home.agh.edu.pl/~ggorecki/IS_Java/students.txt"));
                        add(new URL("http://home.agh.edu.pl/~ggorecki/IS_Java/students.txt"));
                        add(new URL("http://home.agh.edu.pl/~ggorecki/IS_Java/students.txt"));
                        add(new URL("http://home.agh.edu.pl/~ggorecki/IS_Java/students.txt"));
                        add(new URL("http://home.agh.edu.pl/~ggorecki/IS_Java/students.txt"));
                        add(new URL("http://home.agh.edu.pl/~ggorecki/IS_Java/students.txt"));
                        add(new URL("http://home.agh.edu.pl/~ggorecki/IS_Java/students.txt"));
                        add(new URL("http://home.agh.edu.pl/~ggorecki/IS_Java/students.txt"));
                        add(new URL("http://home.agh.edu.pl/~ggorecki/IS_Java/students.txt"));
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }};

                Monitor monitor;
                try {
                    monitor = new Monitor(urls, 10, loggers);
                } catch (MonitorException e) {
                    e.printStackTrace();
                    return;
                }

                monitor.addStudentAddedListener(new CrawlerListener() {
                    @Override
                    public void actionPerformed(AbstractCrawlerEvent crawlerEvent) {
                        System.out.println("Student Added");
                    }
                });

                monitor.run();

                System.out.println("Monitor is running");

                try {
                    Thread.sleep(5*1000);
                    monitor.cancel();
                    System.exit(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println("Monitor isn't running");
            }
        });

        t.start();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader firstScreenLoader = new FXMLLoader(Main.class.getResource("fxml/loginPanel.fxml"));
        try {
            firstScreenLoader.load();
        } catch (IOException e) {
            e.printStackTrace();

            return;
        }

        Parent root = firstScreenLoader.getRoot();
        LoginPanelController loginPanelController = firstScreenLoader.getController();
        loginPanelController.addCorrectPasswdListener(new UserLoggingEventListener() {
            @Override
            public void loggingPerformed(UserLoggingEvent userLoggingEvent) {
                userLogged(primaryStage);
            }
        });

        primaryStage.setScene(new Scene(root, 600, 480));
        primaryStage.show();
    }
}