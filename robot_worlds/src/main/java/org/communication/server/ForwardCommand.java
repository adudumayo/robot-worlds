package org.communication.server;

public class ForwardCommand extends Command {
    @Override
    public boolean execute(Robot target) {
        int nrSteps = Integer.parseInt(getArgument());
        if (target.updatePosition(nrSteps)){
            target.setStatus("Done");
        } else if(target.positionCheck || target.positionCheckRobot) {
            target.setStatus("Obstructed!");
        } else if(target.pathCheck || target.pathCheckRobot) {
            target.setStatus("Obstructed!");
        }else{
            target.setStatus("At the Edge!");
        }
        return true;
    }

    public ForwardCommand(String argument) {
        super("forward", argument);
    }
}

