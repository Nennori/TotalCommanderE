package app.services;

import app.constants.FileConstant;
import app.models.TCFile;
import app.models.TCPanel;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class FileService implements Serializable {

    private static final Logger log = Logger.getLogger(FileService.class);

    public static void initAlert(Stage primaryStage, String title, String headerText, String contentText, boolean isWarning) {
        Alert alert = isWarning ? new Alert(Alert.AlertType.WARNING, contentText, ButtonType.OK) : new Alert(Alert.AlertType.INFORMATION, contentText, ButtonType.OK);
        alert.initOwner(primaryStage);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.showAndWait();
    }

    public static void copyTo(ObservableList<TCFile> selectedFiles, Path pathTo, String path) throws IOException {
        for (TCFile file : selectedFiles.filtered(TCFile::isNotParent)) {
            int index = 1;
            String name = file.getName().replace("." + file.getType(), "");
            String extension = file.getType();
            Path newPath = Paths.get(path + '\\' + file.getName());
            path = pathTo.toString();
            if (Files.exists(newPath)) {
                do {
                    newPath = Paths.get(path + '\\' + name + "(" + index + ")." + extension);
                    index++;
                } while (Files.exists(newPath));
            }
            if (file.isDirectory()) {
                FileUtils.copyDirectory(file, new File(newPath.toString()));
            } else {
                FileUtils.copyFile(file, new File(newPath.getParent().toString(), newPath.getFileName().toString()), true);
            }
        }
    }

    public static void deleteFiles(TableView<TCFile> table, boolean safe) throws IOException {
        ObservableList<TCFile> selectedDelItems = (table.getSelectionModel().getSelectedItems());
        if (!selectedDelItems.isEmpty()) {
            if (safe) {
                safeDelete(table, selectedDelItems);
            } else {
                hardDelete(table, selectedDelItems);
            }
        }
    }

    public static void safeDelete(TableView<TCFile> table, List<TCFile> selectedDelItems) throws IOException {
        com.sun.jna.platform.FileUtils fileUtils = com.sun.jna.platform.FileUtils.getInstance();
        if (fileUtils.hasTrash()) {
            for (TCFile file : selectedDelItems) {
                fileUtils.moveToTrash(file);
                table.getItems().remove(file);
            }
        }
    }

    public static void hardDelete(TableView<TCFile> table, List<TCFile> selectedDelItems) throws IOException {
        for (TCFile file : selectedDelItems) {
            if (file.isDirectory()) {
                FileUtils.deleteDirectory(file);
            } else {
                FileUtils.forceDelete(file);
            }
            table.getItems().remove(file);
        }
    }

    public static String getDirInfo(TCFile directory) {
        return directory.getAbsolutePath() + "\n\nОбщий размер файлов:\n"
                + TCFile.getDirSize(directory) + "байт.\n Всего файлов - "
                + TCFile.getFileCount(directory) + ",\nкаталогов - "
                + TCFile.getDirCount(directory) + ".\n\n";
    }

    public static void zip(List<TCFile> files, ZipOutputStream out, Path parentDir) throws IOException {
        Path filePath;
        for (TCFile f : files) {
            if (f.isDirectory()) {
                zip(f.getFileList(f.listFiles()), out, parentDir);
            } else {
                filePath = Paths.get(f.getPath());
                out.putNextEntry(new ZipEntry(parentDir.relativize(filePath).toString()));
                write(new FileInputStream(f), out);
            }
        }
    }

    public static void write(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int len;
        while ((len = in.read(buffer)) >= 0)
            out.write(buffer, 0, len);
        in.close();
    }

    public static void unzip(File file, String parentPath) {
        try (ZipFile zip = new ZipFile(file.getPath())) {
            Enumeration<? extends ZipEntry> entries = zip.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                if (entry.isDirectory()) {
                    new File(parentPath, entry.getName()).mkdirs();
                } else {
                    writeOut(zip.getInputStream(entry),
                            new BufferedOutputStream(new FileOutputStream(
                                    new File(parentPath, entry.getName()))));
                }
            }
        } catch (IOException e) {
            log.error(FileConstant.ERR_UNZIP);
        }
    }

    public static void writeOut(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int len;
        while ((len = in.read(buffer)) >= 0)
            out.write(buffer, 0, len);
        out.close();
        in.close();
    }

    public static void saveToFile(String filename, List<String> paths) throws IOException {
        try (FileOutputStream fileOutputStream = new FileOutputStream(filename, false); ObjectOutputStream objOut = new ObjectOutputStream(fileOutputStream)) {
            for (String path : paths) {
                objOut.writeObject(path);
            }
        }
    }

    public static List<String> readFromFile(String filename) {
        ArrayList<String> paths = new ArrayList<>();
        try (FileInputStream fInput = new FileInputStream(filename); ObjectInputStream objInput = new ObjectInputStream(fInput)) {
            String path;
            path = (String) objInput.readObject();
            while (path != null) {
                paths.add(path);
                path = (String) objInput.readObject();
            }
            return paths;
        } catch (IOException | ClassNotFoundException e) {
            return paths;
        }
    }

    public static void goToDirectory(TableView<TCFile> tableView, TCFile directory) throws IOException {
        String prevDir = directory.getParent();
        List<TCFile> files = directory.getFileList(directory.listFiles());
        tableView.getItems().clear();
        if (prevDir != null) {
            TCFile previousDir = createParentDir(prevDir);
            files.add(0, previousDir);
        }
        tableView.getItems().addAll(files);
    }

    public static void goAndUpdate(TCPanel panel, TCFile file) throws IOException {
        goToDirectory(panel.getTableView(), file);
        panel.setCurDir(file);
        panel.setLabelData(file);
        panel.setDiskData(new File(Paths.get(file.getPath()).getRoot().toString()));
        panel.getDiskMenuButton().setText(Paths.get(file.getPath()).getRoot().toString());
    }

    public static void refreshTableViewAfterCreateOperation(TCPanel panel, TCFile newFile) {
        if (newFile.getPath().contains(panel.getCurDir().getAbsolutePath())) {
            try {
                goAndUpdate(panel, panel.getCurDir());
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
    }

    public static void save(List<String> pathListLeft, List<String> pathListRight) {
        try {
            saveToFile(FileConstant.SAVE_FILE_1, pathListLeft);
            saveToFile(FileConstant.SAVE_FILE_2, pathListRight);
        } catch (IOException classNotFoundException) {
            classNotFoundException.printStackTrace();
        }
    }

    private static TCFile createParentDir(String prevDir) throws IOException {
        TCFile previousDir = new TCFile(prevDir);
        previousDir.setFilename("[...]");
        previousDir.setParentImage();
        previousDir.setParent(true);
        return previousDir;
    }

    protected static void createZip(String path, TCFile file, List<TCFile> files, TableView<TCFile> tableView) throws IOException {
        TCFile zip;
        if (path.isEmpty()) {
            return;
        }
        Path newPath = Paths.get(path).normalize();
        if (!newPath.isAbsolute()) {
            Path parentPath = Paths.get(file.getParent());
            newPath = parentPath.resolve(newPath);
        }
        zip = new TCFile(newPath.getParent().toString(), newPath.getFileName().toString(), true);
        if (!zip.exists()) {
            ZipOutputStream out;
            out = new ZipOutputStream(new FileOutputStream(zip));
            zip(files, out, Paths.get(file.getParent()));
            out.close();
            tableView.getItems().add(zip);
        }
    }

    public static void searchFiles(TCFile file, ObservableList<Path> files, String key, Integer max) throws IOException {
        Path path = Paths.get(file.getPath());
            String finalKey = key.toLowerCase();
            Files.walkFileTree(path, EnumSet.of(FileVisitOption.FOLLOW_LINKS), max, new FileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
                    if (dir.getFileName().toString().toLowerCase().contains(finalKey)) {
                        files.add(dir);
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    if (file.getFileName().toString().toLowerCase().contains(finalKey)) {
                        files.add(file);
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException e) {
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
                    return FileVisitResult.CONTINUE;
                }
            });
    }

}
