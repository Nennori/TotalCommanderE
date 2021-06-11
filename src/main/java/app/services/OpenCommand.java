package app.services;

import app.models.TCPanel;

import java.io.IOException;
import java.util.List;

public class OpenCommand extends Command {

    List<String> commands;

    public OpenCommand(TCPanel panel, List<String> commands) {
        super(panel);
        this.commands = commands;
    }

    @Override
    public void execute() throws IOException {
        ProcessBuilder pb = new ProcessBuilder(commands);
        pb.start();
    }
}
