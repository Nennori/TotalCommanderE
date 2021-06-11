package app.models;

import app.controllers.FileController;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class TCPanel extends FileController {

    private final TableView<TCFile> tableView;
    private final MenuButton storyMenuButton;
    private final MenuButton dirMenuButton;
    private final MenuButton diskMenuButton;
    private final Label dirInfoLabelSize;
    private final Label dirInfoLabelSizeFree;
    private final Label dirInfoLabelFilesSelected;
    private final Label dirInfoLabelFiles;
    private final Label dirInfoLabelDirSelected;
    private final Label dirInfoLabelDir;
    private final Label diskInfoLabel;
    private TCFile curDir;
    private int curListStoryIndex;
    private final List<String> pathList;

    public TCPanel(TableView<TCFile> tableView, MenuButton storyMenuButton, MenuButton dirMenuButton, MenuButton diskMenuButton, Label dirInfoLabelSize, Label dirInfoLabelSizeFree, Label dirInfoLabelFilesSelected, Label dirInfoLabelFiles, Label dirInfoLabelDirSelected, Label dirInfoLabelDir, Label diskInfoLabel, TCFile curDir, List<String> pathList, int curListStoryIndex) {
        this.tableView = tableView;
        this.storyMenuButton = storyMenuButton;
        this.dirMenuButton = dirMenuButton;
        this.diskMenuButton = diskMenuButton;
        this.dirInfoLabelSize = dirInfoLabelSize;
        this.dirInfoLabelSizeFree = dirInfoLabelSizeFree;
        this.dirInfoLabelFilesSelected = dirInfoLabelFilesSelected;
        this.dirInfoLabelFiles = dirInfoLabelFiles;
        this.dirInfoLabelDirSelected = dirInfoLabelDirSelected;
        this.dirInfoLabelDir = dirInfoLabelDir;
        this.diskInfoLabel = diskInfoLabel;
        this.curDir = curDir;
        this.pathList = pathList;
        this.curListStoryIndex = curListStoryIndex;
    }

    public TableView<TCFile> getTableView() {
        return tableView;
    }

    public TCFile getCurDir() {
        return curDir;
    }

    public void setCurDir(TCFile curDir) {
        this.curDir = curDir;
    }

    public List<String> getPathList() {
        return pathList;
    }

    public int getCurListStoryIndex() {
        return curListStoryIndex;
    }

    public void setCurListStoryIndex(int curListStoryIndex) {
        this.curListStoryIndex = curListStoryIndex;
    }

    public MenuButton getDiskMenuButton() {
        return diskMenuButton;
    }

    public void setLabelData(TCFile file) {
        dirInfoLabelSizeFree.setText("   0 Кб из ");
        dirInfoLabelSize.setText(file.getDirLength() / 1024 + ", ");
        dirInfoLabelFilesSelected.setText("файлов: 0 из ");
        dirInfoLabelFiles.setText(file.getFiles().length + ", ");
        dirInfoLabelDirSelected.setText("папок: 0 из ");
        dirInfoLabelDir.setText(String.valueOf(file.getDirectories().length));
        dirMenuButton.setText(file.getAbsolutePath() + "\\*.*");
    }

    public boolean isActiveLeft() {
        return tableView.getId().contains("Left");
    }

    public void refreshStoryMenu(EventHandler<ActionEvent> eventHandler, int index) {
        int count = storyMenuButton.getItems().size();
        List<String> items;
        if (index + 9 < pathList.size()) {
            items = pathList.subList(index, index + 10);
            storyMenuButton.getItems().clear();
            createStoryMenuItems(items, eventHandler, 0);
        } else if (pathList.size() <= 10) {
            items = pathList;
            storyMenuButton.getItems().clear();
            createStoryMenuItems(items, eventHandler, index % count);
        }
    }

    public void updateStoryMenu(String path, EventHandler<ActionEvent> eventHandler) {
        pathList.add(0, path);
        if (pathList.size() > 100) {
            pathList.remove(pathList.size() - 1);
        }
        refreshStoryMenu(eventHandler, 0);
    }

    public int getIdItemSelected() {
        ObservableList<MenuItem> items = storyMenuButton.getItems();
        for (int i = items.size() - 1; i >= 0; i--) {
            CheckMenuItem checkMenuItem = (CheckMenuItem) items.get(i);
            if (checkMenuItem.isSelected()) {
                return items.size() - 1 - i;
            }
        }
        return 0;
    }

    public void setItemUnselected() {
        ObservableList<MenuItem> items = storyMenuButton.getItems();
        for (int i = items.size() - 1; i >= 0; i--) {
            CheckMenuItem checkMenuItem = (CheckMenuItem) items.get(i);
            if (checkMenuItem.isSelected()) {
                checkMenuItem.setSelected(false);
                return;
            }
        }
    }

    public void setDiskData(File file) {
        diskInfoLabel.setText("[" + FileSystemView.getFileSystemView().getSystemDisplayName(file) + "] " + file.getFreeSpace() / 1024 + " Кб из " + file.getTotalSpace() / 1024 + " Кб свободно");
    }

    public void createMenuItems(List<TCFile> items, String id, EventHandler<ActionEvent> eventHandler) {
        for (TCFile tcFile : items) {
            try {
                List<TCFile> submenus = tcFile.getFileList(tcFile.getDirectories());
                if (!submenus.isEmpty()) {
                    Menu menuItem = new Menu(tcFile.getName());
                    createSubmenuItems(submenus, menuItem, id, eventHandler);
                    menuItem.setId(id);
                    menuItem.setOnAction(eventHandler);
                    dirMenuButton.getItems().add(menuItem);
                } else {
                    MenuItem item = new MenuItem(tcFile.getName());
                    item.setOnAction(eventHandler);
                    item.setId(id);
                    dirMenuButton.getItems().add(item);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void createSubmenuItems(List<TCFile> items, Menu menu, String id, EventHandler<ActionEvent> eventHandler) throws IOException {
        for (TCFile tcFile : items) {
            List<TCFile> submenus = tcFile.getFileList(tcFile.getDirectories());
            if (!submenus.isEmpty()) {
                Menu menuItem = new Menu(tcFile.getName());
                createSubmenuItems(submenus, menuItem, id, eventHandler);
                menuItem.setId(id);
                menuItem.setOnAction(eventHandler);
                menu.getItems().add(menuItem);
            } else {
                MenuItem item = new MenuItem(tcFile.getName());
                item.setId(id);
                item.setOnAction(eventHandler);
                menu.getItems().add(item);
            }
        }
    }

    public void createDiskMenuItems(List<File> items, String id, EventHandler<ActionEvent> eventHandler) {
        diskMenuButton.setText(items.get(0).getPath());
        for (File file : items) {
            MenuItem item = new MenuItem(file.getPath());
            item.setId(id);
            item.setOnAction(eventHandler);
            diskMenuButton.getItems().addAll(item);
        }
    }

    public void setTableViewData(TableColumn<TCFile, String> imageTableColumn, TableColumn<TCFile, String> nameTableColumn, TableColumn<TCFile, String> typeTableColumn, TableColumn<TCFile, String> sizeTableColumn, TableColumn<TCFile, String> dateTableColumn){
        imageTableColumn.setCellValueFactory(new PropertyValueFactory<>("image"));
        nameTableColumn.setCellValueFactory(new PropertyValueFactory<>("filename"));
        typeTableColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        sizeTableColumn.setCellValueFactory(new PropertyValueFactory<>("size"));
        dateTableColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    public void createStoryMenu(EventHandler<ActionEvent> eventHandler) throws IOException {
        List<String> menuItems;
        if (pathList.isEmpty()) {
            File[] roots = File.listRoots();
            pathList.add(roots[0].getAbsolutePath());
            menuItems = new ArrayList<>();
            menuItems.add(roots[0].getAbsolutePath());
        } else {
            if (!Files.exists(Paths.get(pathList.get(0)))) {
                pathList.add(0, File.listRoots()[0].getAbsolutePath());
            }
            curDir = new TCFile(pathList.get(0));
            if (pathList.size() < 10) {
                menuItems = pathList.subList(0, pathList.size());
            } else {
                menuItems = pathList.subList(0, 10);
            }
        }
        createStoryMenuItems(menuItems, eventHandler, 0);
    }

    protected void createStoryMenuItems(List<String> items, EventHandler<ActionEvent> eventHandler, int index) {
        CheckMenuItem item;
        int i;
        for (i = items.size() - 1; i >= 0; i--) {
            item = new CheckMenuItem(items.get(i));
            item.setOnAction(eventHandler);
            item.setId(storyMenuButton.getId());
            if (i == index) {
                item.setSelected(true);
            }
            storyMenuButton.getItems().add(item);
        }
    }
}
