package com.company.fxml;

import com.company.events.CrawlerEventType;

import com.example.Student;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCharacterCombination;
import javafx.scene.layout.AnchorPane;
import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@ThreadSafe
public class MainController implements Initializable {
	private final ReentrantReadWriteLock studentsReadWriteLock = new ReentrantReadWriteLock();
	@GuardedBy("studentsReadWriteLock")
	private final ObservableList<Student> students = FXCollections.observableArrayList();
	@FXML
	private AnchorPane histogramTab;
	@FXML
	private MenuItem exit;
	@FXML
	private AnchorPane studentsTab;
	@FXML
	private AnchorPane logTab;
	@FXML
	private StudentsTabController studentsTabController;
	@FXML
	private LogController logTabController;
	@FXML
	private HistogramController histogramTabController;

	public ObservableList<Student> getStudentsCopy() {
		studentsReadWriteLock.readLock().lock();
		try {
			return FXCollections.observableArrayList(students);
		} finally {
			studentsReadWriteLock.readLock().unlock();
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		exit.setAccelerator(KeyCharacterCombination.keyCombination("Ctrl+C"));
	}


	public void addStudent(Student student) { // todo Czy trzeba synchronizować?
		studentsReadWriteLock.writeLock().lock();
		try {
			students.add(student);
		} finally {
			studentsReadWriteLock.writeLock().unlock();
		}

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				studentsTabController.setStudents(getStudentsCopy());
				logTabController.addData(CrawlerEventType.ADD, student);
				histogramTabController.setStudents(getStudentsCopy());
			}
		});
	}

	public void removeStudent(Student student) {
		studentsReadWriteLock.writeLock().lock();
		try {
			students.remove(student);
		} finally {
			studentsReadWriteLock.writeLock().unlock();
		}

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				studentsTabController.setStudents(getStudentsCopy());
				logTabController.addData(CrawlerEventType.DELETE, student);
				histogramTabController.setStudents(getStudentsCopy());
			}
		});
	}

	public void close(ActionEvent actionEvent) {
		System.exit(0);
	}

	public void about(ActionEvent actionEvent) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("About");
		alert.setHeaderText("Example program information");
		alert.setContentText("I have a great message for you!");

		alert.showAndWait();
	}
}