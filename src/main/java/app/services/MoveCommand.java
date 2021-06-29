package app.services;

import app.models.TCFile;
import app.models.TCPanel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.io.IOException;

public class MoveCommand extends Command {

    TCPanel curPanel;
    TCFile directory;
    EventHandler<ActionEvent> handleStoryItemSelectEvent;

    public MoveCommand(TCPanel panel, TCPanel curPanel, TCFile directory, EventHandler<ActionEvent> handleStoryItemSelectEvent) {
        super(panel);
        this.curPanel = curPanel;
        this.directory = directory;
        this.handleStoryItemSelectEvent = handleStoryItemSelectEvent;
    }

    @Override
    public void execute() throws IOException, NullPointerException {
        FileService.goAndUpdate(panel, directory);
        curPanel.setUnselected();
        curPanel = panel;
        curPanel.setSelected();
        panel.updateStoryMenu(directory.getAbsolutePath(), handleStoryItemSelectEvent);
    }
}
