package app.services;

import app.models.TCPanel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class RefreshCommand extends Command {

    EventHandler<ActionEvent> handleMoveToDiskEvent;

    public RefreshCommand(TCPanel panel, EventHandler<ActionEvent> handleMoveToDiskEvent) {
        super(panel);
        this.handleMoveToDiskEvent = handleMoveToDiskEvent;
    }

    @Override
    public void execute() throws IOException {
        panel.getDiskMenuButton().getItems().clear();
        panel.createDiskMenuItems(Arrays.asList(File.listRoots()), panel.getDiskMenuButton().getId(), handleMoveToDiskEvent);
        FileService.goAndUpdate(panel, panel.getCurDir());
    }
}
