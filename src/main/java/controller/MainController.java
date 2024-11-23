package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    Pane contentArea;
    @FXML
    Button filebtn, urlbtn, searchbtn;

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


}
