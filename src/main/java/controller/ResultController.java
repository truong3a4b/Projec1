package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Report;
import model.ResultAnalysis;
import scan_virus.ApiRequestException;
import scan_virus.ExcelExporter;
import scan_virus.ScanVirus;


import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ResultController implements Initializable {

    private Stage stage;
    private Report report;
    private ObservableList<ResultAnalysis> results;
    private ExcelExporter excelExporter;

    @FXML
    private GridPane tableAnalys;
    @FXML
    private Circle outerCircle;
    @FXML
    private Arc progressArc;
    @FXML
    private Label maliciousLabel, totalLabel,resultLabel, nameLabel;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        excelExporter = new ExcelExporter();
    }

    public void setTableAnalys(){
        tableAnalys.setPrefSize(GridPane.USE_COMPUTED_SIZE, GridPane.USE_COMPUTED_SIZE);
        results = FXCollections.observableArrayList(report.getResults());
        int row = results.size()/2;
        for(int i = 0; i < row; i++){

            Label nameLabel1 = new Label(results.get(i*2).getName());
            nameLabel1.getStyleClass().add("result");
            Label resultLabel1;
            if(results.get(i*2).getResult().equals("null")) resultLabel1 = new Label(results.get(i*2).getCategory());
            else resultLabel1 = new Label(results.get(i*2).getResult());
            resultLabel1.getStyleClass().add("result");
            tableAnalys.add(nameLabel1,0,i);
            tableAnalys.add(resultLabel1,1,i);

            Label space = new Label();
            space.getStyleClass().add("space");
            tableAnalys.add(space,2,i);

            Label nameLabel2 = new Label(results.get(i*2+1).getName());
            nameLabel2.getStyleClass().add("result");
            Label resultLabel2;
            if(results.get(i*2).getResult().equals("null")) resultLabel2 = new Label(results.get(i*2+1).getCategory());
            else resultLabel2 = new Label(results.get(i*2+1).getResult());
            resultLabel2.getStyleClass().add("result");
            tableAnalys.add(nameLabel2,3,i);
            tableAnalys.add(resultLabel2,4,i);

        }
    }
    public void setStage(Stage stage){
        this.stage = stage;
    }

    public void setReport(Report report){
        this.report = report;
        if(report.getResults() != null){
            setTableAnalys();
            setStats();
        }
    }

    public void returnHome(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/home.fxml"));
        Parent root;
        try {
            root = loader.load();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        MainController controller = loader.getController();
        controller.setStage(stage);

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/view/home_style.css").toExternalForm());
        stage.setScene(scene);

    }

    public void setStats(){
        int malicious = report.getStats().getMalicious();
        System.out.println("Malicious: " + malicious);
        int total = report.getResults().size();
        System.out.println("Total: " + total);
        totalLabel.setText("/" + total);
        int ratio = malicious/total*360;


        if(malicious == 0) {
            outerCircle.setFill(Color.web("#54AB98"));
            maliciousLabel.setText("0");
            maliciousLabel.setTextFill(Color.web("#54AB98"));
            progressArc.setLength(0);
            resultLabel.setText("No security vendors flagged this URL as malicious");
            resultLabel.setTextFill(Color.web("#54AB98"));
        }
        else{
            if(ratio < 10) ratio = 10;
            progressArc.setStartAngle(90-ratio);
            progressArc.setLength(ratio);
            maliciousLabel.setText("" + malicious);
            maliciousLabel.setTextFill(Color.web("#ff5a50"));
            resultLabel.setText(malicious + "/" + total + " security vendor flagged this URL as malicious");
            resultLabel.setTextFill(Color.web("#ff5a50"));
        }
    }

    public void setNameLabel(String name){
        nameLabel.setText(name);
    }

    public void exportExcel(){
        Stage folderChooserStage = new Stage();
        folderChooserStage.setTitle("File Explorer");

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select folder");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("xlsx (*.xlsx)", "*.xlsx"));

        File file = fileChooser.showSaveDialog(folderChooserStage);

        if (file != null) {
            try {
                if (file.createNewFile()) {
                    excelExporter.exportReportToExcel(report,file.getAbsolutePath());
                    showAlert(AlertType.INFORMATION, "Success", "File has been saved at:  " + file.getAbsolutePath());
                } else {

                    showAlert(AlertType.WARNING, "WARN", "File already exists!");
                }
            }catch (IOException e){
                showAlert(AlertType.ERROR, "ERROR", "Can't save file: " + e.getMessage());
            }

        } else {
            System.out.println("Người dùng đã hủy.");
        }

        folderChooserStage.close();

    }
    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
