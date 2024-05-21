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
//                    listRobots();
                    System.out.println();
                }else if (userInput.equals("dump")){
                    displayObstaclesAndRobots();
                }else if (userInput.equals("quit")) {
                    flag = true;
                    System.out.println(socketList.toString());
                    for (Socket eachSocket: socketList){
                        PrintStream out = null;

                        try {
                            out = new PrintStream(eachSocket.getOutputStream());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        out.println("quit");
                        out.flush();

                        try {
                            eachSocket.close();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
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
                Thread task = new Thread(r);
                if (!flag){
                    task.start();
                }


            } catch(IOException ex) {
                ex.printStackTrace();
            }
        }
    }


}