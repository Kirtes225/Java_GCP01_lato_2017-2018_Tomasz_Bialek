package com.company.fxml;

import com.company.events.UserLoggingEvent;
import com.company.events.UserLoggingEventListener;
import com.company.utils.FileProperties;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import net.jcip.annotations.GuardedBy;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LoginPanelController implements Initializable {
	private final ReentrantReadWriteLock correctPasswdListenersLock = new ReentrantReadWriteLock();
	@GuardedBy("correctPasswdListenersLock")
	private final ArrayList<UserLoggingEventListener> correctPasswdListeners = new ArrayList<>();
	@FXML
	public Label loginLabel;
	@FXML
	public TextField loginTextField;
	@FXML
	public PasswordField passwordField;
	private FileProperties properties;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			properties = new FileProperties("users");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void loginBtnClick(ActionEvent actionEvent) {
		String login = null;
		String passwdHash = null;
		try {
			login = loginTextField.getText();
			passwdHash = passwordField.getText();
		} catch (Exception e) {
			e.printStackTrace();
			return;

		}

		if (!properties.containsKey(login)) {
			System.out.println("User not found.");
		} else {

			if (properties.getProperty(login).equals(passwdHash)) {
				System.out.println("Correct password");

				correctPasswdListenersLock.readLock().lock();
				try {
					for (UserLoggingEventListener userLoggingEventListener : correctPasswdListeners) {
						userLoggingEventListener.loggingPerformed(new UserLoggingEvent());
					}
				} finally {
					correctPasswdListenersLock.readLock().unlock();
				}
			} else {
				System.out.println("Incorrect password");
			}
		}

	}

	public void createUserBtnClick(ActionEvent actionEvent) {
		String login = loginTextField.getText();
		String password = passwordField.getText();

		if (properties.containsKey(login)) {
			System.out.println("User already exists.");
		} else {
			properties.put(login, password);
			properties.saveToFile();
		}

	}

	public void addCorrectPasswdListener(UserLoggingEventListener userLoggingEventListener) {
		correctPasswdListenersLock.writeLock().lock();
		try {
			correctPasswdListeners.add(userLoggingEventListener);
		} finally {
			correctPasswdListenersLock.writeLock().unlock();
		}
	}

}
