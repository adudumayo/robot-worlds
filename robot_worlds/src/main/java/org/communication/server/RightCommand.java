package org.communication.server;

public class RightCommand extends Command {

    @Override
    public boolean execute(Robot target) {

        target.updateDirection(true);
        target.setStatus("Turned right.");
        return true;

    }

    public RightCommand(String argument){
        super("right", argument);
    }


}
