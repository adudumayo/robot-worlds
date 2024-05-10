package org.communication.server;

public class LeftCommand extends Command {

    @Override
    public boolean execute(Robot target) {

        target.updateDirection(false);
        target.setStatus("Turned left.");
        return true;

    }

    public LeftCommand(String argument) {
        super("left", argument);
    }


}