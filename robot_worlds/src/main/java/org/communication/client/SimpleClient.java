package org.communication.client;

import java.net.*;
import java.io.*;
import java.util.*;
import com.google.gson.*;
import org.communication.server.robotModels.*;
import static org.communication.server.Config.*;
import static org.communication.server.DisplayHeaders.*;
import static org.communication.server.SimpleServer.validCommands;

public class SimpleClient {

    public static boolean keepRunning = true;
    public static boolean launchCount = true;
    public static boolean reloading = false;
    public static boolean repairing = false;
    public static void main(String[] args) throws IOException {

        Scanner sc = new Scanner(System.in);
        Gson gson = new Gson();
        ArrayList<String> robotModels = new ArrayList<>(Arrays.asList("warpath", "demolisher", "reaper", "venom", "blaze"));

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
                    try {
                        if (!repairing) {
                            if (!reloading) {
                                if (parts[0].equalsIgnoreCase("launch") && (robotModels.contains(parts[1])) && parts.length == 3) {
                                    if (launchCount) {
                                        launchCount = false;
                                        Object robot = createRobotInstance(parts[1]);
                                        if (robot != null) {
                                            String shield = (String) robot.getClass().getMethod("getShield").invoke(robot);
                                            String shots = (String) robot.getClass().getMethod("getShots").invoke(robot);
                                            String[] stringArgs = {parts[1], shield, shots};
                                            Request request = new Request(parts[2], parts[0], stringArgs);
                                            out.println(gson.toJson(request));
                                            out.flush();
                                        }
                                    } else {
                                        System.out.println("You have already launched!");
                                    }
                                } else if (validCommands.contains(parts[0])) {
                                    if (parts.length > 1) {
                                        Request request = new Request();
                                        request.setCommand(parts[0]);
                                        request.setArguments(new String[]{parts[1]});
                                        String jsonRequest = gson.toJson(request);
                                        out.println(jsonRequest);
                                        out.flush();
                                    } else if (parts[0].equalsIgnoreCase("reload")) {
                                        Request request = new Request();
                                        request.setCommand(parts[0]);
                                        String jsonRequest = gson.toJson(request);
                                        out.println(jsonRequest);
                                        out.flush();

                                    } else if (parts[0].equalsIgnoreCase("repair")) {
                                        Request request = new Request();
                                        request.setCommand(parts[0]);
                                        String jsonRequest = gson.toJson(request);
                                        out.println(jsonRequest);
                                        out.flush();

                                    } else {
                                        Request request = new Request();
                                        request.setCommand(parts[0]);
                                        String jsonRequest = gson.toJson(request);
                                        out.println(jsonRequest);
                                        out.flush();
                                    }
                                } else {
                                    System.out.println("Invalid Command. Try again or enter 'help'");
                                    if (userInput.startsWith("help")) {
                                        helpMenu();
                                    }
                                }
                            } else {
                                System.out.println("Busy Reloading...");
                            }
                        } else{
                        System.out.println("Busy Repairing...");
                        }

                    } catch (Exception e) {
                        System.out.println("Invalid input: " + e.getMessage());
                    }
                }
            });
            inputThread.start();
            String serverResponse;
            while ((serverResponse = in.readLine()) != null) {
                if (serverResponse.equals("quit") || serverResponse.equals("game over")) {
                    System.out.println("Game Over, bye!");
                    keepRunning = false;
                    System.exit(0);
                    break;
                }
                if (serverResponse.contains("Too many of you in this world")) {
                    launchCount = true; // Reset the launchCount flag to allow launching a new robot

                }
                if (serverResponse.contains("Reloading Complete!")){
                    reloading = true; // Set reloading flag to true
                try {
                    Thread.sleep(reloadTime); // Wait for 5 seconds
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                reloading = false; // Set reloading flag to false after delay
                }
                if (serverResponse.contains("Repairing Complete!")){
                    repairing = true; // Set repairing flag to true
                    try {
                        Thread.sleep(repairTime); // Wait for 5 seconds
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    repairing = false; // Set repairing flag to false after delay
                }
                displayServerResponse(serverResponse);
                System.out.println();
            }

            inputThread.interrupt();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
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