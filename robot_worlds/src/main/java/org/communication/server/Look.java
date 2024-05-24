package org.communication.server;

public class Look extends Command{

    @Override
    public boolean execute(Robot target) {
        target.look();
        System.out.println(target.allObstacles);
        return false;
    }

    public Look(String argument){
        super("look", argument);
    }

}
