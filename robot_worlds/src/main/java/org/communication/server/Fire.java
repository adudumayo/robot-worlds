package org.communication.server;

public class Fire extends Command {
    public static Robot damagedRobot;

    @Override
    public boolean execute(Robot target) {
        damagedRobot = target.fireShots();
        return false;
    }

    public Fire(String argument){
        super("fire", argument);
    }

}
