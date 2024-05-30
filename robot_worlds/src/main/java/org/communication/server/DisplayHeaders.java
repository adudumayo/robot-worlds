package org.communication.server;

import com.google.gson.*;

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
        System.out.println("\n**********************************************************************");
        System.out.println("            🤖✨ Welcome to the Amazing Robot World! ✨🤖");
        System.out.println("**********************************************************************");
    }
    public static void displayWaitingForConnections(){
        System.out.println("\t\tServer running & waiting for client connections.");
        System.out.println("-----------------------------------------------------------------------");
    }
    public static void displayWorldCommands(){
        System.out.println("\nWorld Commands");
        System.out.println("'quit' - Disconnects all robots and ends the world ");
        System.out.println("'robots' - Lists all robots including the robot's name and state");
        System.out.println("'dump' - Displays a representation of the worlds state\n");
    }
    public static void configMenu(){
        System.out.println("\nLet's start by configuring the world size." +
                "\nEnter the top left and bottom right coordinates to create the world " +
                "(\ne.g. (-200,200).");
    }

    public static void displayMenu(){
        System.out.println("\n   \t\t\t\t Welcome to your new robot world " +
                "\n\t   Crafted with a default size of 200 clicks by 200 clicks.\n" +
                "\tEach robot has a standard 50-clicks-per-direction view radius!\n");
        System.out.println("\t\t\t\t  Want to customize your world? \n " +
                "\t\t\t Type 'config' to tweak the settings, or\n " +
                "\t\t Press 's' to embark on your exciting adventure! \n \t\t\t\t\t   " +
                "Press 'q' to quit");
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

    public static void displayServerResponse(String serverResponse) {
        if (serverResponse == null || serverResponse.trim().isEmpty()) {
            System.out.println("Received an empty or null JSON response.");
            return;
        }

        try {
            Gson gson = new Gson();
            JsonElement jsonElement = gson.fromJson(serverResponse, JsonElement.class);

            if (jsonElement.isJsonObject()) {
                JsonObject jsonObject = jsonElement.getAsJsonObject();

                // Unpack and print data with safety checks
                if (jsonObject.has("result")) {
                    String result = jsonObject.get("result").getAsString();
                    System.out.println("Result: " + result);
                }

                JsonObject data = jsonObject.has("data") ? jsonObject.getAsJsonObject("data") : null;
                JsonObject state = jsonObject.has("state") ? jsonObject.getAsJsonObject("state") : null;

                if (data != null) {
                    System.out.println("Data:");
                    if (data.has("message")) {
                        System.out.println("  Message: " + data.get("message").getAsString());
                    }
                    if (data.has("object")) {
                        JsonArray objects = data.getAsJsonArray("object");
                        System.out.println("  Objects:");
                        for (JsonElement objElement : objects) {
                            JsonObject obj = objElement.getAsJsonObject();
                            String direction = obj.has("direction") ? obj.get("direction").getAsString() : "N/A";
                            String type = obj.has("type") ? obj.get("type").getAsString() : "N/A";
                            int distance = obj.has("distance") ? obj.get("distance").getAsInt() : -1;
                            System.out.println("    Direction: " + direction + ", Type: " + type + ", Distance: " + distance);
                        }
                    }
                    // Other data fields you might want to display
                    if (data.has("repair")) {
                        System.out.println("  Repair: " + data.get("repair").getAsString());
                    }
                    if (data.has("shields")) {
                        System.out.println("  Shields: " + data.get("shields").getAsInt());
                    }
                    if (data.has("reload")) {
                        System.out.println("  Reload: " + data.get("reload").getAsString());
                    }
                    if (data.has("visibility")) {
                        System.out.println("  Visibility: " + data.get("visibility").getAsString());
                    }
                    if (data.has("position")) {
                        System.out.println("  Position: " + data.get("position").getAsString());
                    }

                }

                if (state != null) {
                    System.out.println("State:");
                    if (state.has("position")) {
                        System.out.println("  Position: " + state.get("position").getAsString());
                    }
                    if (state.has("direction")) {
                        System.out.println("  Direction: " + state.get("direction").getAsString());
                    }
                    if (state.has("shields")) {
                        if (state.get("shields").getAsInt() != 0) {
                            System.out.println("  Shields: " + state.get("shields").getAsInt());
                        }
                    }
                    if (state.has("shots")) {
                        System.out.println("  Shots: " + state.get("shots").getAsInt());
                    }
                    if (state.has("status")) {
                        System.out.println("  Status: " + state.get("status").getAsString());
                    }
                }
            } else {
                System.out.println("Received a non-JSON object response: " + serverResponse);
            }
        } catch (JsonSyntaxException e) {
            System.out.println("Failed to parse JSON: " + e.getMessage());
        }
    }

}
