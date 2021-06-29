package app.services;

import app.models.TCFile;
import app.models.TCPanel;
import javafx.collections.ObservableList;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CopyCommand extends Command {

    String path;
    ObservableList<TCFile> selectedFiles;

    public CopyCommand(TCPanel panel, String path, ObservableList<TCFile> selectedFiles) {
        super(panel);
        this.path = path;
        this.selectedFiles = selectedFiles;
    }

    @Override
    public void execute() throws IOException {
        Path pathTo = Paths.get(path).toAbsolutePath();
        FileService.copyTo(selectedFiles, pathTo, path);

    }
}
