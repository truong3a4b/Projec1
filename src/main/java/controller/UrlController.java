package controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;


public class UrlController {
    @FXML
    private TextField searchBar;

    public String getUrl(){
        String url = searchBar.getText();
        return url;
    }
}
