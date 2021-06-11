package app;

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
import java.io.IOException;
import java.nio.file.Path;

public class Main extends Application {

    private Stage primaryStage;
    private final Image image = new Image("./img/1961.png");

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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("views/totalcom.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Total Commander Emulator");
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
            loader.setLocation(Main.class.getResource("views/createWindow.fxml"));
            Pane page = loader.load();
            Label label = (Label) page.getChildren().get(0);
            label.setText(labelText);
            TextField textField = (TextField) page.getChildren().get(1);
            textField.setText(fieldText);
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Total Commander Emulator");
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
            e.printStackTrace();
            return "";
        }
    }

    public void initListerText(String title, String text) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("views/listerText.fxml"));
            Pane page = loader.load();
            TextArea textArea = (TextArea) page.getChildren().get(0);
            textArea.setText(text);
            Stage dialogStage = new Stage();
            dialogStage.setTitle(title);
            dialogStage.getIcons().add(image);
            dialogStage.initModality(Modality.NONE);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initListerImage(String title, String imagePath) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("views/listerImage.fxml"));
            Pane page = loader.load();
            ImageView imageView = (ImageView) page.getChildren().get(0);
            Image listerImage = new Image(imagePath);
            imageView.setImage(listerImage);
            Stage dialogStage = new Stage();
            dialogStage.setTitle(title);
            dialogStage.getIcons().add(listerImage);
            dialogStage.initModality(Modality.NONE);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Path initSearchWindow(String curDir) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("views/searchWindow.fxml"));
            Pane page = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Поиск файлов");
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
            e.printStackTrace();
            return null;
        }
    }

    public void initHelpWindow() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("views/help.fxml"));
            Pane page = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Горящие клавиши");
            dialogStage.getIcons().add(image);
            dialogStage.initModality(Modality.NONE);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
