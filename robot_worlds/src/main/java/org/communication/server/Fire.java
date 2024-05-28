package org.communication.server;

public class Fire extends Command {


    @Override
    public boolean execute(Robot target) {
        target.fireShots();
        return false;
    }

    public Fire(String argument){
        super("fire", argument);
    }

}
