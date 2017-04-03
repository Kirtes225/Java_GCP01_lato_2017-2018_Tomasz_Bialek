package com.company.GUI;


import javafx.scene.control.Alert;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCharacterCombination;
import javafx.stage.Stage;

public class CustomMenuBar extends MenuBar {

    private final Menu programMenu;
    private final Menu aboutMenu;

    private final MenuItem exitItem;
    private final MenuItem aboutItem;

    private final MenuBar menuBar;

    public CustomMenuBar(Stage primaryStage){

        menuBar = new MenuBar();
        programMenu = new Menu("Program");
        exitItem = new MenuItem("Close");

        aboutMenu = new Menu("About");
        aboutItem = new MenuItem("About");

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

        //((VBox) scene.getRoot()).setVgrow(tabPane, Priority.ALWAYS);
        //((VBox) scene.getRoot()).getChildren().addAll(menuBar, tabPane);
    }

}
