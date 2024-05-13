package org.communication.server;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;

import static org.communication.client.SimpleClient.robotState;

public class SimpleServer implements Runnable {

    public static final int PORT = 5000;
    private final BufferedReader in;
    private final PrintStream out;
    private final String clientMachine;
    private static ArrayList<String> robotNames = new ArrayList<>(); // ArrayList to store robot names


    public SimpleServer(Socket socket) throws IOException {
        clientMachine = socket.getInetAddress().getHostName();

        out = new PrintStream(socket.getOutputStream());
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        System.out.println("Waiting for client...");

    }

    public void run() {
        Robot robot = null;

        try {
            String messageFromClient;
            // the loop continues as long as there are messages from the client
            while ((messageFromClient = in.readLine()) != null) {

                // if client message equals to quit loop terminates
                if (messageFromClient.equalsIgnoreCase("quit")) {
                    break;

                } else {
                    if (messageFromClient.toLowerCase().startsWith("launch")) {
                        String[] parts = messageFromClient.split(" ");
                        if (parts.length > 1) {
                            String robotName = parts[1]; // name is the second element

                            // check if the provided name does not occur twice in the world
                            if (!robotNames.contains(robotName)) {
                                robot = new Robot(robotName); // create new robot object

                                robotNames.add(robotName); // add the robots name to an array list
                                System.out.println(robotName + " just launched into the game!");
                            } else {
                                out.println("Sorry, too many of " + robotName+ " in this world");
                                continue;
                            }

                        } else {
                            // if no name provide inform client about invalid command
                            out.println("Invalid command. Please provide a name for the robot.");
                            continue; // Skip the rest of the loop iteration
                        }
                    } else {
                        if (robot == null) {
                            out.println("No robot has been launched. Please launch a robot first.");
                            continue; // Skip the rest of the loop iteration
                        }
                        // create a command object
                        Command command = Command.create(messageFromClient);
                        // execute the command
                        robot.handleCommand(command);
                    }
                }
                out.println(robot != null ? robot.toString() : "No robot is available.");
            }
        } catch (IOException ex) {
            System.out.println("Shutting down single client server");
        } finally {
            closeQuietly();
        }
    }

    private void closeQuietly() {
        try { in.close(); out.close();
        } catch(IOException ex) {}
    }

    public static void listObstacles(){
        World world = World.getInstance();
        for (Obstacle obs : world.obstacles){
            String printObstacle =String.format("There are some obstacles:\n- At position " + obs.getX() + ", " +  obs.getY() + " (to " +  (obs.getX()+4) + ", " + (obs.getY()+4) +")");
            System.out.println(printObstacle);
        }
    }


    public static void worldState(){
        System.out.println("Robot World is empty and the moment!!!");
    }

    public static void listRobots(){
        System.out.println("   ***List of Robots*** ");
        for (String robotName : robotNames){
            System.out.println("Robot name      : " + "<" + robotName.toUpperCase() + ">");
            robotState();
        }
    }

}