package app.services;

import app.models.TCFile;
import app.models.TCPanel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.io.IOException;

public class MoveForwardCommand extends Command {

    EventHandler<ActionEvent> handleStoryItemSelectEvent;

    public MoveForwardCommand(TCPanel panel, EventHandler<ActionEvent> handleStoryItemSelectEvent) {
        super(panel);
        this.handleStoryItemSelectEvent = handleStoryItemSelectEvent;
    }

    @Override
    public void execute() throws IOException {
        int index = panel.getCurListStoryIndex() - 1;
        if (index >= 0) {
            panel.setItemUnselected();
            String path = panel.getPathList().get(index);
            TCFile directory = new TCFile(path);
            FileService.goAndUpdate(panel, directory);
            panel.refreshStoryMenu(handleStoryItemSelectEvent, index);
            panel.setCurListStoryIndex(index);
        }
    }
}
