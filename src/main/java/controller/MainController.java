package controller;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
    private int flagBtn;
    private UrlController urlController;
    @FXML
    private Pane contentArea;
    @FXML
    private Button filebtn, urlbtn, searchbtn,scanbtn;
    @FXML
    private Label scanningLabel;
    @FXML
    private ProgressIndicator loadingCircle;


    @FXML
    private TextField searchBar;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadFileComponent();
        scanningLabel.setVisible(false);
        loadingCircle.setVisible(false);
    }
    public void loadUrlComponent(){
        loadComponent("/view/url_component.fxml");
        this.flagBtn = 2;
        urlbtn.getStyleClass().add("selected");
        searchbtn.getStyleClass().remove("selected");
        filebtn.getStyleClass().remove("selected");

    }

    public void loadFileComponent(){
        loadComponent("/view/file_component.fxml");
        this.flagBtn=1;
        filebtn.getStyleClass().add("selected");
        searchbtn.getStyleClass().remove("selected");
        urlbtn.getStyleClass().remove("selected");

    }

    public void loadSearchComponent(){
        this.flagBtn=3;
        loadComponent("/view/search_component.fxml");
        searchbtn.getStyleClass().add("selected");
        filebtn.getStyleClass().remove("selected");
        urlbtn.getStyleClass().remove("selected");
    }

    public void loadComponent(String fileFXML){
        try {
            contentArea.getChildren().clear();
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fileFXML));
            Node page = loader.load();
            contentArea.getChildren().setAll(page);
            if(flagBtn == 1) this.urlController = loader.getController();

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void setStage(Stage stage){
        this.stage = stage;
    }

    public void goToResult() throws Exception{

        scanbtn.setDisable(true);
        scanningLabel.setVisible(true);
        loadingCircle.setVisible(true);



        ScanVirus scanVirus = new ScanVirus(API_KEY);
        Task<Void> scanTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                long startTime = System.currentTimeMillis();

                if(flagBtn == 2){
                    String url = urlController.getUrl();
                    System.out.println(url);
                    scanVirus.scanURL(url);
                }

                Thread.sleep(8000);
                long endTime = System.currentTimeMillis();
                long duration = endTime - startTime;
                updateMessage("Scan completed in " + duration + " ms");
                return null;
            }
        };

        System.out.println(flagBtn);
        scanTask.setOnSucceeded(e -> {
            scanbtn.setDisable(false);
            scanningLabel.setText("Scan Success");
            loadingCircle.setVisible(false);
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
            System.out.println(flagBtn);
        });

        scanTask.setOnFailed(e -> {
            scanbtn.setDisable(false);
            scanningLabel.setText("Scan Fail!");
            loadingCircle.setVisible(false);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Scan Failed");
            alert.setContentText("An error occurred while scanning.");
            alert.showAndWait();
        });
        System.out.println(flagBtn);
        Thread thread = new Thread(scanTask);
        thread.setDaemon(true);
        thread.start();
    }


}
