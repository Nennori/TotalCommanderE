package app.controllers;


import app.Main;
import app.models.TCFile;
import app.models.TCPanel;
import app.constants.FileConstant;
import app.services.*;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.WindowEvent;
import org.apache.log4j.Logger;

public class FileController implements Initializable {

    @FXML
    private TableView<TCFile> tableViewLeft;
    @FXML
    private TableColumn<TCFile, String> imageTableColumnLeft;
    @FXML
    private TableColumn<TCFile, String> imageTableColumnRight;
    @FXML
    private TableColumn<TCFile, String> nameTableColumnLeft;
    @FXML
    private TableColumn<TCFile, String> sizeTableColumnLeft;
    @FXML
    private TableColumn<TCFile, String> dateTableColumnLeft;
    @FXML
    private TableColumn<TCFile, String> typeTableColumnLeft;
    @FXML
    private TableColumn<TCFile, String> typeTableColumnRight;
    @FXML
    private TableView<TCFile> tableViewRight;
    @FXML
    private TableColumn<TCFile, String> nameTableColumnRight;
    @FXML
    private TableColumn<TCFile, String> sizeTableColumnRight;
    @FXML
    private TableColumn<TCFile, String> dateTableColumnRight;
    @FXML
    private Label dirInfoLabelSizeLeft;
    @FXML
    private Label dirInfoLabelSizeFreeLeft;
    @FXML
    private Label dirInfoLabelSizeFreeRight;
    @FXML
    private Label dirInfoLabelFilesSelectedLeft;
    @FXML
    private Label dirInfoLabelFilesLeft;
    @FXML
    private Label dirInfoLabelDirSelectedLeft;
    @FXML
    private Label dirInfoLabelDirLeft;
    @FXML
    private Label dirInfoLabelSizeRight;
    @FXML
    private Label dirInfoLabelFilesSelectedRight;
    @FXML
    private Label dirInfoLabelFilesRight;
    @FXML
    private Label dirInfoLabelDirSelectedRight;
    @FXML
    private Label dirInfoLabelDirRight;
    @FXML
    private Label diskInfoLabelLeft;
    @FXML
    private Label diskInfoLabelRight;
    @FXML
    private MenuButton dirMenuButtonLeft;
    @FXML
    private MenuButton dirMenuButtonRight;
    @FXML
    private MenuButton diskMenuButtonLeft;
    @FXML
    private MenuButton diskMenuButtonRight;
    @FXML
    private MenuButton storyMenuButtonLeft;
    @FXML
    private MenuButton storyMenuButtonRight;
    @FXML
    private MenuItem packMenuItem;
    @FXML
    private MenuItem unpackMenuItem;
    @FXML
    private MenuItem exitMenuItem;
    @FXML
    private MenuItem searchMenuItem;
    @FXML
    private MenuItem backMenuItem;
    @FXML
    private MenuItem cmdMenuItem;
    @FXML
    private MenuItem refreshMenuItem;
    @FXML
    private MenuItem helpMenuItem;
    @FXML
    private MenuItem aboutMenuItem;
    @FXML
    private Button f3Button;
    @FXML
    private Button f4Button;
    @FXML
    private Button f5Button;
    @FXML
    private Button f6Button;
    @FXML
    private Button f7Button;
    @FXML
    private Button f8Button;
    @FXML
    private Button exitButton;
    @FXML
    private Button refreshButton;
    @FXML
    private Button backButton;
    @FXML
    private Button forwardButton;
    @FXML
    private Button packButton;
    @FXML
    private Button extractButton;
    @FXML
    private Button searchButton;
    @FXML
    private Button notepadButton;
    @FXML
    private Button observeButton;
    @FXML
    private Button editButton;
    @FXML
    private Button copyButton;
    @FXML
    private Button renameMoveButton;
    @FXML
    private Button pack1Button;
    @FXML
    private Button rootButtonLeft;
    @FXML
    private Button rootButtonRight;
    @FXML
    private Button parentButtonLeft;
    @FXML
    private Button parentButtonRight;
    @FXML
    private Button createDirButton;
    private ObservableList<TCFile> selectedItems;
    private Main main;
    private String homePath;
    private TCPanel panelLeft;
    private TCPanel panelRight;
    private TCPanel curPanel;
    protected int curListStoryLeftIndex = 0;
    protected int curListStoryRightIndex = 0;
    private static final Logger log = Logger.getLogger(FileController.class);

