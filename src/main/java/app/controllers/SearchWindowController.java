package app.controllers;

import app.Main;
import app.models.TCFile;
import app.constants.FileConstant;
import app.services.FileService;
import app.services.OpenCommand;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.ResourceBundle;

public class SearchWindowController implements Initializable {
    @FXML
    private Button startButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Button reviewButton;
    @FXML
    private Button editButton;
    @FXML
    private Button goToButton;
    @FXML
    private TextField searchFileField;
    @FXML
    private TextField searchDirField;
    @FXML
    private ListView<Path> resultsListView;
    @FXML
    private ComboBox<String> levelComboBox;
    private final ObservableList<Path> files = FXCollections.observableArrayList();
    private Stage dialogStage;
    private Path searchResult = null;
    private Main main;
    private static final Logger log = Logger.getLogger(SearchWindowController.class);

    public void setMainApp(Main main) {
        this.main = main;
    }

    public void setSearchDirField(String curDir) {
        searchDirField.setText(curDir);
    }

    public void setComboBox(ComboBox<String> cb) {
        cb.getItems().add("Все (неограниченная)");
        cb.getItems().add("Число уровней: 1 (только текущий)");
        cb.getSelectionModel().select(0);
        for (int i = 2; i < 101; i++) {
            cb.getItems().add("Число уровней: " + i);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        resultsListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        resultsListView.setItems(files);
        resultsListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                searchResult = resultsListView.getSelectionModel().getSelectedItem();
                dialogStage.close();
            }
        });
        setComboBox(levelComboBox);
        setHandlers();
    }

    public Path getSearchResult() {
        return searchResult;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    private void setHandlers() {
        startButton.addEventHandler(ActionEvent.ACTION, handleSearchFilesEvent);
        cancelButton.setOnAction(event -> dialogStage.close());
        goToButton.addEventHandler(ActionEvent.ACTION, handleGoToEvent);
        reviewButton.addEventHandler(ActionEvent.ACTION, handleReviewFileEvent);
        editButton.addEventHandler(ActionEvent.ACTION, handleEditFileEvent);
    }

    private final EventHandler<ActionEvent> handleSearchFilesEvent = event -> {
        String key = searchFileField.getText();
        String dir = searchDirField.getText();
        if (!key.isEmpty() && !dir.isEmpty()) {
            TCFile file;
            files.clear();
            try {
                file = new TCFile(dir);
                int max = levelComboBox.getSelectionModel().getSelectedIndex();
                if (max == 0) {
                    max = Integer.MAX_VALUE;
                }
                FileService.searchFiles(file, files, key, max);
            } catch (IOException | NullPointerException exception) {
                log.error(exception.getMessage());
                FileService.initAlert(main.getPrimaryStage(), FileConstant.APP_NAME, FileConstant.ERR_SEARCH, FileConstant.ERR_COM_EXP, true);
            }
        } else {
            FileService.initAlert(main.getPrimaryStage(), "Error", "Не заполнены поля", "Заполните поля", true);
        }
    };

    private final EventHandler<ActionEvent> handleGoToEvent = event -> {
        searchResult = resultsListView.getSelectionModel().getSelectedItem();
        dialogStage.close();
    };

    private final EventHandler<ActionEvent> handleEditFileEvent = event -> {
        try {
            new OpenCommand(null, Arrays.asList(FileConstant.CMD, FileConstant.C, FileConstant.START, resultsListView.getSelectionModel().getSelectedItem().toString())).execute();
        } catch (IOException exception) {
            FileService.initAlert(main.getPrimaryStage(), FileConstant.APP_NAME, FileConstant.ERR_EDIT, FileConstant.ERR_COM_EXP, true);
        }
    };

    private final EventHandler<ActionEvent> handleReviewFileEvent = event -> {
        Path path = resultsListView.getSelectionModel().getSelectedItem();
        try {
            new OpenCommand(null, Arrays.asList(FileConstant.NOTEPAD, path.toString())).execute();
        } catch (IOException exception) {
            FileService.initAlert(main.getPrimaryStage(), FileConstant.APP_NAME, FileConstant.ERR_NOTEPAD, FileConstant.ERR_COM_EXP, true);
        }
    };
}
