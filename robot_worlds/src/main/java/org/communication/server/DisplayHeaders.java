package org.communication.server;

import static org.communication.server.SimpleServer.robotNames;

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
        System.out.println("\n*************************************************");
        System.out.println("  🤖✨ Welcome to the Amazing Robot World! ✨🤖");
        System.out.println("*************************************************");
    }

    public static void robotState(){
        System.out.println("State           : <READY FOR BATTLE>");
        System.out.println("Robot type      : <BATTLE BOT>");
        System.out.println("Shield strength : <MAXIMUM>");
        System.out.println("Shots           : <MAXIMUM>\n");
    }
    public static void displayHeader(){
        System.out.println("\n*************************************************");
        System.out.println("  🤖✨ Welcome to the Amazing Robot World! ✨🤖");
        System.out.println("*************************************************");
    }

    public static void displayMenu(){
        System.out.println("\nWorld Commands");
        System.out.println("'quit' - Disconnects all robots and ends the world ");
        System.out.println("'robots' - Lists all robots including the robot's name and state");
        System.out.println("'dump' - Displays a representation of the worlds state\n");
    }

    public static void displayObstaclesAndRobots(){
        World world = World.getInstance();
        System.out.println("There are some obstacles:");
        for (Obstacle obs : world.obstacles){
            String printObstacle =String.format("- At position " + obs.getX() + ", " +  obs.getY() + " (to " +  (obs.getX()+4) + ", " + (obs.getY()+4) +")");
            System.out.println(printObstacle);
        }
        System.out.println("Number robots in the world : " + robotNames.size());
    }

    public static void listRobots(){
        System.out.println("   ***List of Robots*** ");
        for (String robotName : robotNames){
            System.out.println("Robot name      : " + "<" + robotName.toUpperCase() + ">");
            robotState();
        }
    }

}
