package app.services;

import app.models.TCPanel;

import java.io.IOException;

public class DeleteCommand extends Command {

    TCPanel panelRight;
    boolean safe;

    public DeleteCommand(TCPanel panel, TCPanel panelRight, boolean safe) {
        super(panel);
        this.panelRight = panelRight;
        this.safe = safe;
    }

    @Override
    public void execute() throws IOException {
        FileService.deleteFiles(panel.getTableView(), safe);
        FileService.deleteFiles(panelRight.getTableView(), safe);
    }
}
