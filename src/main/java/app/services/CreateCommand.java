package app.services;

import app.models.TCFile;
import app.models.TCPanel;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CreateCommand extends Command {

    private String path;
    private boolean file;

    public CreateCommand(TCPanel panel, String path, boolean file) {
        super(panel);
        this.path = path;
        this.file = file;
    }

    @Override
    public void execute() throws IOException {
        TCFile newFile;
        if (!path.isEmpty()) {
            if (file) {
                newFile = createTCFile(Paths.get(path));
                newFile.createNewFile();
            } else {
                newFile = createTCFileDir(Paths.get(path));
                newFile.mkdirs();
            }
            panel.getTableView().getItems().add(newFile);
        }
    }

    private TCFile createTCFile(Path newPath) throws IOException {
        TCFile newFile;
        if (newPath.isAbsolute()) {
            newFile = new TCFile(newPath.getParent().toString(), newPath.getFileName().toString(), true);
        } else {
            newFile = new TCFile(panel.getCurDir().getAbsolutePath(), newPath.normalize().toString(), true);
        }
        return newFile;
    }

    private TCFile createTCFileDir(Path newPath) throws IOException {
        TCFile newDirectory;
        if (newPath.isAbsolute()) {
            newDirectory = new TCFile(newPath.toString(), true);
        } else {
            newDirectory = new TCFile(panel.getCurDir().getAbsolutePath() + "\\" + newPath.normalize().toString(), true);
        }
        return newDirectory;
    }
}
