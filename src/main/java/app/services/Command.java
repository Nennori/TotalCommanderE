package app.services;

import app.models.TCPanel;

import java.io.IOException;

public abstract class Command {

    protected TCPanel panel;
    public Command(TCPanel panel) {
        this.panel = panel;
    }
    public abstract void execute() throws IOException;
}
