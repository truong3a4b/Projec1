package controller;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import model.Report;
import scan_virus.JsonProcess;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class HistoryController implements Initializable {

    @FXML
    private VBox hisVBox;

    @Override
    public void initialize(URL location, ResourceBundle resources){

    }

    public void createTable() throws IOException {
        JsonProcess jsonProcess = new JsonProcess();
        List<Report> reports = jsonProcess.getHistoryFromJson();

        hisVBox.getChildren().clear();

        for(Report report: reports){
            HBox compHbox = new HBox();
            Label nameLabel = new Label(report.getName());
            Label timeLabel = new Label(report.getTime());

            nameLabel.setPrefHeight(50);
            nameLabel.setPrefWidth(500);
            nameLabel.setPadding(new Insets(0, 0, 0, 50));
            nameLabel.setFont(Font.font(14));

            timeLabel.setPrefWidth(300);
            timeLabel.setPrefHeight(50);
            timeLabel.setPadding(new Insets(0, 0, 0, 50));
            timeLabel.setFont(Font.font(14));

            compHbox.getChildren().addAll(nameLabel,timeLabel);
            hisVBox.getChildren().add(compHbox);
        }
    }

    public void clearHis() throws IOException {
        JsonProcess jsonProcess = new JsonProcess();
        jsonProcess.clearJson();
        createTable();
    }
}
