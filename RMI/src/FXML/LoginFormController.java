package FXML;

import Client.Client;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginFormController {

    Stage stage;
    Scene sceneNewUser;
    Scene sceneCrawler;

    @FXML
    Button exitButton;
    @FXML
    Button loginButton;
    @FXML
    Button newUserButton;
    @FXML
    TextField nameTextField;
    @FXML
    PasswordField passwordField;

    @FXML
    public void exitButtonClick(){
        System.exit(0);
    }

    @FXML
    public void loginButtonClick(){
        stage.setScene(sceneCrawler);
        Client.running = false;
        stage.show();
    }

    @FXML
    public void newUserButtonClick(){
        stage.setScene(sceneNewUser);
        stage.show();
    }

    public Stage getStage() {
        return stage;
    }
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    public void setSceneNewUser(Scene scene) {
        this.sceneNewUser = scene;
    }
    public void setSceneCrawler(Scene scene) { this.sceneCrawler = scene; }
    public Scene getSceneNewUser() {
        return sceneNewUser;
    }
    public Scene getSceneCrawler() {
        return sceneCrawler;
    }

    @FXML
    public void initialize(){

    }

}
