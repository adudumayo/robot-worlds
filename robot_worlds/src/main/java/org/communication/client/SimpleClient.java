package org.communication.client;

import java.net.*;
import java.io.*;
import java.util.Scanner;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class SimpleClient {
    public static void main(String[] args) {


        Scanner sc = new Scanner(System.in);
        Gson gson = new Gson();
        try (
                Socket socket = new Socket("localhost", 5000);
                PrintStream out = new PrintStream(socket.getOutputStream());
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        ) {
            displayHeaderRobot();
            displayRobotCommands();

            boolean robotIsLaunched = false;

            //Keep sending and receiving messages util the user decides to quit
            while(true){
                String userInput = sc.nextLine().toLowerCase();

                if (userInput.equals("quit") || userInput.equals("off")) {
                    System.exit(0);
                }else if(userInput.equals("state") && robotIsLaunched){
                    robotState();
                    continue;
                } else {
                    out.println(userInput);
                    out.flush();
                }
                // Read and display response from server
                String serverResponse = in.readLine();

                // If the response is a JSON array, deserialize and print it
                if(userInput.equalsIgnoreCase("look")) {
                    List<String> lookResults = gson.fromJson(serverResponse, new TypeToken<List<String>>(){}.getType());
                    for (String result : lookResults) {
                        System.out.println(result);
                    }
                } else{
                        System.out.println(serverResponse);
                    }

//              read and display response from server
                String serverResponse = in.readLine();

                String[] serverResponseArray = serverResponse.split(" ");
                String robotStatus = serverResponseArray[serverResponseArray.length - 1]; // the last word on the [] name> Status

                if (robotStatus.equalsIgnoreCase("Ready")) {
                    robotIsLaunched = true;
                }

                System.out.println(serverResponse);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void displayRobotCommands(){
        System.out.println("\nRobot Commands");
        System.out.println("'launch' - launch a new robot into the world");
        System.out.println("'look' - Allows your robot to look around");
        System.out.println("'state' - View the current state of your robot\n");
        System.out.println("launch a robot to start the game: ");;
    }

    public static void displayHeaderRobot(){
        System.out.println("You have successfully connected to the game server!!!");
        System.out.println("\n**********************************************");
        System.out.println("       ### Welcome to Robot World ###");
        System.out.println("**********************************************");
    }

    public static void robotState(){
        System.out.println("State           : <READY FOR BATTLE>");
        System.out.println("Robot type      : <BATTLE BOT>");
        System.out.println("Shield strength : <MAXIMUM>");
        System.out.println("Shots           : <MAXIMUM>\n");
    }
}