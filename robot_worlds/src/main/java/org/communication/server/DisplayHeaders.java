package org.communication.server;

public class DisplayHeaders {

    public static void displayRobotCommands(){
        System.out.println("\nRobot Commands");
        System.out.println("'launch' - launch a new robot into the world");
        System.out.println("'look' - Allows your robot to look around");
        System.out.println("'state' - View the current state of your robot\n");
        System.out.println("launch a robot to start the game: ");;
    }

    public static void displayHeaderRobot(){
        System.out.println("You have successfully connected to the game server!!!");
        System.out.println("\n**********************************************");
        System.out.println("       ### Welcome to Robot World ###");
        System.out.println("**********************************************");
    }

    public static void robotState(){
        System.out.println("State           : <READY FOR BATTLE>");
        System.out.println("Robot type      : <BATTLE BOT>");
        System.out.println("Shield strength : <MAXIMUM>");
        System.out.println("Shots           : <MAXIMUM>\n");
    }

}
