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

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
			properties = new FileProperties("props");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void loginBtnClick(ActionEvent actionEvent) {
		String login = loginTextField.getText();
		String passwdHash = null;
		try {
			passwdHash = getHex_SHA_256(passwordField.getText().getBytes());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return;
		}

		int t = properties.size();

		if (!properties.containsKey(login)) {
			System.out.println("User not found.");
		} else {

			if (properties.getProperty(login).equals(passwdHash)) {
				System.out.println("PASSWD OK");

				correctPasswdListenersLock.readLock().lock();
				try {
					for (UserLoggingEventListener userLoggingEventListener : correctPasswdListeners) {
						userLoggingEventListener.loggingPerformed(new UserLoggingEvent());
					}
				} finally {
					correctPasswdListenersLock.readLock().unlock();
				}
			} else {
				System.out.println("BAD PASSWD");
			}
		}

	}

	public void createUserBtnClick(ActionEvent actionEvent) {
		String login = loginTextField.getText();
		String passwdHash;
		try {
			passwdHash = getHex_SHA_256(passwordField.getText().getBytes());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return;
		}

		if (properties.containsKey(login)) {
			System.out.println("User already exists.");
		} else {
			properties.put(login, passwdHash);
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

	private String getHex_SHA_256(byte[] input) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.update(input);
		return DatatypeConverter.printHexBinary(md.digest());
	}
}