    public void setMainApp(Main main) {
        this.main = main;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        File[] roots = File.listRoots();
        homePath = System.getProperty(FileConstant.USER_HOME) + "/Desktop";
        try {
            List<String> pathListLeft = FileService.readFromFile(FileConstant.SAVE_FILE_1);
            List<String> pathListRight = FileService.readFromFile(FileConstant.SAVE_FILE_2);
            panelLeft = new TCPanel(tableViewLeft, storyMenuButtonLeft, dirMenuButtonLeft, diskMenuButtonLeft,
                    dirInfoLabelSizeLeft, dirInfoLabelSizeFreeLeft, dirInfoLabelFilesSelectedLeft,
                    dirInfoLabelFilesLeft, dirInfoLabelDirSelectedLeft, dirInfoLabelDirLeft, diskInfoLabelLeft, null, pathListLeft, curListStoryLeftIndex);
            panelRight = new TCPanel(tableViewRight, storyMenuButtonRight, dirMenuButtonRight,
                    diskMenuButtonRight, dirInfoLabelSizeRight, dirInfoLabelSizeFreeRight,
                    dirInfoLabelFilesSelectedRight, dirInfoLabelFilesRight, dirInfoLabelDirSelectedRight,
                    dirInfoLabelDirRight, diskInfoLabelRight, null, pathListRight, curListStoryRightIndex);
            panelLeft.createStoryMenu(handleStoryItemSelectEvent);
            panelRight.createStoryMenu(handleStoryItemSelectEvent);
            TCFile homeDir = new TCFile(System.getProperty(FileConstant.USER_HOME) + "\\Desktop");
            panelLeft.createMenuItems(homeDir.getFileList(homeDir.getDirectories()), FileConstant.LEFT_ID, handleMoveToDirectoryEvent);
            panelRight.createMenuItems(homeDir.getFileList(homeDir.getDirectories()), FileConstant.RIGHT_ID, handleMoveToDirectoryEvent);
            panelLeft.setTableViewData(imageTableColumnLeft, nameTableColumnLeft, typeTableColumnLeft, sizeTableColumnLeft, dateTableColumnLeft);
            panelRight.setTableViewData(imageTableColumnRight, nameTableColumnRight, typeTableColumnRight, sizeTableColumnRight, dateTableColumnRight);
            panelLeft.setDiskData(roots[0]);
            panelRight.setDiskData(roots[0]);
            panelLeft.createDiskMenuItems(Arrays.asList(roots.clone()), FileConstant.LEFT_ID, handleMoveToDiskEvent);
            panelRight.createDiskMenuItems(Arrays.asList(roots.clone()), FileConstant.RIGHT_ID, handleMoveToDiskEvent);
            setListeners();
            setHandlers();
            curPanel = panelLeft;
            curPanel.setSelected();
            FileService.goAndUpdate(panelLeft, panelLeft.getCurDir());
            FileService.goAndUpdate(panelRight, panelRight.getCurDir());
        } catch (IOException ioException) {
            log.error(FileConstant.ERR_OPEN_APP);
        }
    }

