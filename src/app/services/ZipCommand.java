package app.services;

import app.models.TCFile;
import app.models.TCPanel;

import java.io.IOException;
import java.util.List;

public class ZipCommand extends Command {

    List<TCFile> files;
    TCFile file;
    String path;

    public ZipCommand(TCPanel panel, List<TCFile> files, TCFile file, String path) {
        super(panel);
        this.files = files;
        this.file = file;
        this.path = path;
    }

    @Override
    public void execute() throws IOException {
        FileService.createZip(path, file, files, panel.getTableView());
    }
}
