package app;

import app.constants.ExtensionImage;
import app.constants.FileConstant;
import app.controllers.CreateWindowController;
import app.controllers.FileController;
import app.controllers.SearchWindowController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;

public class Main extends Application {

    private Stage primaryStage;
    private final Image image = new Image(ExtensionImage.ICON);
    private static final Logger log = Logger.getLogger(Main.class);

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        initMainWindow();
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void initMainWindow() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/totalcom.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle(FileConstant.APP_NAME);
        primaryStage.getIcons().add(image);
        Scene mainScene = new Scene(root, 827, 551);
        primaryStage.setScene(mainScene);
        FileController fileController = loader.getController();
        fileController.setMainApp(this);
        primaryStage.setOnCloseRequest(fileController.handleCloseEvent);
        fileController.setKeyBoardListener();
        primaryStage.show();
    }

    public String initCreateWindow(String labelText, String fieldText) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/views/createWindow.fxml"));
            Pane page = loader.load();
            Label label = (Label) page.getChildren().get(0);
            label.setText(labelText);
            TextField textField = (TextField) page.getChildren().get(1);
            textField.setText(fieldText);
            Stage dialogStage = new Stage();
            dialogStage.setTitle(FileConstant.APP_NAME);
            dialogStage.getIcons().add(image);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            CreateWindowController createWindowController = loader.getController();
            createWindowController.setDialogStage(dialogStage);
            dialogStage.showAndWait();
            return createWindowController.getDirectory();
        } catch (IOException e) {
            log.error(FileConstant.ERR_OPEN_CREATE_WINDOW);
            return "";
        }
    }

    public void initListerText(String title, String text) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/views/listerText.fxml"));
            Pane page = loader.load();
            TextArea textArea = (TextArea) page.getChildren().get(0);
            textArea.setText(text);
            createStage(title, image, page);
        } catch (IOException e) {
            log.error(FileConstant.ERR_OPEN_LISTER_WINDOW);
        }
    }

    public void initListerImage(String title, String imagePath) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/views/listerImage.fxml"));
            Pane page = loader.load();
            ImageView imageView = (ImageView) page.getChildren().get(0);
            Image listerImage = new Image(imagePath);
            imageView.setImage(listerImage);
            createStage(title, listerImage, page);
        } catch (IOException e) {
            log.error(FileConstant.ERR_OPEN_LISTER_WINDOW);
        }
    }

    public Path initSearchWindow(String curDir) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/views/searchWindow.fxml"));
            Pane page = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle(FileConstant.SEARCH_WINDOW_NAME);
            dialogStage.getIcons().add(image);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            SearchWindowController searchWindowController = loader.getController();
            searchWindowController.setMainApp(this);
            searchWindowController.setSearchDirField(curDir);
            searchWindowController.setDialogStage(dialogStage);
            dialogStage.showAndWait();
            return searchWindowController.getSearchResult();
        } catch (IOException e) {
            log.error(FileConstant.ERR_OPEN_SEARCH_WINDOW);
            return null;
        }
    }

    public void initHelpWindow() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/views/help.fxml"));
            Pane page = loader.load();
            createStage(FileConstant.HELP_WINDOW_NAME, image, page);
        } catch (IOException e) {
            log.error(FileConstant.ERR_OPEN_HELP_WINDOW);
        }
    }

    public void createStage(String title, Image image, Pane page) {
        Stage dialogStage = new Stage();
        dialogStage.setTitle(title);
        dialogStage.getIcons().add(image);
        dialogStage.initModality(Modality.NONE);
        dialogStage.initOwner(primaryStage);
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);
        dialogStage.showAndWait();
    }
}
