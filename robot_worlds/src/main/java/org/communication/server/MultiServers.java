package org.communication.server;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.List;
import java.util.Scanner;

import static org.communication.server.SimpleServer.*;


public class MultiServers extends DisplayHeaders {
    public static boolean flag;
    public static List<Socket> socketList = new ArrayList<>();


    public static void main(String[] args) throws ClassNotFoundException, IOException {
        Scanner sc = new Scanner(System.in);

        ServerSocket s = new ServerSocket( SimpleServer.PORT);

        displayHeader();
        displayMenu();
        System.out.println("Server running & waiting for client connections.");
        flag = false;

        // Thread for handling user input
        Thread userInputThread = new Thread(() -> {

            String userInput;

            while (true){
                userInput = sc.nextLine();
                if (userInput.equals("robots")){
                    if (robotObjects.isEmpty()){
                        System.out.println("Sorry there isn't any robots in the world at the moment!");
                    }else{
                        listRobots();
                        System.out.println();
                    }

                }else if (userInput.equals("dump")){
                    displayObstaclesAndRobots();
                }else if (userInput.equals("quit")) {
                    flag = true;

                    System.out.println("Game terminated!");
                    for (Socket eachSocket: socketList) {
                        try (PrintStream out = new PrintStream(eachSocket.getOutputStream())) {
                            out.println("quit");
                            out.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    for (Socket eachSocket : socketList){
                        try {
                            eachSocket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    sc.close();
                    System.exit(0); // Close the server socket when user inputs "quit"
                }
            }
        });
        userInputThread.start();

        while(true) {
            try {
                Socket socket = s.accept();
                socketList.add(socket);
                Runnable r = new SimpleServer(socket);
                if (!flag){
                    new Thread(r).start();
                }

            } catch(IOException ex) {
                ex.printStackTrace();
            }
        }
    }


}