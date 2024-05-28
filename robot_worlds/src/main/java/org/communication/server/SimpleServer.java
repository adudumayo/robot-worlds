package org.communication.server;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.*;
import org.communication.client.Request;


public class SimpleServer implements Runnable {

    public static final int PORT = 5000;
    private final BufferedReader in;
    private final PrintStream out;
    public static ArrayList<String> robotNames = new ArrayList<>(); // ArrayList to store robot names
    public static ArrayList<String> validCommands = new ArrayList<>(Arrays.asList("forward", "back", "look", "turn", "state", "fire")); //ArrayList to store robots valid commands
    public static ArrayList<String> turns = new ArrayList<>(Arrays.asList("left", "right")); //ArrayList to store robots valid commands
    public static ArrayList<Robot> robotObjects = new ArrayList<>();
    Gson gson = new Gson();
    Gson gsonPretty = new GsonBuilder().setPrettyPrinting().create();



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
                    if (request.getCommand().equals("launch")){
                        String robotName = request.getRobotName();
                        if (!robotNames.contains(robotName)) {
                            robot = new Robot(robotName);
                            request.setRobot(robotName);
                            robotNames.add(robotName);
                            int shield = Integer.parseInt(request.getArguments()[1]);
                            int shots = Integer.parseInt(request.getArguments()[2]);
                            System.out.println(request.getRobotName() + " just launched into the world");
                            out.println(sendResponsetoClient(robot, gsonPretty, shield, shots));
                            robotObjects.add(robot);

                        }else {
                            errorResponse(robot, gsonPretty, "ERROR", "Too many of you in this world");
                        }


                    }else if (validCommands.contains(request.getCommand()) && !request.getCommand().equals("look") && !request.getCommand().equals("state") && !request.getCommand().equals("fire")) {
                        try {
                            if (!turns.contains(request.getArguments()[0])) {
                                String newRobotCommand = request.getCommand() + " " + request.getArguments()[0];
                                Command command = Command.create(newRobotCommand);
                                robot.handleCommand(command);
                                String jsonToClient = successfulResponse(robot, gsonPretty, "OK", robot.getState().getShields(), robot.getState().getShots());
                                out.println(jsonToClient);

                            } else if (turns.contains(request.getArguments()[0])) {
                                String newRobotCommand = request.getArguments()[0];
                                Command command = Command.create(newRobotCommand);
                                robot.handleCommand(command);
                                String jsonToClient = successfulResponse(robot, gsonPretty, "OK", robot.getState().getShields(), robot.getState().getShots());
                                out.println(jsonToClient);

                            } else {
                                String errorResponse = errorResponse(robot, gsonPretty, "ERROR", "Could not parse arguments");
                                out.println(errorResponse);
                            }

                        } catch (IllegalArgumentException |NullPointerException e) {
                            String errorResponse = errorResponse(robot, gsonPretty, "ERROR", "Could not parse arguments");
                            out.println(errorResponse);

                        }

                    }else if (validCommands.contains(request.getCommand()) && request.getCommand().equals("fire") && request.getArguments()==null){
                        Command command = Command.create(request.getCommand());
                        robot.handleCommand(command);
                        String jsonToClient = sendFireResponseMiss(robot, gsonPretty, robot.getState().getShields(), robot.getState().getShots() );
                        out.println(jsonToClient);


                    }else if (request.getCommand().equals("look") && validCommands.contains("look")) {

                        String newRobotCommand = request.getCommand();
                        Command command = Command.create(newRobotCommand);
                        robot.handleCommand(command);

                        String jsonToClient = successfulLookResponse(robot, gsonPretty, "OK", robot.getState().getShields(), robot.getState().getShots());
                        out.println(jsonToClient);

                    }else if (request.getCommand().equals("state") && validCommands.contains("state")){
                        String jsonToClient = sendStateResponseToClient(robot, gsonPretty, robot.getState().getShields(), robot.getState().getShots());
                        out.println(jsonToClient);


                    }else {
                        String errorResponse = errorResponse(robot, gsonPretty, "ERROR", "Could not parse arguments");
                        out.println(errorResponse);
                    }

                }catch (JsonSyntaxException e){
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

    private String sendFireResponseMiss(Robot robot, Gson gsonPretty, int shield, int shots){
        Map<String, Object> data = new HashMap<>();
        Response response = new Response();
        State state = new State(shield, shots);
        if (shots == 0){
            data.put("message", "please reload bullets");
        }else{
            data.put("message", "Miss");
        }
        data.put("shots", state.getShots());
        response.setData(data);

        return gsonPretty.toJson(response);

    }

        private String sendStateResponseToClient(Robot robot, Gson gsonPretty, int shield, int shots){
            Response response = new Response();
            // Create and set the state object
            State state = new State(shield, shots);
            state.setPosition(robot.coordinatePosition());
            state.setDirection(robot.getCurrentDirection());
            response.setState(state);
            return gsonPretty.toJson(response);
        }

        private String sendResponsetoClient(Robot robot, Gson gsonPretty, int shield, int shots){
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
            data.put("visibility", "10");
            data.put("reload", "10");
            data.put("repair", "15");
            data.put("shields", robot.getState().getShields());
            response.setData(data);

            return gsonPretty.toJson(response);
        }

        private String errorResponse(Robot robot, Gson gsonPretty, String setResult, String message){
            Response response = new Response();
            response.setResult(setResult);
            Map<String, Object> data = new HashMap<>();
            data.put("message", message);
            response.setData(data);
            return gsonPretty.toJson(response);

        }
        private String successfulResponse(Robot robot, Gson gsonPretty, String setResult, int shield, int shots){
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

            return gsonPretty.toJson(response);

        }
        private String successfulLookResponse(Robot robot, Gson gsonPretty, String setResult, int shield, int shots) {

            // Create the response object
            Map<String, Object> data = new HashMap<>();
            ArrayList<ObstacleType> objects = new ArrayList<>();
            ArrayList<String> directions = new ArrayList<>(Arrays.asList("North", "East", "South", "West"));
            Response response = new Response();
            System.out.println(robot.obstacleSteps);


            for (int i = 0; i < robot.obstacleSteps.size(); i++) {
                int directionIndex = i % directions.size(); // Calculate the index of direction
                String dir = directions.get(directionIndex);


                if (robot.obstacleSteps.get(i) != 0) {
                    ObstacleType obj = new ObstacleType(dir, "obstacle", robot.obstacleSteps.get(i));
                    objects.add(obj);
                }
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
            return gsonPretty.toJson(response);
        }
    }



