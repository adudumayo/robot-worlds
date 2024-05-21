package org.communication.server;

public class BackCommand extends Command{

    @Override
    public boolean execute(Robot target) {
        int nrSteps = Integer.parseInt(getArgument());
        if (target.updatePosition(-nrSteps)){
            target.setStatus("Done");
        } else if(target.positionCheck) {
            target.setStatus("Obstructed!");
        } else if(target.pathCheck) {
            target.setStatus("Obstructed!");
        }else{
            target.setStatus("At the Edge!");
        }
        return true;
    }

    public BackCommand(String argument) {
        super("backward", argument);
    }

}
