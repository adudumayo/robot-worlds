package org.communication.server;

public abstract class Command {
    private final String name;
    private String argument;

    public abstract boolean execute(Robot target);

    public Command(String name){
        this.name = name.trim().toLowerCase();
        this.argument = "";
    }
    public Command(String name, String argument) {
        this(name);
        this.argument = argument.trim();
    }
    public String getName() {                                                                           //<2>
        return name;
    }

    public String getArgument() {
        return this.argument;
    }

    public static Command create(String instruction) {

        String[] args = instruction.toLowerCase().trim().split(" ");

        switch (args[0]){

            case "forward":
                return new ForwardCommand(args[1]);
            case "back":
                return new BackCommand(args[1]);
            case "right":
                return new RightCommand(args[0]);
            case "left":
                return new LeftCommand(args[0]);
            case "look":
                return new Look(args[0]);
            case "fire":
                return new Fire(args[0]);
            default:
                throw new IllegalArgumentException("Unsupported command: " + instruction);
        }
    }
}

