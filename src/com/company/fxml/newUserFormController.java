package com.company.fxml;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.util.LinkedList;

public class newUserFormController {

    @FXML Label infoLabel;
    @FXML Button clearButton;
    @FXML Button cancelButton;
    @FXML TextField loginTextField;
    @FXML TextField passwordTextField;
    @FXML TextField ageTextField;
    @FXML TextField sexTextField;
    @FXML TextField addressTextField;

    private Stage stage;
    private Scene loginScene;
    private LinkedList<User> users;

    private String dir = "C:\\Users\\Tomek\\Desktop\\Java-students\\users.txt";
    FileWriter fileWriter;

    @FXML
    public void newUserButtonClick(){
        User tmp = new User();
        tmp.setName(loginTextField.getText());
        tmp.setPass(passwordTextField.getText());
        tmp.setAge(ageTextField.getText());
        tmp.setSex(sexTextField.getText());
        tmp.setAdress(addressTextField.getText());
        if (!users.contains(tmp) && !tmp.getName().equals("")){
            users.add(tmp);
            clearButtonClick();
            stage.setScene(loginScene);
        } else{
            infoLabel.setText("User exists or login is empty");
        }
        //clearButtonClick();
    }

    @FXML
    public void clearButtonClick(){
        loginTextField.clear();
        passwordTextField.clear();
        sexTextField.clear();
        ageTextField.clear();
        addressTextField.clear();
    }

    @FXML
    public void cancelButtonClick(){
        clearButtonClick();
        stage.setScene(loginScene);

    }

    public void setUsers(LinkedList<User> users) {
        this.users = users;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setLoginScene(Scene loginScene) {
        this.loginScene = loginScene;
    }

    @FXML
    public void initialize(){
    }
}
