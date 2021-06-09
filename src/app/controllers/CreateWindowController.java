package app.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CreateWindowController {
    @FXML
    private TextField createDirField;
    @FXML
    private Button createDirButton;
    @FXML
    private Stage dialogStage;
    private String file = "";


    public String getDirectory() {
        return this.file;
    }

    @FXML
    private void initialize() {
        createDirButton.setDefaultButton(true);
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    @FXML
    private void handleOk() {
        this.file = createDirField.getText();
        System.out.println("New directory/file name is: " + this.file);

        dialogStage.close();
    }

    @FXML
    private void handleCancel() {
        createDirField.setText("");
        dialogStage.close();
    }
}
