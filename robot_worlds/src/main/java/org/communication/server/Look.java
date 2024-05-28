package org.communication.server;
import static org.communication.server.MultiServers.setVisibility;
public class Look extends Command{

    @Override
    public boolean execute(Robot target) {
        target.look(setVisibility);
        System.out.println(target.allObstacles);
        return false;
    }

    public Look(String argument){
        super("look", argument);
    }

}
