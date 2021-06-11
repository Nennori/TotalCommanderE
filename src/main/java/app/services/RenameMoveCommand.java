package app.services;

import app.models.TCFile;
import app.models.TCPanel;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class RenameMoveCommand extends Command {

    TCFile file;
    Path newPath;

    public RenameMoveCommand(TCPanel panel, TCFile file, Path newPath) {
        super(panel);
        this.file = file;
        this.newPath = newPath;
    }

    @Override
    public void execute() throws IOException {
        if (!file.renameTo(new File(newPath.getParent().toString(), newPath.getFileName().toString()))) {
            throw new IOException();
        }
    }
}