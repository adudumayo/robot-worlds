package org.communication.client;

import java.net.*;
import java.io.*;
import java.util.*;
import com.google.gson.*;
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
                Socket socket = new Socket("localhost", 5001);
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
                    if (parts[0].equalsIgnoreCase("launch") && (robotModels.contains(parts[1])) && parts.length == 3 && launchCount ==0) {
                        launchCount += 1;
                        System.out.println("Enter the Max Shield:");
                        String maxShield = sc.nextLine();
                        if (maxShield.startsWith("quit")) {
                            System.out.println("Exiting game...");
                            System.exit(0);
                        }
                        System.out.println("Enter the Max Shots:");
                        String maxShots = sc.nextLine();
                        if (maxShots.startsWith("quit")) {
                            System.out.println("Exiting game...");
                            System.exit(0);
                        }
                        String[] stringArgs = {parts[1], maxShield, maxShots};
                        Request request = new Request(parts[2], parts[0], stringArgs);
                        out.println(gson.toJson(request));
                        out.flush();

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
                        System.out.println("Invalid Command, try again!");

                    }
                }
            });
            inputThread.start();
            String serverResponse;
            while ((serverResponse = in.readLine()) != null) {
                if (serverResponse.equals("quit")){
                    System.out.println("You're out, bye!");
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


}