    private void setHandlers() {
        f3Button.addEventHandler(ActionEvent.ACTION, handleObserveFilesEvent);
        f4Button.addEventHandler(ActionEvent.ACTION, handleEditFileEvent);
        f5Button.addEventHandler(ActionEvent.ACTION, handleCopyFilesEvent);
        f6Button.addEventHandler(ActionEvent.ACTION, handleRenameMoveEvent);
        f7Button.addEventHandler(ActionEvent.ACTION, handleCreateDirEvent);
        f8Button.addEventHandler(ActionEvent.ACTION, handleDeleteFilesSafeEvent);
        createDirButton.addEventHandler(ActionEvent.ACTION, handleCreateDirEvent);
        notepadButton.addEventHandler(ActionEvent.ACTION, handleOpenNotepadEvent);
        refreshButton.addEventHandler(ActionEvent.ACTION, handleRefreshEvent);
        copyButton.addEventHandler(ActionEvent.ACTION, handleCopyFilesEvent);
        renameMoveButton.addEventHandler(ActionEvent.ACTION, handleRenameMoveEvent);
        packButton.addEventHandler(ActionEvent.ACTION, handleZipDirEvent);
        pack1Button.addEventHandler(ActionEvent.ACTION, handleZipDirEvent);
        exitButton.addEventHandler(ActionEvent.ACTION, handleExitEvent);
        extractButton.addEventHandler(ActionEvent.ACTION, handleUnzipDirEvent);
        observeButton.addEventHandler(ActionEvent.ACTION, handleObserveFilesEvent);
        rootButtonLeft.addEventHandler(ActionEvent.ACTION, handleMoveToRootEvent);
        rootButtonRight.addEventHandler(ActionEvent.ACTION, handleMoveToRootEvent);
        parentButtonLeft.addEventHandler(ActionEvent.ACTION, handleMoveToParentEvent);
        parentButtonRight.addEventHandler(ActionEvent.ACTION, handleMoveToParentEvent);
        editButton.addEventHandler(ActionEvent.ACTION, handleEditFileEvent);
        forwardButton.addEventHandler(ActionEvent.ACTION, handleForwardEvent);
        backButton.addEventHandler(ActionEvent.ACTION, handleBackEvent);
        searchButton.addEventHandler(ActionEvent.ACTION, handleSearchEvent);
        packMenuItem.addEventHandler(Event.ANY, handleZipDirEvent);
        unpackMenuItem.addEventHandler(Event.ANY, handleUnzipDirEvent);
        exitMenuItem.addEventHandler(Event.ANY, handleExitEvent);
        searchMenuItem.addEventHandler(Event.ANY, handleSearchEvent);
        backMenuItem.addEventHandler(Event.ANY, handleBackEvent);
        cmdMenuItem.addEventHandler(Event.ANY, handleOpenCmdEvent);
        refreshMenuItem.addEventHandler(Event.ANY, handleRefreshEvent);
        helpMenuItem.addEventHandler(Event.ANY, handleHelpEvent);
        aboutMenuItem.addEventHandler(Event.ANY, handleAboutEvent);
    }

    private void setListeners() {
        setRowListener(tableViewLeft);
        setRowListener(tableViewRight);
        setTableViewListener(tableViewLeft, dirInfoLabelSizeFreeLeft, dirInfoLabelFilesSelectedLeft, dirInfoLabelDirSelectedLeft);
        setTableViewListener(tableViewRight, dirInfoLabelSizeFreeRight, dirInfoLabelFilesSelectedRight, dirInfoLabelDirSelectedRight);
    }

    public void setKeyBoardListener() {
        Scene mainScene = main.getPrimaryStage().getScene();
        assert mainScene != null;
        mainScene.setOnKeyPressed(keyEvent -> {
            KeyCode keyCode = keyEvent.getCode();
            if (keyEvent.isShiftDown() && (keyCode == KeyCode.F4)) {
                handleCreateFileEvent.handle(keyEvent);
            } else if (keyEvent.isControlDown() && (keyCode == KeyCode.C)) {
                handleSelectFilesEvent.handle(keyEvent);
            } else if (keyEvent.isControlDown() && (keyCode == KeyCode.V)) {
                handleCopyFilesEvent1.handle(keyEvent);
            } else if (keyEvent.isAltDown() && keyEvent.isControlDown() && (keyCode == KeyCode.F5)) {
                handleZipDirEvent.handle(keyEvent);
            } else if (keyEvent.isAltDown() && (keyCode == KeyCode.F4)) {
                main.getPrimaryStage().close();
            } else if (keyEvent.isShiftDown() && keyCode == KeyCode.LEFT) {
                handleBackEvent.handle(keyEvent);
            } else if (keyEvent.isShiftDown() && keyCode == KeyCode.RIGHT) {
                handleForwardEvent.handle(keyEvent);
            } else if (keyEvent.isShiftDown() && (keyCode == KeyCode.DELETE || keyCode == KeyCode.F8)) {
                handleDeleteFilesHardEvent.handle(keyEvent);
            } else if (keyEvent.isControlDown() && keyEvent.isShiftDown() && keyCode == KeyCode.K) {
                handleOpenTrashEvent.handle(keyEvent);
            } else if (keyEvent.isAltDown() && keyCode == KeyCode.T) {
                handleOpenControlEvent.handle(keyEvent);
            } else if (keyEvent.isControlDown() && keyCode == KeyCode.SLASH) {
                handleMoveToRootEvent1.handle(keyEvent);
            } else if (keyEvent.isControlDown() && keyCode == KeyCode.BACK_SLASH) {
                handleMoveToRootEvent1.handle(keyEvent);
            } else if (keyEvent.isAltDown() && keyCode == KeyCode.F7) {
                handleSearchEvent.handle(keyEvent);
            } else if (keyCode.equals(KeyCode.F3)) {
                handleObserveFilesEvent.handle(keyEvent);
            } else if (keyCode.equals(KeyCode.F4)) {
                handleEditFileEvent.handle(keyEvent);
            } else if (keyCode.equals(KeyCode.F5)) {
                handleCopyFilesEvent.handle(keyEvent);
            } else if (keyCode.equals(KeyCode.F6)) {
                handleRenameMoveEvent.handle(keyEvent);
            } else if (keyCode.equals(KeyCode.F7)) {
                handleCreateDirEvent.handle(keyEvent);
            } else if ((keyCode == KeyCode.F8) || (keyCode == KeyCode.DELETE)) {
                handleDeleteFilesSafeEvent.handle(keyEvent);
            } else if (keyEvent.getCode() == KeyCode.F9) {
                handleRefreshEvent.handle(keyEvent);
            } else if (keyCode == KeyCode.BACK_SPACE) {
                handleMoveToParentEvent.handle(keyEvent);
            }
        });
    }

