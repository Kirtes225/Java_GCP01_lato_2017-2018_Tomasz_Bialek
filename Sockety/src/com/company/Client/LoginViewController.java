package com.company.Client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginViewController {

    @FXML private TextField fieldUsername, fieldNewUsername, fieldServerIP, fieldServerPort;
    @FXML private PasswordField fieldPassword, fieldNewPassword;
    @FXML private Label labelNewError;
    private Client client;
    private ClientEventLogin cel = new ClientEventLogin()
    {
        @Override
        public void invalidLogin()
        {
            labelNewError.setText( "Invalid username or password!" );
        }

        @Override
        public void successfulLogin( ActionEvent event )
        {
            ( (Node) event.getSource() ).getScene().getWindow().hide();
            FXMLLoader fxmlLoader = new FXMLLoader( getClass().getResource("/FXML/ChatView.fxml") );

            Parent parent = null;
            try {parent = fxmlLoader.load();}
            catch( IOException e ) {e.printStackTrace();}
            ChatController controller = fxmlLoader.getController();
            controller.init( client );

            Stage stage = new Stage();
            Scene scene = new Scene( parent );

            stage.setScene( scene );
            stage.setMinWidth( 400 );
            stage.setMinHeight( 400 );
            stage.show();
        }

        @Override
        public void newAccountCreated()
        {
            labelNewError.setText( "A new account has been created successfully" );
        }

        @Override
        public void usernameUsed()
        {
            labelNewError.setText( "This user already exists" );
        }


        @Override
        public void cannotConnectLogin()
        {
            labelNewError.setText( "Connection to the server failed" );
        }


        @Override
        public void cannotConnectRegister()
        {
            labelNewError.setText( "Connection to the server failed" );
        }
    };

    @FXML
    public void btnLogInAction( ActionEvent event )
    {
        String username = fieldUsername.getText();
        String password = fieldPassword.getText();
        String ip = fieldServerIP.getText();
        int port = Integer.parseInt( fieldServerPort.getText() );

        client = new Client( username, password );
        client.setClientEventLogin( cel );
        if( client.connect( ip, port ) )
            client.login( event );
    }

    @FXML
    public void btnSaveAction()
    {
        String username = fieldNewUsername.getText();
        String password = fieldNewPassword.getText();
        String ip = fieldServerIP.getText();
        int port = Integer.parseInt( fieldServerPort.getText() );

        client = new Client( username, password );
        client.setClientEventLogin( cel );
        if( client.connect( ip, port ) )
        {
            client.register();
        }

        fieldNewUsername.clear();
        fieldNewPassword.clear();
    }

}
