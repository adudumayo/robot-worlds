package org.communication.server;

public class BackCommand extends Command{

    @Override
    public boolean execute(Robot target) {
        int nrSteps = Integer.parseInt(getArgument());
        if (target.updatePosition(-nrSteps)){
            target.setStatus("Moved back by "+nrSteps+" steps.");
        } else if(target.positionCheck) {
            target.setStatus("Sorry, there is an obstacle in way!");
        } else if(target.pathCheck) {
            target.setStatus("Sorry, there is an obstacle in way!");
        }else{
            target.setStatus("Sorry, I cannot go outside my safe zone.");
        }
        return true;
    }

    public BackCommand(String argument) {
        super("backward", argument);
    }

}
