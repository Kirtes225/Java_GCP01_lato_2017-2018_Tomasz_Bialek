package com.company;

import com.company.GUI.CustomTabPane;
import com.company.GUI.LogTab;
import com.company.loggers.ConsoleLogger;
import com.company.loggers.GUILogger;
import com.company.loggers.Logger;
import com.company.loggers.MailLogger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCharacterCombination;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.net.URL;


public class Main extends Application {
    private static String[] arguments;

    public static void main(String[] args) throws Exception {
        arguments = args;
        launch(args);

    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        final Logger[] loggers = new Logger[]{
                new ConsoleLogger(),
                new MailLogger(),
                new GUILogger(new LogTab())
        };

        Scene scene = new Scene(new VBox(), 400, 400);

        MenuBar menuBar = new MenuBar();
        Menu programMenu = new Menu("Program");
        MenuItem exitItem = new MenuItem("Close");
        Menu aboutMenu = new Menu("About");
        MenuItem aboutItem = new MenuItem("About");

        exitItem.setAccelerator(KeyCharacterCombination.keyCombination("Ctrl+C"));
        exitItem.setOnAction(event -> System.exit(0));
        aboutItem.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("About");
            alert.setHeaderText("Example program information");
            alert.setContentText("Author: anonymous!");

            alert.showAndWait();
        });

        aboutMenu.getItems().addAll(aboutItem);
        programMenu.getItems().addAll(exitItem);

        menuBar.getMenus().addAll(programMenu, aboutMenu);
        CustomTabPane tabPane = new CustomTabPane();

        ((VBox) scene.getRoot()).setVgrow(tabPane, Priority.ALWAYS);
        ((VBox) scene.getRoot()).getChildren().addAll(menuBar, tabPane);

        scene.setFill(Color.RED);
        primaryStage.setScene(scene);
        primaryStage.setMinHeight(320);
        primaryStage.setMinWidth(320);
        primaryStage.show();

        GUILogger guiLogger = new GUILogger(tabPane.getLogTab());

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final Crawler crawler = new Crawler(new URL("http://home.agh.edu.pl/~ggorecki/IS_Java/students.txt"));

                    crawler.addStudentAddedListener(new CrawlerListener() {
                        @Override
                        public void actionPerformed(CrawlerEvent crawlerEvent) {
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    tabPane.addStudent(crawlerEvent.getStudent());
                                    guiLogger.log(crawlerEvent.getType().toString(), crawlerEvent.getStudent());
                                }
                            });
                        }
                    });

                    crawler.addStudentRemovedListener(new CrawlerListener() {
                        @Override
                        public void actionPerformed(CrawlerEvent crawlerEvent) {
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    tabPane.removeStudent(crawlerEvent.getStudent());
                                    guiLogger.log(crawlerEvent.getType().toString(), crawlerEvent.getStudent());
                                }
                            });
                        }
                    });
                    crawler.run();
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Error");
                }

            }
        }).start();
    }
}