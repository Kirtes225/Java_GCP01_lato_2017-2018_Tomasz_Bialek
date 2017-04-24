package com.company.fxml;

import com.company.events.CrawlerEventType;
import com.example.Student;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

@ThreadSafe
public class LogController implements Initializable {
	private final Object dataLock = new Object();
	@FXML
	public ListView listView;
	@GuardedBy("dataLock")
	private ObservableList<String> logsData;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		logsData = FXCollections.observableArrayList();
		listView.setItems(logsData);

	}

	public void addData(CrawlerEventType crawlerEventType, Student student) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {

				Date date = new Date();
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss.SSS");

				synchronized (dataLock) {
					logsData.add(simpleDateFormat.format(date) + "        [" + crawlerEventType + "]          " +
							student.getFirstName() + "        " + student.getLastName());

				}
			}
		});
	}
}
