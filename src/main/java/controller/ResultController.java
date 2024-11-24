package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.Report;
import scan_virus.ScanVirus;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ResultController implements Initializable {

    private Stage stage;
    private List<Report> reports;

    @FXML
    private GridPane tableAnalys;


    @Override
    public void initialize(URL location, ResourceBundle resources) {


    }

    public void setTableAnalys(){
        tableAnalys.setPrefSize(GridPane.USE_COMPUTED_SIZE, GridPane.USE_COMPUTED_SIZE);
        int row = reports.size()/2;
        for(int i = 0; i < row; i++){

            Label nameLabel1 = new Label(reports.get(i*2).getName());
            nameLabel1.getStyleClass().add("result");
            Label resultLabel1 = new Label(reports.get(i*2).getResult());
            resultLabel1.getStyleClass().add("result");
            tableAnalys.add(nameLabel1,0,i);
            tableAnalys.add(resultLabel1,1,i);

            Label space = new Label();
            space.getStyleClass().add("space");
            tableAnalys.add(space,2,i);

            Label nameLabel2 = new Label(reports.get(i*2+1).getName());
            nameLabel2.getStyleClass().add("result");
            Label resultLabel2 = new Label(reports.get(i*2+1).getResult());
            resultLabel2.getStyleClass().add("result");
            tableAnalys.add(nameLabel2,3,i);
            tableAnalys.add(resultLabel2,4,i);

        }
    }
    public void setStage(Stage stage){
        this.stage = stage;
    }

    public void setReports(List<Report> reports){
        this.reports = reports;
        if(reports != null){
            setTableAnalys();
        }
    }
}
