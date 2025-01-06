package controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;


public class UrlController extends SearchController{
    @FXML
    private TextField searchBar;

    public String getUrl(){
        String url = this.searchBar.getText();
        return url;
    }
}
