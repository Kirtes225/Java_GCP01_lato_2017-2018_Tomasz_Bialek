package com.company.fxml;

import com.example.Student;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.net.URL;
import java.util.ResourceBundle;

@ThreadSafe
public class StudentsTabController implements Initializable {

	@FXML
	public TableView tableView;
	@FXML
	private TableColumn markColumn;
	@FXML
	private TableColumn firstNameColumn;
	@FXML
	private TableColumn lastNameColumn;
	@FXML
	private TableColumn ageColumn;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		markColumn.setCellValueFactory(new PropertyValueFactory<Student, String>("mark"));
		firstNameColumn.setCellValueFactory(new PropertyValueFactory<Student, String>("firstName"));
		lastNameColumn.setCellValueFactory(new PropertyValueFactory<Student, String>("lastName"));
		ageColumn.setCellValueFactory(new PropertyValueFactory<Student, String>("age"));

		tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
	}

	@GuardedBy("this")
	public synchronized void setStudents(ObservableList<Student> students) {
		tableView.setItems(students);
	}
}
