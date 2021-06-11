package app.services;

import app.models.TCFile;
import app.models.TCPanel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.CheckMenuItem;

import java.io.IOException;

public class SelectStoryItemCommand extends Command {

    CheckMenuItem item;
    TCPanel curPanel;
    TCFile directory;
    EventHandler<ActionEvent> handleStoryItemSelectEvent;

    public SelectStoryItemCommand(TCPanel panel, TCPanel curPanel, CheckMenuItem item, TCFile directory, EventHandler<ActionEvent> handleStoryItemSelectEvent) {
        super(panel);
        this.curPanel = curPanel;
        this.item = item;
        this.directory = directory;
        this.handleStoryItemSelectEvent = handleStoryItemSelectEvent;
    }

    @Override
    public void execute() throws IOException {
        panel.setItemUnselected();
        item.setSelected(true);
        int index = panel.getIdItemSelected();
        panel.setCurListStoryIndex(panel.getCurListStoryIndex() + index);
        curPanel = panel;
        FileService.goAndUpdate(panel, directory);
        panel.refreshStoryMenu(handleStoryItemSelectEvent, panel.getCurListStoryIndex());
    }
}
