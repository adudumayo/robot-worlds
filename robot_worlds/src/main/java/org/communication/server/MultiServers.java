package org.communication.server;

import java.net.*;
import java.io.*;
import java.util.Scanner;
import static org.communication.server.SimpleServer.listRobots;
import static org.communication.server.SimpleServer.worldState;


public class MultiServers {

    public static void main(String[] args) throws ClassNotFoundException, IOException {
        Scanner sc = new Scanner(System.in);

        ServerSocket s = new ServerSocket( SimpleServer.PORT);
        displayHeader();
        displayMenu();
        System.out.println("Server running & waiting for client connections.");

        World world = World.getInstance();

        // Thread for handling user input
        Thread userInputThread = new Thread(() -> {
            String trialString;
            do {
                trialString = sc.nextLine();
                if (trialString.equals("robots")){
                    listRobots();
                }else if (trialString.equals("dump")){
                    worldState();
                }
            } while (!trialString.equals("quit"));
            sc.close();
            System.exit(0); // Close the server socket when user inputs "quit"
        });
        userInputThread.start();

        while(true) {
            try {
                Socket socket = s.accept();
                System.out.println("Connection: " + socket);

                Runnable r = new SimpleServer(socket);
                Thread task = new Thread(r);
                task.start();

            } catch(IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void displayHeader(){
        System.out.println("\n**********************************************");
        System.out.println("       ### Welcome to Robot World ###");
        System.out.println("**********************************************");
    }

    public static void displayMenu(){
        System.out.println("\nWorld Commands");
        System.out.println("'quit' - Disconnects all robots and ends the world ");
        System.out.println("'robots' - Lists all robots including the robot's name and state");
        System.out.println("'dump' - Displays a representation of the worlds state\n");
    }
}