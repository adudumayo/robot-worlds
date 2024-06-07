package org.communication.server;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.*;
import org.communication.client.Request;
import org.communication.server.robotModels.*;

import static org.communication.server.Fire.damagedRobot;

public class SimpleServer implements Runnable {

    public static final int PORT = 5000;
    private final BufferedReader in;
    private final PrintStream out;
    public static ArrayList<String> robotNames = new ArrayList<>(); // ArrayList to store robot names
    public static ArrayList<String> validCommands = new ArrayList<>(Arrays.asList("forward", "back", "look", "turn", "state", "fire", "orientation", "reload", "repair")); //ArrayList to store robots valid commands
    public static ArrayList<String> turns = new ArrayList<>(Arrays.asList("left", "right")); //ArrayList to store robots valid commands
    public static ArrayList<Robot> robotObjects = new ArrayList<>();
    Gson gson = new Gson();



    public SimpleServer(Socket socket) throws IOException {
        out = new PrintStream(socket.getOutputStream());
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public void run() {
        Robot robot = null;
        String messageFromClient;

        try{
            while ((messageFromClient = in.readLine()) != null) {
//                System.out.println(messageFromClient);

                try{
                    Request request = gson.fromJson(messageFromClient, Request.class);
                   
                    if (request.getCommand().equals("launch")) {
                        String robotName = request.getRobotName();
                        String robotType = request.getArguments()[0];
                        if (!robotNames.contains(robotName)) {
                            robot = new Robot(robotName,robotType);
                            robot.setName(robotName);
                            request.setRobot(robotName);
                            robotNames.add(robotName);
                            int shield = Integer.parseInt(request.getArguments()[1]);
                            int shots = Integer.parseInt(request.getArguments()[2]);
                            System.out.println(request.getRobotName() + " just launched into the world");
                            out.println(sendResponsetoClient(robot, gson, shield, shots));
                            robotObjects.add(robot);

                        } 

                        else {
                            String jsonToClient = errorResponse(robot, gson, "ERROR", "Too many of you in this world");
                            out.println(jsonToClient);
                        }

                    }else if (robot != null && robot.getState().getShields() <= 0) {
                        out.println("game over");


                    }else if (validCommands.contains(request.getCommand()) && !request.getCommand().equals("look") && !request.getCommand().equals("state") && !request.getCommand().equals("fire") && !request.getCommand().equals("orientation") && !request.getCommand().equals("reload") && !request.getCommand().equals("repair")) {

                        try {
                            if (!turns.contains(request.getArguments()[0])) {
                                String newRobotCommand = request.getCommand() + " " + request.getArguments()[0];
                                Command command = Command.create(newRobotCommand);
                                robot.handleCommand(command);
                                String jsonToClient = successfulResponse(robot, gson, "OK", robot.getState().getShields(), robot.getState().getShots());
                                out.println(jsonToClient);

                            } else if (turns.contains(request.getArguments()[0])) {
                                String newRobotCommand = request.getArguments()[0];
                                Command command = Command.create(newRobotCommand);
                                robot.handleCommand(command);
                                String jsonToClient = successfulResponse(robot, gson, "OK", robot.getState().getShields(), robot.getState().getShots());
                                out.println(jsonToClient);

                            } else {
                                String errorResponse = errorResponse(robot, gson, "ERROR", "Could not parse arguments");
                                out.println(errorResponse);
                            }

                        } catch (IllegalArgumentException | NullPointerException e) {
                            String errorResponse = errorResponse(robot, gson, "ERROR", "Could not parse arguments. Try launching first.");
                            out.println(errorResponse);

                        }

                    }else if (validCommands.contains(request.getCommand()) && request.getCommand().equals("fire") && request.getArguments()==null){
                        Command command = Command.create(request.getCommand());
                        robot.handleCommand(command);

                        if (damagedRobot != null) {
                            // Robot hit something, send hit response
                            String jsonToClient = sendFireResponseHit(damagedRobot, robot, gson, robot.getState().getShields(), robot.getState().getShots());
                            out.println(jsonToClient);
                        } else {
                            // Robot missed, send miss response
                            String jsonToClient = sendFireResponseMiss(gson, robot.getState().getShots());
                            out.println(jsonToClient);
                        }


                     }
                    // else if (request.getCommand().eq)
                    else if (request.getCommand().equals("look") && validCommands.contains("look")) {
                        String newRobotCommand = request.getCommand();
                        Command command = Command.create(newRobotCommand);
                        robot.handleCommand(command);
                        String jsonToClient = successfulLookResponse(robot, gson, "OK", robot.getState().getShields(), robot.getState().getShots());
                        out.println(jsonToClient);

                    }else if (request.getCommand().equals("state") && validCommands.contains("state")) {
                        assert robot != null;
                        String jsonToClient = sendStateResponseToClient(robot, gson, robot.getState().getShields(), robot.getState().getShots());
                        out.println(jsonToClient);

                    }else if (request.getCommand().equals("orientation") && validCommands.contains("orientation")) {
                        assert robot != null;
                        String jasonToClient = sendOrientationResponseToClient(robot, gson);
                        out.println(jasonToClient);

                    }else if (request.getCommand().equals("reload") && validCommands.contains("reload")){
                        assert robot != null;
                        String jasonToClient = reloadResponse(robot, gson);
                        out.println(jasonToClient);

                    }else if (request.getCommand().equals("repair") && validCommands.contains("repair")){
                        assert robot != null;
                        String jasonToClient = repairResponse(robot, gson);
                        out.println(jasonToClient);


                    }
                    // else if (request.getCommand().equals("fire") && robotObjects.isEmpty()){
                    //     String errorResponse = errorResponse(robot, gson, "ERROR", "No robots in the world");
                    //     out.println(errorResponse);
                    // }
                    else {
                        String errorResponse = errorResponse(robot, gson, "ERROR", "Could not parse arguments");
                        out.println(errorResponse);
                    }

                }
                catch (IllegalArgumentException | NullPointerException e) {
                    String errorResponse = errorResponse(robot, gson, "ERROR", "Could not parse arguments. Try launching first.");
                    out.println(errorResponse);

                }
                catch (JsonSyntaxException e){
                    System.out.println("invalid json received!");
                }
            }
            
            }catch (IOException e){
                if (e instanceof SocketException){
                    System.exit(0);
                }else {
                    e.printStackTrace();
                }
            }

        }


    private String sendFireResponseMiss( Gson gson, int shots){
        Map<String, Object> data = new HashMap<>();
        Response response = new Response();
        State state = new State(shots);
        if (shots == 0){
            data.put("message", "please reload bullets");
        }else{
            data.put("message", "Miss");
        }
        data.put("shots", state.getShots());
        response.setState(state);
        response.setData(data);

        return gson.toJson(response);

    }

    private String sendFireResponseHit(Robot hitRobot, Robot robot, Gson gson, int shields, int shots) {
        Map<String, Object> data = new HashMap<>();
        Response response = new Response();

        response.setResult("OK");
        data.put("message", "Hit");
        data.put("distance", robot.getDistance());
        data.put("robot", hitRobot.getName());
        Map<String, Object> hitRobotState = new HashMap<>();
        hitRobotState.put("position", hitRobot.coordinatePosition());
        hitRobotState.put("direction", hitRobot.getCurrentDirection());
        if (hitRobot.getState().getShields() == 0){
            hitRobot.getState().setStatus("DEAD");
        }
        hitRobotState.put("shields", hitRobot.getState().getShields());
        hitRobotState.put("shots", hitRobot.getState().getShots());
        hitRobotState.put("status", hitRobot.getState().getStatus());
        data.put("state", hitRobotState);

        response.setData(data);
        State state = new State(shields, shots);
        state.setPosition(robot.coordinatePosition());
        state.setDirection(robot.getCurrentDirection());
        state.setStatus("NORMAL");
        response.setState(state);
        return gson.toJson(response);
    }

    private String sendStateResponseToClient(Robot robot, Gson gson, int shield, int shots){
        Response response = new Response();
        // Create and set the state object
        State state = new State(shield, shots);
        state.setPosition(robot.coordinatePosition());
        state.setDirection(robot.getCurrentDirection());
        response.setState(state);
        return gson.toJson(response);
        }

    private String sendOrientationResponseToClient(Robot robot, Gson gson){
        Response response = new Response();
        Map<String, Object> data = new HashMap<>();
        data.put("direction", robot.getCurrentDirection() );
        response.setData(data);
        // Create and set the state object
        return gson.toJson(response);
    }

    private String sendResponsetoClient(Robot robot, Gson gson, int shield, int shots){
        Response response = new Response();
        // Create and set the state object
        State state = new State(shield,shots);
        state.setPosition(robot.coordinatePosition());
        state.setDirection(robot.getCurrentDirection());
        robot.setState(state);
        state.setStatus("NORMAL");
        response.setState(state);

        response.setResult("OK");
        // create and set the data map
        Map<String, Object> data = new HashMap<>();
        data.put("position", robot.coordinatePosition());
        data.put("visibility", Config.getVisibility());
        data.put("reload", "5 seconds");
        data.put("repair", "5 seconds");
        data.put("shields", robot.getState().getShields());
        response.setData(data);
        return gson.toJson(response);
    }

    private String errorResponse(Robot robot, Gson gson, String setResult, String message){
        Response response = new Response();
        response.setResult(setResult);
        Map<String, Object> data = new HashMap<>();
        data.put("message", message);
        response.setData(data);
        return gson.toJson(response);

    }
    private String successfulResponse(Robot robot, Gson gson, String setResult, int shield, int shots){
        Response response = new Response();
        response.setResult(setResult);
        Map<String, Object> data = new HashMap<>();
        data.put("message", robot.getStatus());
        response.setData(data);

        //Create and set the state object
        State state = new State(shield, shots);
        state.setPosition(robot.coordinatePosition());
        state.setDirection(robot.getCurrentDirection());
        state.setStatus("NORMAL");
        response.setState(state);

        return gson.toJson(response);

    }
    private String successfulLookResponse(Robot robot, Gson gson, String setResult, int shield, int shots) {
        // Create the response object
        Map<String, Object> data = new HashMap<>();
        ArrayList<ObstacleType> objects = new ArrayList<>();
        Response response = new Response();

        // Add North obstacles
        if (robot.obstacleSteps.containsKey("North_obstacle") && robot.obstacleSteps.get("North_obstacle") != 0) {
            objects.add(new ObstacleType("North", "obstacle", robot.obstacleSteps.get("North_obstacle")));
        }
        if (robot.obstacleSteps.containsKey("North_robot") && robot.obstacleSteps.get("North_robot") != 0) {
            objects.add(new ObstacleType("North", "robot", robot.obstacleSteps.get("North_robot")));
        }

        // Add East obstacles
        if (robot.obstacleSteps.containsKey("East_obstacle") && robot.obstacleSteps.get("East_obstacle") != 0) {
            objects.add(new ObstacleType("East", "obstacle", robot.obstacleSteps.get("East_obstacle")));
        }
        if (robot.obstacleSteps.containsKey("East_robot") && robot.obstacleSteps.get("East_robot") != 0) {
            objects.add(new ObstacleType("East", "robot", robot.obstacleSteps.get("East_robot")));
        }

        // Add South obstacles
        if (robot.obstacleSteps.containsKey("South_obstacle") && robot.obstacleSteps.get("South_obstacle") != 0) {
            objects.add(new ObstacleType("South", "obstacle", robot.obstacleSteps.get("South_obstacle")));
        }
        if (robot.obstacleSteps.containsKey("South_robot") && robot.obstacleSteps.get("South_robot") != 0) {
            objects.add(new ObstacleType("South", "robot", robot.obstacleSteps.get("South_robot")));
        }

        // Add West obstacles
        if (robot.obstacleSteps.containsKey("West_obstacle") && robot.obstacleSteps.get("West_obstacle") != 0) {
            objects.add(new ObstacleType("West", "obstacle", robot.obstacleSteps.get("West_obstacle")));
        }
        if (robot.obstacleSteps.containsKey("West_robot") && robot.obstacleSteps.get("West_robot") != 0) {
            objects.add(new ObstacleType("West", "robot", robot.obstacleSteps.get("West_robot")));
        }

        response.setResult(setResult);

        // Create the data map and populate it
        data.put("message", robot.getStatus());
        data.put("object", objects);
        response.setData(data);

        // Create and set the state object
        State state = new State(shield, shots);
        state.setPosition(robot.coordinatePosition());
        state.setDirection(robot.getCurrentDirection());
        state.setStatus("NORMAL");
        response.setState(state);

        // Convert response to JSON
        return gson.toJson(response);
    }


    private String reloadResponse(Robot robot, Gson gson) {
        if (robot.getRobotType().equalsIgnoreCase("venom")){
            Venom venom = new Venom();
            int reloadShots = Integer.parseInt(venom.getShots());
            robot.getState().setShots(reloadShots);

        }else if (robot.getRobotType().equalsIgnoreCase("blaze")){
            Blaze blaze = new Blaze();
            int reloadShots = Integer.parseInt(blaze.getShots());
            robot.getState().setShots(reloadShots);

        }else if (robot.getRobotType().equalsIgnoreCase("demolisher")) {
            Demolisher demolisher = new Demolisher();
            int reloadShots = Integer.parseInt(demolisher.getShots());
            robot.getState().setShots(reloadShots);

        }else if (robot.getRobotType().equalsIgnoreCase("reaper")) {
            Reaper reaper = new Reaper();
            int reloadShots = Integer.parseInt(reaper.getShots());
            robot.getState().setShots(reloadShots);

        }else if (robot.getRobotType().equalsIgnoreCase("warpath")) {
            Warpath warpath = new Warpath();
            int reloadShots = Integer.parseInt(warpath.getShots());
            robot.getState().setShots(reloadShots);
        }

        Response response = new Response();
        response.setResult("Reloaded");
        Map<String, Object> data = new HashMap<>();
        data.put("message", "Reloading Complete!");
        State state = new State(robot.getState().getShields(), robot.getState().getShots());
        state.setPosition(robot.coordinatePosition());
        state.setDirection(robot.getCurrentDirection());
        response.setState(state);
        response.setData(data);
        return gson.toJson(response);
    }

    private String repairResponse(Robot robot, Gson gson){
        if (robot.getRobotType().equalsIgnoreCase("venom")){
            Venom venom = new Venom();
            int repairShield = Integer.parseInt(venom.getShield());
            robot.getState().setShields(repairShield);
        }else if (robot.getRobotType().equalsIgnoreCase("blaze")){
            Blaze blaze = new Blaze();
            int repairShield = Integer.parseInt(blaze.getShield());
            robot.getState().setShields(repairShield);
        }else if (robot.getRobotType().equalsIgnoreCase("demolisher")) {
            Demolisher demolisher = new Demolisher();
            int repairShield = Integer.parseInt(demolisher.getShield());
            robot.getState().setShields(repairShield);
        }else if (robot.getRobotType().equalsIgnoreCase("reaper")) {
            Reaper reaper = new Reaper();
            int repairShield = Integer.parseInt(reaper.getShield());
            robot.getState().setShields(repairShield);
        }else if (robot.getRobotType().equalsIgnoreCase("warpath")) {
            Warpath warpath = new Warpath();
            int repairShield = Integer.parseInt(warpath.getShield());
            robot.getState().setShields(repairShield);
        }

        Response response = new Response();
        response.setResult("Repair");
        Map<String, Object> data = new HashMap<>();
        data.put("message", "Repairing Complete!");
        State state = new State(robot.getState().getShields(), robot.getState().getShots());
        state.setPosition(robot.coordinatePosition());
        state.setDirection(robot.getCurrentDirection());
        response.setState(state);
        response.setData(data);
        return gson.toJson(response);


    }

}



