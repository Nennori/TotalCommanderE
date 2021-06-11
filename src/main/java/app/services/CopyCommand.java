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
        for (TCFile file : selectedFiles.filtered(tcFile -> !tcFile.isParent())) {
            int index = 1;
            String name = file.getName().replace("." + file.getType(), "");
            String extension = file.getType();
            Path newPath = Paths.get(path + "\\" + file.getName());
            path = pathTo.toString();
            if (Files.exists(newPath)) {
                do {
                    newPath = Paths.get(path + "\\" + name + "(" + index + ")." + extension);
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
}
