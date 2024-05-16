package org.communication.server;

import java.net.*;
import java.io.*;
import java.util.Scanner;

import static org.communication.server.SimpleServer.*;


public class MultiServers extends DisplayHeaders {

    public static void main(String[] args) throws ClassNotFoundException, IOException {
        Scanner sc = new Scanner(System.in);

        ServerSocket s = new ServerSocket( SimpleServer.PORT);

        displayHeader();
        displayMenu();
        System.out.println("Server running & waiting for client connections.");

        // Thread for handling user input
        Thread userInputThread = new Thread(() -> {

            String trialString;
            do {
                trialString = sc.nextLine();
                if (trialString.equals("robots")){
                    listRobots();
                }else if (trialString.equals("dump")){
                        displayObstaclesAndRobots();
                }
            } while (!trialString.equals("quit"));
            sc.close();
            System.exit(0); // Close the server socket when user inputs "quit"
        });
        userInputThread.start();

        while(true) {
            try {
                Socket socket = s.accept();
                Runnable r = new SimpleServer(socket);
                Thread task = new Thread(r);
                task.start();

            } catch(IOException ex) {
                ex.printStackTrace();
            }
        }
    }


}