package FXML;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class NewUserFormController {

    Stage stage;
    Scene scene;

    @FXML
    TextField loginTextField;
    @FXML
    PasswordField passwordField;
    @FXML
    TextField ageTextField;
    @FXML
    TextField addressTextField;
    @FXML
    TextField sexTextField;

    @FXML
    Button newUserButton;
    @FXML
    Button clearButton;
    @FXML
    Button cancelButton;

    @FXML
    public void clearButtonClick(){
        loginTextField.clear();
        passwordField.clear();
        ageTextField.clear();
        addressTextField.clear();
        sexTextField.clear();
    }

    @FXML
    public void cancelButtonClick(){
        clearButtonClick();
        stage.setScene(scene);
    }

    @FXML
    public void newUserButtonClick(){
        stage.setScene(scene);
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    @FXML
    public void initialize(){

    }

}
