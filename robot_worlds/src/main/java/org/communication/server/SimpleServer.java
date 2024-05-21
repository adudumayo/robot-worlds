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

    public static final int PORT = 8081;
    private final BufferedReader in;
    private final PrintStream out;
    public static ArrayList<String> robotNames = new ArrayList<>(); // ArrayList to store robot names
    public static ArrayList<String> validCommands = new ArrayList<>(Arrays.asList("forward", "back", "look", "turn")); //ArrayList to store robots valid commands
    public static ArrayList<String> turns = new ArrayList<>(Arrays.asList("left", "right")); //ArrayList to store robots valid commands
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
                System.out.println(messageFromClient);

                try{
                    Request request = gson.fromJson(messageFromClient, Request.class);
                    if (request.getCommand().equals("launch")){
                        String robotName = request.getRobotName();
                        if (!robotNames.contains(robotName)){
                            robot = new Robot(robotName);
                            request.setRobot(robotName);
                            robotNames.add(robotName);
                            State state = new State();
                            int numShield = Integer.parseInt(request.getArguments()[1]);
                            int numShots = Integer.parseInt(request.getArguments()[2]);
                            state.setShields(numShield);
                            state.setShots(numShots);
                            robot.setState(state);

                            System.out.println(request.getRobotName() + " just launched into the world");
                            out.println(sendResponsetoClient(robot, gsonPretty));
                        }else {
                            errorResponse(robot, gsonPretty, "ERROR", "Too many of you in this world");
                        }

                    }else if (validCommands.contains(request.getCommand())){
                        try{

                            if (!turns.contains(request.getArguments()[0])){
                                String newRobotCommand = request.getCommand() + " " + request.getArguments()[0];
                                Command command = Command.create(newRobotCommand);
                                robot.handleCommand(command);
                                String jsonToClient = successfulResponse(robot, gsonPretty, "OK");
                                out.println(jsonToClient);

                            }else if (turns.contains(request.getArguments()[0])) {
                                String newRobotCommand = request.getArguments()[0];
                                Command command = Command.create(newRobotCommand);
                                robot.handleCommand(command);
                                String jsonToClient = successfulResponse(robot, gsonPretty, "OK");
                                out.println(jsonToClient);
                            }else{
                                String errorResponse = errorResponse(robot, gsonPretty, "ERROR", "Could not parse arguments");
                                out.println(errorResponse);
                            }

                        }catch (IllegalArgumentException e){
                            String errorResponse = errorResponse(robot, gsonPretty, "ERROR", "Could not parse arguments");
                            out.println(errorResponse);

                        }


                    }else {
                        String errorResponse = errorResponse(robot, gsonPretty, "ERROR", "Could not parse arguments");
                        out.println(errorResponse);
                    }


                }catch (JsonSyntaxException e){
                    System.out.println("invalid json received!");
                }

            }

            }catch (IOException e){
                e.printStackTrace();
            }

        }


        private String sendResponsetoClient(Robot robot, Gson gsonPretty){
            Response response = new Response();
            response.setResult("OK");
            // create and set the data map
            Map<String, Object> data = new HashMap<>();
            data.put("position", robot.coordinatePosition());
            data.put("visibility", "10");
            data.put("reload", "10");
            data.put("repair", "15");
            data.put("shields", robot.getState().getShields());
            response.setData(data);

            // Create and set the state object
            State state = new State();
//            robot.setState(state);
            state.setPosition(robot.coordinatePosition());
            state.setDirection(robot.getCurrentDirection());
            state.setShields(5);
            state.setShots(3);

            state.setStatus("NORMAL");
            response.setState(state);

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
        private String successfulResponse(Robot robot, Gson gsonPretty, String setResult){
            Response response = new Response();
            response.setResult(setResult);
            Map<String, Object> data = new HashMap<>();
            data.put("message", robot.getStatus());
            response.setData(data);

            // Create and set the state object
            State state = new State();
            state.setPosition(robot.coordinatePosition());
            state.setDirection(robot.getCurrentDirection());
            state.setShields(3);
            state.setShots(5);
            state.setStatus("NORMAL");
            response.setState(state);

            return gsonPretty.toJson(response);

        }




    }



