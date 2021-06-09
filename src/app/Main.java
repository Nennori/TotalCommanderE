package app;

import app.controllers.CreateWindowController;
import app.controllers.FileController;
import app.controllers.SearchWindowController;
import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.nio.file.Path;

public class Main extends Application {

    private Stage primaryStage;
    private FileController fileController;
    private CreateWindowController createWindowController;
    private SearchWindowController searchWindowController;
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
        fileController = loader.getController();
        fileController.setMainApp(this);
        primaryStage.setOnCloseRequest(fileController.handleCloseEvent);
        setKeyBoardListener();
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
            createWindowController = loader.getController();
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
            Image image = new Image(imagePath);
            imageView.setImage(image);
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
            searchWindowController = loader.getController();
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

    private void setKeyBoardListener() {
        Scene mainScene = primaryStage.getScene();
        if (mainScene != null) System.out.println("Main scene is received");
        assert mainScene != null;
        mainScene.setOnKeyPressed(keyEvent -> {
            KeyCode keyCode = keyEvent.getCode();
            if (keyEvent.isShiftDown() && (keyCode == KeyCode.F4)) {
                fileController.handleCreateFileEvent.handle(keyEvent);
            } else if (keyEvent.isControlDown() && (keyCode == KeyCode.C)) {
                fileController.handleSelectFilesEvent.handle(keyEvent);
            } else if (keyEvent.isControlDown() && (keyCode == KeyCode.V)) {
                fileController.handleCopyFilesEvent1.handle(keyEvent);
            } else if (keyEvent.isAltDown() && keyEvent.isControlDown() && (keyCode == KeyCode.F5)) {
                fileController.handleZipDirEvent.handle(keyEvent);
            } else if (keyEvent.isAltDown() && (keyCode == KeyCode.F4)) {
                new EventHandler<Event>() {
                    @Override
                    public void handle(Event event) {
                        primaryStage.close();
                    }
                };
            } else if (keyEvent.isAltDown() && keyCode == KeyCode.LEFT) {
                fileController.handleBackEvent.handle(keyEvent);
            } else if (keyEvent.isAltDown() && keyCode == KeyCode.RIGHT) {
                fileController.handleForwardEvent.handle(keyEvent);
            } else if (keyEvent.isShiftDown() && (keyCode == KeyCode.DELETE || keyCode == KeyCode.F8)) {
                fileController.handleDeleteFilesHardEvent.handle(keyEvent);
            } else if (keyEvent.isControlDown() && keyEvent.isShiftDown() && keyCode == KeyCode.K) {
                fileController.handleOpenTrashEvent.handle(keyEvent);
            } else if (keyEvent.isAltDown() && keyCode == KeyCode.T) {
                fileController.handleOpenControlEvent.handle(keyEvent);
            } else if (keyEvent.isControlDown() && keyCode == KeyCode.SLASH) {
                fileController.handleMoveToRootEvent1.handle(keyEvent);
            } else if (keyEvent.isControlDown() && keyCode == KeyCode.BACK_SLASH) {
                fileController.handleMoveToRootEvent1.handle(keyEvent);
            } else if (keyEvent.isAltDown() && keyCode == KeyCode.F7) {
                fileController.handleSearchEvent.handle(keyEvent);
            } else if (keyCode.equals(KeyCode.F3)) {
                fileController.handleObserveFilesEvent.handle(keyEvent);
            } else if (keyCode.equals(KeyCode.F4)) {
                fileController.handleEditFileEvent.handle(keyEvent);
            } else if (keyCode.equals(KeyCode.F5)) {
                fileController.handleCopyFilesEvent.handle(keyEvent);
            } else if (keyCode.equals(KeyCode.F6)) {
                fileController.handleRenameMoveEvent.handle(keyEvent);
            } else if (keyCode.equals(KeyCode.F7)) {
                fileController.handleCreateDirEvent.handle(keyEvent);
            } else if ((keyCode == KeyCode.F8) || (keyCode == KeyCode.DELETE)) {
                fileController.handleDeleteFilesSafeEvent.handle(keyEvent);
            } else if (keyEvent.getCode() == KeyCode.F9) {
                fileController.handleRefreshEvent.handle(keyEvent);
            } else if (keyCode == KeyCode.BACK_SPACE) {
                fileController.handleMoveToParentEvent.handle(keyEvent);
            }
        });

    }
}
