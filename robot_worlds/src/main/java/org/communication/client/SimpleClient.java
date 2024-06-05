package org.communication.client;

import java.net.*;
import java.io.*;
import java.util.*;
import com.google.gson.*;
import org.communication.server.robotModels.*;

import static org.communication.server.DisplayHeaders.*;
import static org.communication.server.SimpleServer.validCommands;

public class SimpleClient {

    public static boolean keepRunning = true;
    public static int launchCount = 0;

    public static void main(String[] args) throws IOException {

        Scanner sc = new Scanner(System.in);
        Gson gson = new Gson();
        ArrayList<String> robotModels = new ArrayList<>(Arrays.asList("warpath","demolisher","reaper", "venom", "blaze"));

        try (
                Socket socket = new Socket("localhost", 5000);
                PrintStream out = new PrintStream(socket.getOutputStream());
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        ) {
            displayHeaderRobot();
            displayRobotCommands();
            displayRobotModels();

            Thread inputThread = new Thread(() -> {
                while (keepRunning) {
                    String userInput = sc.nextLine().toLowerCase();
                    String[] parts = userInput.split(" ");
                    if (userInput.startsWith("quit")) {
                        System.out.println("Exiting game...");
                        System.exit(0);
                    }
                    if (parts[0].equalsIgnoreCase("launch") && (robotModels.contains(parts[1])) && parts.length == 3 && launchCount == 0) {
                        launchCount += 1;
                        try {
                            Object robot = createRobotInstance(parts[1]);
                            if (robot != null) {
                                String shield = (String) robot.getClass().getMethod("getShield").invoke(robot);
                                String shots = (String) robot.getClass().getMethod("getShots").invoke(robot);
                                String[] stringArgs = {parts[1], shield, shots};
                                Request request = new Request(parts[2], parts[0], stringArgs);
                                out.println(gson.toJson(request));
                                out.flush();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else if(validCommands.contains(parts[0])){
                        if (parts.length > 1){
                            Request request = new Request();
                            request.setCommand(parts[0]);
                            request.setArguments(new String[]{parts[1]});
                            String jsonRequest = gson.toJson(request);
                            out.println(jsonRequest);
                            out.flush();

                        }else {
                            Request request = new Request();
                            request.setCommand(parts[0]);
                            String jsonRequest = gson.toJson(request);
                            out.println(jsonRequest);
                            out.flush();
                        }
                    }else{
                        System.out.println("Invalid Command. Try again or enter 'help'");
                        if (userInput.startsWith("help")) {
                            helpMenu();
                        }

                    }
                }
            });
            inputThread.start();
            String serverResponse;
            while ((serverResponse = in.readLine()) != null) {
                if (serverResponse.equals("quit") || serverResponse.equals("game over")){
                    System.out.println("Game Over, bye!");
                    keepRunning = false;
                    System.exit(0);
                    break;
                }
//                System.out.println(serverResponse);
                displayServerResponse(serverResponse);
                System.out.println();
            }

            inputThread.interrupt();

            } catch(IOException e){
                e.printStackTrace();
            }finally {
            sc.close();
        }

        }

    private static Object createRobotInstance(String model) throws Exception {
        switch (model.toLowerCase()) {
            case "warpath":
                return new Warpath();
            case "venom":
                return new Venom();
            case "reaper":
                return new Reaper();
            case "demolisher":
                return new Demolisher();
            case "blaze":
                return new Blaze();
            // Add other cases for different robot models if necessary
            default:
                System.out.println("Invalid robot model: " + model);
                return null;
        }
    }


}



