package controller;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.Report;
import scan_virus.ScanVirus;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    private static final String API_KEY = "66e79ad2fb6a04e4a58649327d1e4d725496123209810be4548c466a449ef0ca";
    private Stage stage;
    @FXML
    Pane contentArea;
    @FXML
    Button filebtn, urlbtn, searchbtn,scanbtn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
    public void loadUrlComponent(){
        loadComponent("/view/url_component.fxml");
        urlbtn.getStyleClass().add("selected");
        searchbtn.getStyleClass().remove("selected");
        filebtn.getStyleClass().remove("selected");
    }

    public void loadFileComponent(){
        loadComponent("/view/file_component.fxml");
        filebtn.getStyleClass().add("selected");
        searchbtn.getStyleClass().remove("selected");
        urlbtn.getStyleClass().remove("selected");
    }

    public void loadSearchComponent(){
        loadComponent("/view/search_component.fxml");
        searchbtn.getStyleClass().add("selected");
        filebtn.getStyleClass().remove("selected");
        urlbtn.getStyleClass().remove("selected");
    }

    public void loadComponent(String fileFXML){
        try {
            contentArea.getChildren().clear();
            Node page = FXMLLoader.load(getClass().getResource(fileFXML));
            contentArea.getChildren().setAll(page);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void setStage(Stage stage){
        this.stage = stage;
    }

    public void goToResult() throws Exception{
        Label statusLabel = new Label("Scanning");
        contentArea.getChildren().add(statusLabel);
        scanbtn.setDisable(true);

        ScanVirus scanVirus = new ScanVirus(API_KEY);
        Task<Void> scanTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                long startTime = System.currentTimeMillis();
                scanVirus.scanURL("https://outlawseafood.com/");
                Thread.sleep(5000);
                long endTime = System.currentTimeMillis();
                long duration = endTime - startTime;
                updateMessage("Scan completed in " + duration + " ms");
                return null;
            }
        };

        scanTask.setOnSucceeded(e -> {
            statusLabel.setText(scanTask.getMessage());
            scanbtn.setDisable(false);

            List<Report> reports = scanVirus.getReports();
            for(Report re : reports){
                System.out.println(re.getName());
            }


            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/result.fxml"));
            Parent root;
            try {
                root = loader.load();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

            ResultController controller = loader.getController();
            controller.setReports(reports);
            controller.setStage(stage);

            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/view/result_style.css").toExternalForm());
            stage.setScene(scene);

        });

        scanTask.setOnFailed(e -> {
            statusLabel.setText("Status: Scan failed.");
            scanbtn.setDisable(false);

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Scan Failed");
            alert.setContentText("An error occurred while scanning.");
            alert.showAndWait();
        });

        Thread thread = new Thread(scanTask);
        thread.setDaemon(true);
        thread.start();
    }


}
