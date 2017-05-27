package FXML;

import Student.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CrawlerController {

    private final ObservableList<Student> studentsList= FXCollections.observableArrayList();
    private ObservableList<XYChart.Data<String,Integer>> marks = FXCollections.observableArrayList();
    private XYChart.Series<String,Integer> students = new XYChart.Series<String, Integer>(marks);
    private final ObservableList<String> log = FXCollections.observableArrayList();
    private DateFormat dateFormat;

    Stage stage;
    Scene scene;

    @FXML
    MenuBar menuBar;

    @FXML
    Menu menuFile;
    @FXML
    Menu menuAbout;

    @FXML
    MenuItem menuItemClose;
    @FXML
    MenuItem menuItemAbout;

    @FXML
    TableView tableView;

    @FXML
    ListView listLogView;

    @FXML
    TableColumn tableColumnMark;
    @FXML
    TableColumn tableColumnFirstName;
    @FXML
    TableColumn tableColumnLastName;
    @FXML
    TableColumn tableColumnAge;

    @FXML
    BarChart barChart;

    @FXML
    CategoryAxis categoryAxis;
    @FXML
    NumberAxis numberAxis;

    @FXML
    public void closeClick(){
        stage.setScene(scene);
    }

    @FXML
    public void aboutClick(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Author: anonymous" , ButtonType.OK);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.setTitle("About");
        alert.setHeaderText("Example program informations");
        alert.setContentText("Author: anonymous");
        alert.showAndWait();
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

        //tableView.setItems(studentsList);
        //tableView.setEditable(false);

        tableView.setItems(studentsList);

        tableColumnMark.prefWidthProperty().bind(tableView.widthProperty().multiply(0.25));
        tableColumnMark.setCellValueFactory(new PropertyValueFactory<Student,String>("mark"));

        tableColumnFirstName.prefWidthProperty().bind(tableView.widthProperty().multiply(0.25));
        tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<Student,String>("firstName"));

        tableColumnLastName.prefWidthProperty().bind(tableView.widthProperty().multiply(0.25));
        tableColumnLastName.setCellValueFactory(new PropertyValueFactory<Student,String>("lastName"));

        tableColumnAge.prefWidthProperty().bind(tableView.widthProperty().multiply(0.25));
        tableColumnAge.setCellValueFactory(new PropertyValueFactory<Student,String>("age"));

        barChart.setMinSize(Region.USE_PREF_SIZE,Region.USE_PREF_SIZE);

        numberAxis.setTickLabelFormatter(new StringConverter<Number>() {
            @Override
            public String toString(Number object) {
                if(object.intValue()!=object.doubleValue())
                    return "";
                return ""+(object.intValue());
            }

            @Override
            public Number fromString(String string) {
                Number val = Double.parseDouble(string);
                return val.intValue();
            }
        });

        marks.addAll(
                new XYChart.Data<String, Integer>(Double.toString(2.0),0),
                new XYChart.Data<String, Integer>(Double.toString(3.0),0),
                new XYChart.Data<String, Integer>(Double.toString(3.5),0),
                new XYChart.Data<String, Integer>(Double.toString(4.0),0),
                new XYChart.Data<String, Integer>(Double.toString(4.5),0),
                new XYChart.Data<String, Integer>(Double.toString(5.0),0)
        );
        barChart.getData().addAll(students);

        dateFormat = new SimpleDateFormat("HH:mm:ss:ms");
        listLogView.setItems(log);

    }

    public synchronized void addStudent(Student student){
        studentsList.add(student);
    }
    public synchronized void removeStudent(Student student){
        studentsList.remove(student);
    }

    public synchronized void updateChartAdd(double mark){
        for(XYChart.Data<String,Integer> tmp : marks){
            int pom = tmp.getYValue();
            {
                if(Double.toString(mark).equals(tmp.getXValue())){
                    tmp.setYValue(++pom);
                }
            }
        }

    }
    public synchronized void updateChartRemove(double mark){
        for(XYChart.Data<String,Integer> tmp : marks){
            int pom = tmp.getYValue();
            {
                if(Double.toString(mark).equals(tmp.getXValue())){
                    tmp.setYValue(--pom);
                }
            }
        }

    }

    public synchronized void addedStudentInfo(Student student){
        log.add("" + dateFormat.format(new Date())+"\tADDED:\t"+student.toString());
    }
    public synchronized void removedStudentInfo(Student student){
        log.add("" + dateFormat.format(new Date())+"\tREMOVED:\t"+student.toString());
    }
}
