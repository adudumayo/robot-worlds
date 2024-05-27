package org.communication.server;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.List;

import static org.communication.server.SimpleServer.*;


public class MultiServers extends DisplayHeaders {
    public static boolean flag;
    public static List<Socket> socketList = new ArrayList<>();
    public static int topLeftX_world;
    public static int topLeftY_world;
    public static int bottomRightX_world;
    public static int bottomRightY_world;

    public static void main(String[] args) throws ClassNotFoundException, IOException {
        Scanner sc = new Scanner(System.in);
        displayHeader();
        displayMenu();
        System.out.println("\nLets start by configuring the world size.");
        System.out.println("Enter the top left and bottom right coordinates to create the world \ne.g (-200,200)");
        System.out.println("Enter the top left Coordinates: ");

        while(true){
            String configInput = sc.nextLine();
            if (configInput.startsWith("(") && configInput.endsWith(")") & configInput.length()>2) {
                ArrayList<Integer> xyValuesTopLeft = validateInput(configInput);
                topLeftX_world = xyValuesTopLeft.get(0);
                topLeftY_world = xyValuesTopLeft.get(1);
                break;
            }else {
                System.out.println("Oops!, I think you made a mistake lets try again!");
            }
        }
        System.out.println("Enter the bottom right Coordinates: ");
        while(true){
            String configInput = sc.nextLine();
            if (configInput.startsWith("(") && configInput.endsWith(")") & configInput.length()>2) {
                ArrayList<Integer> xyValuesTopLeft = validateInput(configInput);
                bottomRightX_world = xyValuesTopLeft.get(0);
                bottomRightY_world = xyValuesTopLeft.get(1);
                break;
            }else {
                System.out.println("Oops!, I think you made a mistake lets try again!");
            }
        }


        ServerSocket s = new ServerSocket( SimpleServer.PORT);

        System.out.println("Server running & waiting for client connections.");

        flag = false;

        // Thread for handling user input
        Thread userInputThread = new Thread(() -> {
            String userInput;
            while (true) {
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


    public static ArrayList<Integer> validateInput (String configInput){
            ArrayList<Integer> coordinateArr = new ArrayList<>();
            String[] parts = configInput.split(",");
            int x_value = Integer.parseInt(parts[0].replace("(", ""));
            int y_value = Integer.parseInt(parts[1].replace(")", ""));
            coordinateArr.add(x_value);
            coordinateArr.add(y_value);

        return coordinateArr;
    }

}