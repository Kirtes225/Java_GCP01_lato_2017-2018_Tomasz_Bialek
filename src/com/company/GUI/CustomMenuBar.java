package com.company.GUI;


import javafx.scene.control.Alert;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

public class CustomMenuBar extends MenuBar {

    private final Menu programMenu;
    private final Menu aboutMenu;

    private final MenuItem exitItem;
    private final MenuItem aboutItem;

    public CustomMenuBar()
    {
        this.programMenu = new Menu("Program");
        this.exitItem = new MenuItem("Close");

        this.aboutMenu = new Menu("About");
        this.aboutItem = new MenuItem("About");

        this.exitItem.setAccelerator(new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN));

        this.exitItem.setOnAction(event -> System.exit(0));
        this.aboutItem.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("About");
            alert.setHeaderText("Example program information");
            alert.setContentText("Author: anonymous!");

            alert.showAndWait();
        });

        this.aboutMenu.getItems().addAll(aboutItem);
        this.programMenu.getItems().addAll(exitItem);

        this.getMenus().addAll(programMenu, aboutMenu);
    }

}