    private final EventHandler<ActionEvent> handleStoryItemSelectEvent = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent e) {
            CheckMenuItem item = (CheckMenuItem) e.getSource();
            String path = item.getText();
            TCFile directory;
            try {
                directory = new TCFile(path);
                if (item.getId().contains(FileConstant.LEFT_ID)) {
                    new SelectStoryItemCommand(panelLeft, curPanel, item, directory, handleStoryItemSelectEvent).execute();
                } else {
                    new SelectStoryItemCommand(panelRight, curPanel, item, directory, handleStoryItemSelectEvent).execute();
                }
            } catch (IOException exception) {
                FileService.initAlert(main.getPrimaryStage(), FileConstant.APP_NAME, FileConstant.ERR_DIR, FileConstant.ERR_DIR_EXP, true);
                log.error(FileConstant.ERR_DIR);
            }
        }
    };

    private void setTableViewListener(TableView<TCFile> tableView, Label label1, Label label2, Label label3) {
        tableView.setOnMouseClicked(event -> {
            int filesCount = 0;
            int dirCount = 0;
            long size = 0L;
            ObservableList<TCFile> selectedFiles = tableView.getSelectionModel().getSelectedItems();
            for (TCFile file : selectedFiles) {
                if (file.isDirectory()) {
                    dirCount++;
                } else {
                    filesCount++;
                }
                size += file.length();
            }
            label1.setText("   " + size + " Кб из ");
            label2.setText("файлов: " + filesCount + " из ");
            label3.setText("папок: " + dirCount + " из ");
            if (!tableView.equals(curPanel.getTableView())) {
                curPanel.setUnselected();
                curPanel = curPanel.isActiveLeft() ? panelRight : panelLeft;
            }
            curPanel.setSelected();
        });
    }

    private void setRowListener(TableView<TCFile> tableView) {
        tableView.setRowFactory(tv -> {
            TableRow<TCFile> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) {
                    TCFile file = row.getItem();
                    if (file.isDirectory()) {
                        moveToDirectory(row, file);
                    } else {
                        openFile(file);
                    }
                }
            });
            return row;
        });
    }

    private final EventHandler<Event> handleSelectFilesEvent = e -> selectedItems = curPanel.getTableView().getSelectionModel().getSelectedItems();

    private final EventHandler<Event> handleCopyFilesEvent = e -> {
        ObservableList<TCFile> selectedFiles = curPanel.getTableView().getSelectionModel().getSelectedItems();
        int count = selectedFiles.size();
        String labelText;
        String path;
        if (count != 0) {
            labelText = count > 1 ? FileConstant.COPY_FILES + " (" + count + ") в:"
                    : FileConstant.COPY_FILE + selectedFiles.get(0).getName() + " в:";
            path = main.initCreateWindow(labelText, curPanel.getCurDir().getPath());
            try {
                if (!path.isEmpty()) {
                    new CopyCommand(curPanel, path, selectedFiles).execute();
                }
            } catch (IOException ioException) {
                FileService.initAlert(main.getPrimaryStage(), FileConstant.APP_NAME, FileConstant.ERR_COPY, FileConstant.ERR_COM_EXP, true);
                log.error(FileConstant.ERR_COPY);
            }
            FileService.refreshTableViewAfterCreateOperation(panelLeft, panelLeft.getCurDir());
            FileService.refreshTableViewAfterCreateOperation(panelRight, panelRight.getCurDir());
        }
    };

    private final EventHandler<Event> handleCopyFilesEvent1 = e -> {
        if (!selectedItems.isEmpty()) {
            try {
                new CopyCommand(curPanel, curPanel.getCurDir().getAbsolutePath(), selectedItems).execute();
                FileService.refreshTableViewAfterCreateOperation(panelLeft, panelLeft.getCurDir());
                FileService.refreshTableViewAfterCreateOperation(panelRight, panelRight.getCurDir());
            } catch (IOException ioException) {
                FileService.initAlert(main.getPrimaryStage(), FileConstant.APP_NAME, FileConstant.ERR_COPY, FileConstant.ERR_COM_EXP, true);
                log.error(FileConstant.ERR_COPY);
            }
        }
    };

    private final EventHandler<Event> handleZipDirEvent = e -> {
        ObservableList<TCFile> files = curPanel.getTableView().getSelectionModel().getSelectedItems();
        String path;
        String textField;
        TCFile file = files.get(0);
        try {
            if (files.size() == 1) {
                textField = file.isDirectory() ? file.getAbsolutePath() + FileConstant.ZIP_EXT : file.getParent() + "\\" + file.getName().substring(0, file.getName().indexOf(".")) + FileConstant.ZIP_EXT;
                path = main.initCreateWindow("Упаковать файлы (1 шт) в архив:", textField);
                files = (ObservableList<TCFile>) file.getFileList(file.listFiles());
            } else {
                textField = file.getParent() + FileConstant.ZIP_EXT;
                path = main.initCreateWindow("Упаковать файлы (" + files.size() + " шт) в архив:", textField);
            }
            new ZipCommand(curPanel, files, file, path).execute();
        } catch (IOException ioException) {
            FileService.initAlert(main.getPrimaryStage(), FileConstant.APP_NAME, FileConstant.ERR_ZIP, FileConstant.ERR_OPEN_EXP, true);
            log.error(FileConstant.ERR_ZIP);
        }
    };

    private final EventHandler<Event> handleUnzipDirEvent = e -> {
        TCFile file = curPanel.getTableView().getFocusModel().getFocusedItem();
        String path = main.initCreateWindow(FileConstant.UNZIP, file.getAbsolutePath().substring(0, file.getAbsolutePath().indexOf(FileConstant.ZIP_EXT)));
        try {
            new UnzipCommand(curPanel, path, file).execute();
        } catch (IOException | IllegalArgumentException exception) {
            FileService.initAlert(main.getPrimaryStage(), FileConstant.APP_NAME, FileConstant.ERR_UNZIP, FileConstant.ERR_UNZIP_EXP, true);
            log.error(FileConstant.ERR_UNZIP);
        }
    };

    private final EventHandler<Event> handleCreateFileEvent = e -> {
        String path = main.initCreateWindow(FileConstant.CREATE_FILE, "");
        try {
            new CreateCommand(curPanel, path, true).execute();
        } catch (IOException exception) {
            FileService.initAlert(main.getPrimaryStage(), FileConstant.APP_NAME, FileConstant.ERR_CREATE_FILE, FileConstant.ERR_OPEN_EXP, true);
            log.error(FileConstant.ERR_CREATE_FILE);
        }
    };

    private final EventHandler<Event> handleCreateDirEvent = e -> {
        String path = main.initCreateWindow(FileConstant.CREATE_DIR, "");
        try {
            new CreateCommand(curPanel, path, false).execute();
        } catch (IOException exception) {
            FileService.initAlert(main.getPrimaryStage(), FileConstant.APP_NAME, FileConstant.ERR_CREATE_DIR, FileConstant.ERR_CREATE_DIR_EXP, true);
            log.error(FileConstant.ERR_CREATE_DIR);
        }
    };

    private final EventHandler<Event> handleDeleteFilesSafeEvent = e -> {
        try {
            new DeleteCommand(panelLeft, panelRight, true).execute();
        } catch (IOException exception) {
            FileService.initAlert(main.getPrimaryStage(), FileConstant.APP_NAME, FileConstant.ERR_DEL, FileConstant.ERR_COM_EXP, true);
            log.error(FileConstant.ERR_DEL);
        }
    };

    private final EventHandler<Event> handleDeleteFilesHardEvent = e -> {
        try {
            new DeleteCommand(panelLeft, panelRight, false).execute();
        } catch (IOException exception) {
            FileService.initAlert(main.getPrimaryStage(), FileConstant.APP_NAME, FileConstant.ERR_DEL, FileConstant.ERR_COM_EXP, true);
            log.error(FileConstant.ERR_DEL);
        }
    };

    private final EventHandler<Event> handleForwardEvent = e -> {
        try {
            new MoveForwardCommand(curPanel, handleStoryItemSelectEvent).execute();
        } catch (IOException ignored) {
            log.error(FileConstant.ERR_DIR);
        }
    };

    private final EventHandler<Event> handleBackEvent = e -> {
        try {
            new MoveBackCommand(curPanel, handleStoryItemSelectEvent).execute();
        } catch (IOException ignored) {
            log.error(FileConstant.ERR_DIR);
        }
    };

    private final EventHandler<Event> handleRenameMoveEvent = e -> {
        TCFile file = curPanel.getTableView().getFocusModel().getFocusedItem();
        String path = main.initCreateWindow(FileConstant.COPY_RENAME, file.getAbsolutePath());
        try {
            if (!path.isEmpty() && file.isNotParent()) {
                new RenameMoveCommand(null, file, Paths.get(path)).execute();
            }
        } catch (IOException exception) {
            FileService.initAlert(main.getPrimaryStage(), FileConstant.APP_NAME, FileConstant.ERR_COPY_RENAME, FileConstant.ERR_COPY_RENAME_EXP, true);
            log.error(FileConstant.ERR_COPY_RENAME);
        }
    };

    private final EventHandler<Event> handleEditFileEvent = e -> {
        TCFile file = curPanel.getTableView().getFocusModel().getFocusedItem();
        try {
            new OpenCommand(null, Arrays.asList(FileConstant.CMD, FileConstant.C, FileConstant.START, file.getAbsolutePath())).execute();
        } catch (IOException exception) {
            FileService.initAlert(main.getPrimaryStage(), FileConstant.APP_NAME, FileConstant.ERR_EDIT, FileConstant.ERR_COM_EXP, true);
            log.error(FileConstant.ERR_EDIT);
        }
    };

    public final EventHandler<WindowEvent> handleCloseEvent = e -> {
        try {
            new SaveCommand(panelLeft, panelRight).execute();
        } catch (IOException exception) {
            log.error(FileConstant.ERR_CLOSE);
        }
    };

    private final EventHandler<Event> handleExitEvent = e -> {
        try {
            new SaveCommand(panelLeft, panelRight).execute();
        } catch (IOException exception) {
            log.error(FileConstant.ERR_CLOSE);
        }
        main.getPrimaryStage().close();
    };

    private final EventHandler<ActionEvent> handleMoveToDirectoryEvent = e -> {
        MenuItem item = (MenuItem) e.getSource();
        StringBuilder path = new StringBuilder(item.getText());
        Menu menu = item.getParentMenu();
        while (menu != null) {
            path.insert(0, menu.getText() + "\\");
            menu = menu.getParentMenu();
        }
        try {
            TCFile directory = new TCFile(homePath + "\\" + path);
            if (item.getId().contains(FileConstant.LEFT_ID)) {
                new MoveCommand(panelLeft, curPanel, directory, handleStoryItemSelectEvent).execute();
            } else {
                new MoveCommand(panelRight, curPanel, directory, handleStoryItemSelectEvent).execute();
            }
        } catch (IOException | NullPointerException exception) {
            FileService.initAlert(main.getPrimaryStage(), FileConstant.APP_NAME, FileConstant.ERR_DIR, FileConstant.ERR_DIR_EXP, true);
            log.error(FileConstant.ERR_DIR);
        }
    };

    private final EventHandler<ActionEvent> handleMoveToDiskEvent = e -> {
        MenuItem item = (MenuItem) e.getSource();
        String path = item.getText();
        TCFile directory;
        try {
            directory = new TCFile(path);
            if (item.getId().contains(FileConstant.LEFT_ID)) {
                new MoveCommand(panelLeft, curPanel, directory, handleStoryItemSelectEvent).execute();
            } else {
                new MoveCommand(panelRight, curPanel, directory, handleStoryItemSelectEvent).execute();
            }
        } catch (IOException | NullPointerException exception) {
            FileService.initAlert(main.getPrimaryStage(), FileConstant.APP_NAME, FileConstant.ERR_DISK, FileConstant.ERR_DISK_EXP, true);
            log.error(FileConstant.ERR_DISK);
        }
    };

    private final EventHandler<Event> handleObserveFilesEvent = e -> {
        TCFile file = curPanel.getTableView().getFocusModel().getFocusedItem();
        if (file.isDirectory()) {
            String text = FileService.getDirInfo(file);
            main.initListerText("Lister " + file.getPath(), text);
        } else {
            if (file.isImage()) {
                main.initListerImage("Lister " + file.getPath(), file.toURI().toString());
            } else {
                try {
                    new OpenCommand(null, Arrays.asList(FileConstant.CMD, FileConstant.C, FileConstant.START, file.getAbsolutePath())).execute();
                } catch (IOException ioException) {
                    FileService.initAlert(main.getPrimaryStage(), FileConstant.APP_NAME, FileConstant.ERR_OPEN, FileConstant.ERR_OPEN_EXP, true);
                    log.error(FileConstant.ERR_OPEN);
                }
            }
        }
    };

    private final EventHandler<ActionEvent> handleMoveToRootEvent = e -> {
        Button button = (Button) e.getTarget();
        try {
            if (button.getId().contains(FileConstant.LEFT_ID)) {
                TCFile root = new TCFile(Paths.get(panelLeft.getCurDir().getAbsolutePath()).getRoot().toString());
                new MoveCommand(panelLeft, curPanel, root, handleStoryItemSelectEvent).execute();
            } else {
                TCFile root = new TCFile(Paths.get(panelLeft.getCurDir().getAbsolutePath()).getRoot().toString());
                new MoveCommand(panelRight, curPanel, root, handleStoryItemSelectEvent).execute();
            }
        } catch (IOException | NullPointerException exception) {
            log.error(FileConstant.ERR_DIR);
        }
    };

    private final EventHandler<KeyEvent> handleMoveToRootEvent1 = e -> {
        try {
            if (curPanel.isActiveLeft()) {
                TCFile root = new TCFile(Paths.get(panelLeft.getCurDir().getAbsolutePath()).getRoot().toString());
                new MoveCommand(panelLeft, curPanel, root, handleStoryItemSelectEvent).execute();
            } else {
                TCFile root = new TCFile(Paths.get(panelRight.getCurDir().getAbsolutePath()).getRoot().toString());
                new MoveCommand(panelRight, curPanel, root, handleStoryItemSelectEvent).execute();
            }
        } catch (IOException | NullPointerException exception) {
            log.error(FileConstant.ERR_DIR);
        }
    };

    private final EventHandler<Event> handleMoveToParentEvent = e -> {
        Button button = (Button) e.getTarget();
        try {
            if (curPanel.getCurDir().getParent() != null) {
                TCFile parent = new TCFile(curPanel.getCurDir().getParent());
                if (button.getId().contains(FileConstant.LEFT_ID)) {
                    new MoveCommand(panelLeft, curPanel, parent, handleStoryItemSelectEvent).execute();
                } else {
                    new MoveCommand(panelRight, curPanel, parent, handleStoryItemSelectEvent).execute();
                }
            }
        } catch (IOException | NullPointerException exception) {
            log.error(FileConstant.ERR_DIR);
        }
    };

    private final EventHandler<Event> handleRefreshEvent = e -> {
        try {
            new RefreshCommand(curPanel, handleMoveToDiskEvent).execute();
        } catch (IOException exception) {
            log.error(FileConstant.ERR_REFRESH);
        }
    };

    private final EventHandler<Event> handleSearchEvent = e -> {
        Path resultPath = main.initSearchWindow(curPanel.getCurDir().getAbsolutePath());
        TCFile dir;
        if (resultPath != null) {
            try {
                dir = Files.isDirectory(resultPath) ? new TCFile(resultPath.toString()) : new TCFile(resultPath.getParent().toString());
                if (curPanel.isActiveLeft()) {
                    new MoveCommand(panelLeft, curPanel, dir, handleStoryItemSelectEvent).execute();
                } else {
                    TCFile file = new TCFile(resultPath.getParent().toString(), resultPath.getFileName().toString());
                    new MoveCommand(panelRight, curPanel, dir, handleStoryItemSelectEvent).execute();
                    curPanel.getTableView().getSelectionModel().select(file);
                }
            } catch (IOException | NullPointerException exception) {
                FileService.initAlert(main.getPrimaryStage(), FileConstant.APP_NAME, FileConstant.ERR_DIR, FileConstant.ERR_DIR_EXP, true);
                log.error(FileConstant.ERR_DIR);
            }
        }
    };

    private final EventHandler<Event> handleOpenCmdEvent = e -> {
        try {
            new OpenCommand(null, Arrays.asList(FileConstant.CMD, FileConstant.C, FileConstant.START)).execute();
        } catch (IOException exception) {
            FileService.initAlert(main.getPrimaryStage(), FileConstant.APP_NAME, FileConstant.ERR_CMD, FileConstant.ERR_COM_EXP, true);
            log.error(FileConstant.ERR_CMD);
        }
    };

    private final EventHandler<Event> handleOpenTrashEvent = e -> {
        try {
            new OpenCommand(null, Arrays.asList(FileConstant.CMD, FileConstant.C, FileConstant.OPEN_BIN)).execute();
        } catch (IOException exception) {
            FileService.initAlert(main.getPrimaryStage(), FileConstant.APP_NAME, FileConstant.ERR_TRASH, FileConstant.ERR_COM_EXP, true);
            log.error(FileConstant.ERR_TRASH);
        }
    };

    private final EventHandler<Event> handleOpenControlEvent = e -> {
        try {
            new OpenCommand(null, Arrays.asList(FileConstant.CMD, FileConstant.C, FileConstant.OPEN_CONTROL)).execute();
        } catch (IOException exception) {
            FileService.initAlert(main.getPrimaryStage(), FileConstant.APP_NAME, FileConstant.ERR_CONTROL, FileConstant.ERR_COM_EXP, true);
            log.error(FileConstant.ERR_CONTROL);
        }
    };

    private final EventHandler<Event> handleOpenNotepadEvent = e -> {
        try {
            new OpenCommand(null, Collections.singletonList(FileConstant.NOTEPAD)).execute();
        } catch (IOException exception) {
            FileService.initAlert(main.getPrimaryStage(), FileConstant.APP_NAME, FileConstant.ERR_NOTEPAD, FileConstant.ERR_COM_EXP, true);
            log.error(FileConstant.ERR_NOTEPAD);
        }
    };

    private final EventHandler<Event> handleHelpEvent = e -> main.initHelpWindow();

    private final EventHandler<Event> handleAboutEvent = e -> FileService.initAlert(main.getPrimaryStage(), FileConstant.ABOUT_TOTAL_COM, FileConstant.ABOUT, FileConstant.DESCRIPTION, false);

    private void moveToDirectory(TableRow<TCFile> row, TCFile file){
        try {
            if (row.getTableView().getId().contains(FileConstant.LEFT_ID)) {
                new MoveCommand(panelLeft, curPanel, file, handleStoryItemSelectEvent).execute();
            } else {
                new MoveCommand(panelRight, curPanel, file, handleStoryItemSelectEvent).execute();
            }
        } catch (IOException | NullPointerException e) {
            FileService.initAlert(main.getPrimaryStage(), FileConstant.APP_NAME, FileConstant.ERR_DIR, FileConstant.ERR_DIR_EXP, true);
            log.error(FileConstant.ERR_DIR);
        }
    }

    private void openFile(TCFile file){
        try {
            new OpenCommand(null, Arrays.asList(FileConstant.CMD, FileConstant.C, file.getAbsolutePath())).execute();
        } catch (IOException | NullPointerException e) {
            FileService.initAlert(main.getPrimaryStage(), FileConstant.APP_NAME, FileConstant.ERR_OPEN, FileConstant.ERR_OPEN, true);
            log.error(FileConstant.ERR_OPEN);
        }
    }
}