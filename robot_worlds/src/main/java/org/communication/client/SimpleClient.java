package org.communication.client;

import java.net.*;
import java.io.*;
import java.util.Scanner;

public class SimpleClient {
    public static void main(String args[]) {


        Scanner sc = new Scanner(System.in);
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
                }
                else {
                    out.println(userInput);
                    out.flush();
                }

//              read and display response from server
                String serverResponse = in.readLine();
                if (serverResponse.equals("true")) {
                    robotIsLaunched = true;
                    continue;
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