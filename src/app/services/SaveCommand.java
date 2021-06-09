package app.services;

import app.models.TCPanel;

import java.io.IOException;

public class SaveCommand extends Command {

    TCPanel panelRight;

    public SaveCommand(TCPanel panelLeft, TCPanel panelRight) {
        super(panelLeft);
        this.panelRight = panelRight;
    }

    @Override
    public void execute() throws IOException {
        FileService.save(panel.getPathList(), panelRight.getPathList());
    }
}
