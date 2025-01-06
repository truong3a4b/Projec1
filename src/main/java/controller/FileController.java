package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class FileController {
    private  File selectedFile;
    private static final long MAX_FILE_SIZE = 600 * 1024 * 1024;

    @FXML
    private Label fileNameLabel;

    public void chooseFile() throws Exception {
        Stage fileChooserStage = new Stage();
        fileChooserStage.setTitle("File Explorer");

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select file you want to scan");

        this.selectedFile = fileChooser.showOpenDialog(fileChooserStage);
        if (selectedFile != null) {
            if (selectedFile.length() > MAX_FILE_SIZE) {
                System.out.println("Tệp vượt quá dung lượng cho phép (600 MB)!");
                try {
                    throw new Exception("Tệp vượt quá dung lượng cho phép (600 MB)!");
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Error");
                    alert.setContentText(e.getMessage());
                    alert.showAndWait();
                }
            } else {
                System.out.println("File được chọn: " + selectedFile.getAbsolutePath());
                fileNameLabel.setText(selectedFile.getName());
            }

        } else {
            System.out.println("Không có file nào được chọn.");
        }

        fileChooserStage.close();
    }

    public File getSelectedFile(){
        return selectedFile;
    }
}
