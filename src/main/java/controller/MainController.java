package controller;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.ResultAnalysis;
import scan_virus.ScanVirus;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    private static final String API_KEY = "66e79ad2fb6a04e4a58649327d1e4d725496123209810be4548c466a449ef0ca";
    private Stage stage;
    private int flagBtn;
    private UrlController urlController;
    private FileController fileController;
    private SearchController searchController;
    @FXML
    private HBox contentArea;
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
        this.flagBtn = 1;
        loadFileComponent();
        scanningLabel.setVisible(false);
        loadingCircle.setVisible(false);
    }
    public void loadUrlComponent(){
        this.flagBtn = 2;
        loadComponent("/view/url_component.fxml");
        urlbtn.getStyleClass().add("selected");
        searchbtn.getStyleClass().remove("selected");
        filebtn.getStyleClass().remove("selected");

    }

    public void loadFileComponent(){
        this.flagBtn=1;
        loadComponent("/view/file_component.fxml");
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
            if(flagBtn == 2) this.urlController = loader.getController();
            if(flagBtn == 1) this.fileController = loader.getController();
            if(flagBtn == 3) this.searchController=loader.getController();

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
        scanningLabel.setText("Scanning");


        ScanVirus scanVirus = new ScanVirus(API_KEY);
        Task<Void> scanTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                long startTime = System.currentTimeMillis();
                filebtn.setDisable(true);
                urlbtn.setDisable(true);
                searchbtn.setDisable(true);

                if(flagBtn == 1){
                    File file = fileController.getSelectedFile();
                    System.out.println(file.getName());
                    scanVirus.scanFile(file);
                }

                if(flagBtn == 2){
                    String url = urlController.getUrl();
                    System.out.println(url);
                    scanVirus.scanURL(url);
                }

                if(flagBtn == 3){
                    String input = searchController.getUrl();
                    System.out.println(input);
                    scanVirus.scanIpAndDomain(input);
                }

                int timeLimit = 30;
                while (scanVirus.getReports().getResults().size() == 0){
                    Thread.sleep(1000);
                    System.out.println("+1s");
                    timeLimit--;
                    if(timeLimit == 0){
                        throw new Exception("Time limit");
                    }
                }

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
            List<ResultAnalysis> resultAnalyses = scanVirus.getReports().getResults();
            for(ResultAnalysis re : resultAnalyses){
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
            controller.setReport(scanVirus.getReports());
            controller.setStage(stage);
            if(flagBtn == 2) {
                controller.setNameLabel(urlController.getUrl());

            }
            if(flagBtn == 1) {
                controller.setNameLabel(fileController.getSelectedFile().getName());

            }
            if(flagBtn == 3) {
                controller.setNameLabel(searchController.getUrl());

            }

            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/view/result_style.css").toExternalForm());
            stage.setScene(scene);
            System.out.println(flagBtn);
        });

        scanTask.setOnFailed(e -> {
            filebtn.setDisable(false);
            urlbtn.setDisable(false);
            searchbtn.setDisable(false);
            scanbtn.setDisable(false);
            scanningLabel.setText("Scan Fail!");
            loadingCircle.setVisible(false);

            Throwable exception = scanTask.getException();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Scan Failed");
            alert.setContentText(exception.getMessage());
            alert.showAndWait();
        });
        System.out.println(flagBtn);
        Thread thread = new Thread(scanTask);
        thread.setDaemon(true);
        thread.start();
    }

    public void setFlagBtn(int k){
        this.flagBtn = k;
    }
}
