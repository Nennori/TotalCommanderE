package app.services;

import app.models.TCFile;
import app.models.TCPanel;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class UnzipCommand extends Command {

    String path;
    TCFile file;

    public UnzipCommand(TCPanel panel, String path, TCFile file) {
        super(panel);
        this.path = path;
        this.file = file;
    }

    @Override
    public void execute() throws IOException {
        Path newPath = Paths.get(path).normalize();
        if (!newPath.isAbsolute()) {
            Path parentPath = Paths.get(file.getParent());
            newPath = parentPath.resolve(newPath);
        }
        FileService.unzip(file, newPath.getParent().toString());
    }
}
