package app.controllers;


import app.Main;
import app.models.TCFile;
import app.models.TCPanel;
import app.services.*;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import app.views.View;
import javafx.scene.input.KeyEvent;
import javafx.stage.WindowEvent;

public class FileController extends View implements Initializable {

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
    public TCFile curDirLeft;
    public TCFile curDirRight;
    public ArrayList<String> pathListLeft;
    public ArrayList<String> pathListRight;
    private Main main;
    private String homePath;
    private TCPanel panelLeft;
    private TCPanel panelRight;
    private TCPanel curPanel;
    protected int curListStoryLeftIndex = 0;
    protected int curListStoryRightIndex = 0;

    public void setMainApp(Main main) {
        this.main = main;
    }

    public FileController() {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        File[] roots = File.listRoots();
        homePath = System.getProperty("user.home") + "/Desktop";
        try {
            pathListLeft = FileService.ReadFromFile("totalcom1.bin");
            pathListRight = FileService.ReadFromFile("totalcom2.bin");
            panelLeft = new TCPanel(tableViewLeft, storyMenuButtonLeft, dirMenuButtonLeft, diskMenuButtonLeft,
                    dirInfoLabelSizeLeft, dirInfoLabelSizeFreeLeft, dirInfoLabelFilesSelectedLeft,
                    dirInfoLabelFilesLeft, dirInfoLabelDirSelectedLeft, dirInfoLabelDirLeft, diskInfoLabelLeft, curDirLeft, pathListLeft, curListStoryLeftIndex);
            panelRight = new TCPanel(tableViewRight, storyMenuButtonRight, dirMenuButtonRight,
                    diskMenuButtonRight, dirInfoLabelSizeRight, dirInfoLabelSizeFreeRight,
                    dirInfoLabelFilesSelectedRight, dirInfoLabelFilesRight, dirInfoLabelDirSelectedRight,
                    dirInfoLabelDirRight, diskInfoLabelRight, curDirRight, pathListRight, curListStoryRightIndex);
            panelLeft.createStoryMenu(handleStoryItemSelectEvent);
            panelRight.createStoryMenu(handleStoryItemSelectEvent);
            TCFile homeDir = new TCFile(System.getProperty("user.home") + "\\Desktop");
            panelLeft.createMenuItems(homeDir.getFileList(homeDir.getDirectories()), "Left", handleMoveToDirectoryEvent);
            panelRight.createMenuItems(homeDir.getFileList(homeDir.getDirectories()), "Right", handleMoveToDirectoryEvent);
            panelLeft.setTableViewData(imageTableColumnLeft, nameTableColumnLeft, typeTableColumnLeft, sizeTableColumnLeft, dateTableColumnLeft);
            panelRight.setTableViewData(imageTableColumnRight, nameTableColumnRight, typeTableColumnRight, sizeTableColumnRight, dateTableColumnRight);
            panelLeft.setDiskData(roots[0]);
            panelRight.setDiskData(roots[0]);
            panelLeft.createDiskMenuItems(Arrays.asList(roots.clone()), "Left", handleMoveToDiskEvent);
            panelRight.createDiskMenuItems(Arrays.asList(roots.clone()), "Right", handleMoveToDiskEvent);
            setListeners();
            setHandlers();
            curPanel = panelLeft;
            FileService.goAndUpdate(panelLeft, panelLeft.getCurDir());
            FileService.goAndUpdate(panelRight, panelRight.getCurDir());
        } catch (IOException | ClassNotFoundException ioException) {
            ioException.printStackTrace();
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
        packMenuItem.addEventHandler(Event.ANY, handleZipDirEvent);//?
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
                curPanel = curPanel.isActiveLeft() ? panelRight : panelLeft;
            }
        });
    }

    private void setRowListener(TableView<TCFile> tableView) {
        tableView.setRowFactory(tv -> {
            TableRow<TCFile> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) {
                    TCFile file = row.getItem();
                    if (file.isDirectory()) {
                        try {
                            if (row.getTableView().getId().contains("Left")) {
                                new MoveCommand(panelLeft, curPanel, file, handleStoryItemSelectEvent).execute();
                            } else {
                                new MoveCommand(panelRight, curPanel, file, handleStoryItemSelectEvent).execute();
                            }
                        } catch (IOException e) {
                            FileService.initAlert(main.getPrimaryStage(), "Emulator Total Commander", "Не удалось перейти в директорию", "Директория не существует или программа не имеет прав данную операцию", true);
                        }
                    } else {
                        try {
                            new OpenCommand(null, Arrays.asList("cmd.exe", "/c", file.getAbsolutePath())).execute();
                        } catch (IOException e) {
                            FileService.initAlert(main.getPrimaryStage(), "Emulator Total Commander", "Ошибка открытия файла", "Не удалось открфть файл", true);
                        }
                    }
                }
            });
            return row;
        });
    }

    //Handlers

    public EventHandler<Event> handleSelectFilesEvent = new EventHandler<Event>() {
        @Override
        public void handle(Event e) {
            selectedItems = curPanel.getTableView().getSelectionModel().getSelectedItems();
        }
    };

    public EventHandler<Event> handleCopyFilesEvent = new EventHandler<Event>() {
        @Override
        public void handle(Event e) {
            ObservableList<TCFile> selectedFiles = curPanel.getTableView().getSelectionModel().getSelectedItems();
            int count = selectedFiles.size();
            String labelText;
            String path;
            if (count != 0) {
                if (count > 1) {
                    labelText = "Копировать файлы (" + count + ") в:";
                } else {
                    labelText = "Копировать файл " + selectedFiles.get(0).getName() + " в:";
                }
                path = main.initCreateWindow(labelText, curPanel.getCurDir().getPath());
                try {
                    if (!path.isEmpty()) {
                        new CopyCommand(curPanel, path, selectedFiles).execute();
                    }
                } catch (IOException ioException) {
                    FileService.initAlert(main.getPrimaryStage(), "Emulator Total Commander", "Не удалось копировать файл(-ы)", "Программа не имеет прав на данную операцию", true);
                }
                FileService.refreshTableViewAfterCreateOperation(panelLeft, panelLeft.getCurDir());
                FileService.refreshTableViewAfterCreateOperation(panelRight, panelRight.getCurDir());
            }
        }
    };

    public EventHandler<Event> handleCopyFilesEvent1 = new EventHandler<Event>() {
        @Override
        public void handle(Event e) {
            if (selectedItems.size() != 0) {
                try {
                    new CopyCommand(curPanel, curPanel.getCurDir().getAbsolutePath(), selectedItems).execute();
                    FileService.refreshTableViewAfterCreateOperation(panelLeft, panelLeft.getCurDir());
                    FileService.refreshTableViewAfterCreateOperation(panelRight, panelRight.getCurDir());
                } catch (IOException ioException) {
                    FileService.initAlert(main.getPrimaryStage(), "Emulator Total Commander", "Не удалось копировать файл(-ы)", "Программа не имеет прав на данную операцию", true);
                }
            }
        }
    };

    public EventHandler<Event> handleZipDirEvent = new EventHandler<Event>() {
        @Override
        public void handle(Event e) {
            ObservableList<TCFile> files = curPanel.getTableView().getSelectionModel().getSelectedItems();
            String path;
            String textField;
            TCFile file = files.get(0);
            try {
                if (files.size() == 1) {
                    textField = file.isDirectory() ? file.getAbsolutePath() + ".zip" : file.getParent() + "\\" + file.getName().substring(0, file.getName().indexOf(".")) + ".zip";
                    path = main.initCreateWindow("Упаковать файлы (1 шт) в архив:", textField);
                    files = (ObservableList<TCFile>) file.getFileList(file.listFiles());
                } else {
                    textField = file.getParent() + ".zip";
                    path = main.initCreateWindow("Упаковать файлы (" + files.size() + " шт) в архив:", textField);
                }
                new ZipCommand(curPanel, files, file, path).execute();
            } catch (IOException ioException) {
                FileService.initAlert(main.getPrimaryStage(), "Emulator Total Commander", "Can't zip file", "File does not exist or program has no permission to zip the file", true);
            }
        }
    };

    public EventHandler<Event> handleUnzipDirEvent = new EventHandler<Event>() {
        @Override
        public void handle(Event e) {
            TCFile file = curPanel.getTableView().getFocusModel().getFocusedItem();
            String path = main.initCreateWindow("Распаковать архив в:", file.getAbsolutePath().substring(0, file.getAbsolutePath().indexOf(".zip")));
            try {
                new UnzipCommand(curPanel, path, file).execute();
            } catch (IOException | IllegalArgumentException exception) {
                FileService.initAlert(main.getPrimaryStage(), "Zip Emulator Total Commander", "File is not a zip", "Choose zip file", true);
            }
        }
    };

    public EventHandler<Event> handleCreateFileEvent = new EventHandler<Event>() {
        @Override
        public void handle(Event event) {
            String path = main.initCreateWindow("Создать новый файл:", "");
            try {
                new CreateCommand(curPanel, path, true).execute();
            } catch (IOException exception) {
                FileService.initAlert(main.getPrimaryStage(), "Emulator Total Commander", "Не удалось создать файл", "Файл уже существует или программа не имеет прав на данную операцию", true);
            }
        }
    };

    public EventHandler<Event> handleCreateDirEvent = new EventHandler<Event>() {
        @Override
        public void handle(Event event) {
            String path = main.initCreateWindow("Создать новый каталог:", "");
            try {
                new CreateCommand(curPanel, path, false).execute();
            } catch (IOException exception) {
                FileService.initAlert(main.getPrimaryStage(), "Emulator Total Commander", "Не удалось создать файл", "Файл уже существует или программа не имеет прав на данную операцию", true);
            }
        }
    };

    public EventHandler<Event> handleDeleteFilesSafeEvent = new EventHandler<Event>() {
        @Override
        public void handle(Event event) {
            try {
                new DeleteCommand(panelLeft, panelRight, true).execute();
            } catch (IOException e) {
                FileService.initAlert(main.getPrimaryStage(), "Emulator Total Commander", "Не удалось удалить файл(-ы)", "Программа не имеет прав на данную операцию", true);
            }
        }
    };

    public EventHandler<Event> handleDeleteFilesHardEvent = new EventHandler<Event>() {
        @Override
        public void handle(Event event) {
            try {
                new DeleteCommand(panelLeft, panelRight, false).execute();
            } catch (IOException e) {
                FileService.initAlert(main.getPrimaryStage(), "Emulator Total Commander", "Не удалось удалить файл(-ы)", "Программа не имеет прав на данную оперецию", true);
            }
        }
    };

    public EventHandler<Event> handleForwardEvent = new EventHandler<Event>() {
        @Override
        public void handle(Event e) {
            try {
                new MoveForwardCommand(curPanel, handleStoryItemSelectEvent).execute();
            } catch (IOException ignored) {
            }
        }
    };

    public EventHandler<Event> handleBackEvent = new EventHandler<Event>() {
        @Override
        public void handle(Event e) {
            try {
                new MoveBackCommand(curPanel, handleStoryItemSelectEvent).execute();
            } catch (IOException ignored) {
            }
        }
    };

    public EventHandler<Event> handleRenameMoveEvent = new EventHandler<Event>() {
        @Override
        public void handle(Event e) {
            TCFile file = curPanel.getTableView().getFocusModel().getFocusedItem();
            String path = main.initCreateWindow("Переименовать/переместить файл в:", file.getAbsolutePath());
            try {
                if (!file.isParent()) {
                    new RenameMoveCommand(null, file, Paths.get(path)).execute();
                }
            } catch (IOException exception) {
                FileService.initAlert(main.getPrimaryStage(), "Emulator Total Commander", "Не удалось переименовать/переместить файл", "Файл с таким именем уже существует", true);
            }
        }
    };

    public EventHandler<Event> handleEditFileEvent = new EventHandler<Event>() {
        @Override
        public void handle(Event e) {
            TCFile file = curPanel.getTableView().getFocusModel().getFocusedItem();
            try {
                new OpenCommand(null, Arrays.asList("cmd.exe", "/c", "start", file.getAbsolutePath())).execute();
            } catch (IOException exception) {
                FileService.initAlert(main.getPrimaryStage(), "Emulator Total Commander", "Не удалось открыть файл для редактирования", "Программа не имеет прав на данную операцию", true);
            }
        }
    };

    public EventHandler<WindowEvent> handleCloseEvent = e -> {
        try {
            new SaveCommand(panelLeft, panelRight).execute();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    };

    public EventHandler<Event> handleExitEvent = new EventHandler<Event>() {
        @Override
        public void handle(Event e) {
            try {
                new SaveCommand(panelLeft, panelRight).execute();
            } catch (IOException ignored) {
            }
            main.getPrimaryStage().close();
        }
    };

    public EventHandler<ActionEvent> handleMoveToDirectoryEvent = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent e) {
            MenuItem item = (MenuItem) e.getSource();
            StringBuilder path = new StringBuilder(item.getText());
            Menu menu = item.getParentMenu();
            while (menu != null) {
                path.insert(0, menu.getText() + "\\");
                menu = menu.getParentMenu();
            }
            try {
                TCFile directory = new TCFile(homePath + "\\" + path);
                if (item.getId().contains("Left")) {
                    new MoveCommand(panelLeft, curPanel, directory, handleStoryItemSelectEvent).execute();
                } else {
                    new MoveCommand(panelRight, curPanel, directory, handleStoryItemSelectEvent).execute();
                }
            } catch (IOException ioException) {
                FileService.initAlert(main.getPrimaryStage(), "Emulator Total Commander", "Не удалось перейти в директорию", "Директория не существует или программа не имеет прав данную операцию", true);
            }
        }
    };

    public EventHandler<ActionEvent> handleMoveToDiskEvent = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent e) {
            MenuItem item = (MenuItem) e.getSource();
            String path = item.getText();
            TCFile directory;
            try {
                directory = new TCFile(path);
                if (item.getId().contains("Left")) {
                    new MoveCommand(panelLeft, curPanel, directory, handleStoryItemSelectEvent).execute();
                } else {
                    new MoveCommand(panelRight, curPanel, directory, handleStoryItemSelectEvent).execute();
                }
            } catch (IOException ioException) {
                FileService.initAlert(main.getPrimaryStage(), "Emulator Total Commander", "Не удалось перейти на диск", "Выберите другой диск", true);
            }
        }
    };

    public EventHandler<Event> handleObserveFilesEvent = new EventHandler<Event>() {
        @Override
        public void handle(Event e) {
            TCFile file = curPanel.getTableView().getFocusModel().getFocusedItem();
            if (file.isDirectory()) {
                String text = FileService.getDirInfo(file);
                main.initListerText("Lister " + file.getPath(), text);
            } else {
                if (file.isImage()) {
                    main.initListerImage("Lister " + file.getPath(), file.toURI().toString());
                } else {
                    try {
                        new OpenCommand(null, Arrays.asList("cmd.exe", "/c", "start", file.getAbsolutePath())).execute();
                    } catch (IOException ioException) {
                        FileService.initAlert(main.getPrimaryStage(), "Emulator Total Commander", "Can't open the file", "The file is not exist or program has no permission to open the file.", true);
                    }
                }
            }
        }
    };

    public EventHandler<ActionEvent> handleMoveToRootEvent = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent e) {
            Button button = (Button) e.getTarget();
            try {
                if (button.getId().contains("Left")) {
                    TCFile root = new TCFile(Paths.get(panelLeft.getCurDir().getAbsolutePath()).getRoot().toString());
                    new MoveCommand(panelLeft, curPanel, root, handleStoryItemSelectEvent).execute();
                } else {
                    TCFile root = new TCFile(Paths.get(panelLeft.getCurDir().getAbsolutePath()).getRoot().toString());
                    new MoveCommand(panelRight, curPanel, root, handleStoryItemSelectEvent).execute();
                }
            } catch (IOException ignored) {
            }
        }
    };

    public EventHandler<KeyEvent> handleMoveToRootEvent1 = new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent e) {
            try {
                if (curPanel.isActiveLeft()) {
                    TCFile root = new TCFile(Paths.get(panelLeft.getCurDir().getAbsolutePath()).getRoot().toString());
                    new MoveCommand(panelLeft, curPanel, root, handleStoryItemSelectEvent).execute();
                } else {
                    TCFile root = new TCFile(Paths.get(panelRight.getCurDir().getAbsolutePath()).getRoot().toString());
                    new MoveCommand(panelRight, curPanel, root, handleStoryItemSelectEvent).execute();
                }
            } catch (IOException ignored) {
            }
        }
    };

    public EventHandler<Event> handleMoveToParentEvent = new EventHandler<Event>() {
        @Override
        public void handle(Event e) {
            Button button = (Button) e.getTarget();
            try {
                if (curPanel.getCurDir().getParent() != null) {
                    TCFile parent = new TCFile(curPanel.getCurDir().getParent());
                    if (button.getId().contains("Left")) {
                        new MoveCommand(panelLeft, curPanel, parent, handleStoryItemSelectEvent).execute();
                    } else {
                        new MoveCommand(panelRight, curPanel, parent, handleStoryItemSelectEvent).execute();
                    }
                }
            } catch (IOException ignored) {
            }
        }
    };

    public EventHandler<Event> handleRefreshEvent = new EventHandler<Event>() {
        @Override
        public void handle(Event e) {
            try {
                new RefreshCommand(curPanel, handleMoveToDiskEvent).execute();
            } catch (IOException ignored) {
            }
        }
    };

    public EventHandler<ActionEvent> handleStoryItemSelectEvent = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent e) {
            CheckMenuItem item = (CheckMenuItem) e.getSource();
            String path = item.getText();
            TCFile directory;
            try {
                directory = new TCFile(path);
                if (item.getId().contains("Left")) {
                    new SelectStoryItemCommand(panelLeft, curPanel, item, directory, handleStoryItemSelectEvent).execute();
                } else {
                    new SelectStoryItemCommand(panelRight, curPanel, item, directory, handleStoryItemSelectEvent).execute();
                }
            } catch (IOException ioException) {
                FileService.initAlert(main.getPrimaryStage(), "Emulator Total Commander", "Can't go to the directory", "The directory does not exist or program has no permission to go.", true);
            }
        }
    };

    public EventHandler<Event> handleSearchEvent = new EventHandler<Event>() {
        @Override
        public void handle(Event e) {
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
                } catch (IOException ioException) {
                    FileService.initAlert(main.getPrimaryStage(), "Emulator Total Commander", "Can't go to the directory", "The directory does not exist or program has no permission to go.", true);
                }
            }
        }
    };

    public EventHandler<Event> handleOpenCmdEvent = e -> {
        try {
            new OpenCommand(null, Arrays.asList("cmd.exe", "/c", "start")).execute();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    };

    public EventHandler<Event> handleOpenTrashEvent = e -> {
        try {
            new OpenCommand(null, Arrays.asList("cmd.exe", "/c", "start shell:RecycleBinFolder")).execute();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    };

    public EventHandler<Event> handleOpenControlEvent = e -> {
        try {
            new OpenCommand(null, Arrays.asList("cmd.exe", "/c", "start shell:RecycleBinFolder")).execute();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    };

    public EventHandler<Event> handleOpenNotepadEvent = new EventHandler<Event>() {
        @Override
        public void handle(Event event) {
            try {
                new OpenCommand(null, Collections.singletonList("Notepad.exe")).execute();
            } catch (IOException e) {
                FileService.initAlert(main.getPrimaryStage(), "Emulator Total Commander", "Can't open Notepad", "Error opening Notepad", true);
            }
        }
    };

    public EventHandler<Event> handleHelpEvent = new EventHandler<Event>() {
        @Override
        public void handle(Event e) {
            main.initHelpWindow();
        }
    };

    public EventHandler<Event> handleAboutEvent = new EventHandler<Event>() {
        @Override
        public void handle(Event e) {
            FileService.initAlert(main.getPrimaryStage(), "O программе Emulator Total Commander", "О программе", "Emulator Total Commander, версия 1.0\nАвтор: Жук А.Д.", false);
        }
    };
}