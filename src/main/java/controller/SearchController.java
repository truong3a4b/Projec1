package controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class SearchController {
    @FXML
    private TextField searchBar;

    public String getUrl(){
        String url = searchBar.getText();
        return url;
    }
}