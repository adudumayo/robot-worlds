package org.communication.server;

import static org.communication.server.SimpleServer.robotNames;
import static org.communication.server.SimpleServer.robotObjects;

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
    public static void displayRobotModels(){
        System.out.println("Choose a Robot below:");
        System.out.println("1. Warpath");
        System.out.println("2. Demolisher");
        System.out.println("3. Reaper");
        System.out.println("4. Venom");
        System.out.println("5. Blaze\n");
        System.out.println("To join the game enter e.g 'launch reaper james'\n");
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
        for (Object obs : world.obstacles){
            if (obs instanceof Obstacle obstacle) {
                String printObstacle =String.format("- At position " + obstacle.getX() + ", " +  obstacle.getY() + " (to " +  (obstacle.getX()+4) + ", " + (obstacle.getY()+4) +")");
                System.out.println(printObstacle);
            }
        }
        System.out.println("\nNumber robots in the world : " + robotNames.size());
        for (Object obs : world.obstacles) {
            if (obs instanceof Robot robot) {
                System.out.println(robot);
            }
        }
    }
//    public static void displayObstaclesAndRobots() {
//        World world = World.getInstance();
//        System.out.println("There are some obstacles:");
//        for (Obstacle obs : world.obstacles) {
//            String printObstacle = String.format("- At position " + obs.getX() + ", " + obs.getY() + " (to " + (obs.getX() + 4) + ", " + (obs.getY() + 4) + ")");
//            System.out.println(printObstacle);
//        }
//        System.out.println("Number robots in the world : " + robotNames.size());
//
//        System.out.println("Robot names:");
//
//        for (Robot robot : robotObjects) {
//            System.out.println("- " + robot.getName() + " at position " + robot.coordinatePosition());
//        }
//
//    }

    public static void listRobots(){
        System.out.println("  ***List of Robots*** ");
        for (Robot robot : robotObjects){
            System.out.println("Name          : <" + robot.getName().toUpperCase() + ">\n" +
                               "Position      : <" + robot.coordinatePosition() + ">\n" +
                               "Direction     : <" + robot.getCurrentDirection() + ">\n" +
                               "Shields       : <" + robot.getState().getShields() + ">\n"+
                               "Shots         : <" + robot.getState().getShields() + ">\n" +
                               "Status        : <" + robot.getState().getStatus() + "\n");

        }
    }

}
