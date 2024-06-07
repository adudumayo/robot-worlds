package org.communication.server;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.communication.server.SimpleServer.*;
import static org.communication.server.DisplayHeaders.*;

public class MultiServers{

    public static boolean flag;
    public static List<Socket> socketList = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);

        displayHeader();
        displayMenu();
        handleWorldConfiguration(sc);
        ServerSocket serverSocket = new ServerSocket(SimpleServer.PORT);
        displayWaitingForConnections();
        flag = false;
        startUserInputThread(sc);
        acceptClientConnections(serverSocket);
    }

    private static void handleWorldConfiguration(Scanner sc) {
        String userConfig = sc.nextLine();

        if (userConfig.equalsIgnoreCase("config")) {
            configMenu();
            System.out.println("Let's start with the top left coordinates: ");
            int[] topLeftCoordinates = getCoordinates(sc);
            Config.setTopLeftX_world(topLeftCoordinates[0]);
            Config.setTopLeftY_world(topLeftCoordinates[1]);

            System.out.println("Enter the bottom right coordinates: ");
            int[] bottomRightCoordinates = getCoordinates(sc);
            Config.setBottomRightX_world(bottomRightCoordinates[0]);
            Config.setBottomRightY_world(bottomRightCoordinates[1]);

            System.out.println("Enter visibility constraint: ");
            int newVisibility = Integer.parseInt(sc.nextLine());
            Config.setVisibility(newVisibility);


        }else if (userConfig.equalsIgnoreCase("q")){
            System.exit(0);
        }
    }
/*thread for server commands
 * robots - lists all the robots in the world.
 * dump - displays a representation of the world's state.
 * quit - terminates the server and all active connections.
 * view - displays all the available/acceptable commands.
 */
    private static void startUserInputThread(Scanner sc) {
        Thread userInputThread = new Thread(() -> {
            System.out.println("enter 'view' for available commands.");
            while (true) {
                String userInput = sc.nextLine();

                if (userInput.equals("robots")) {
                    if (robotObjects.isEmpty()) {
                        System.out.println("Sorry, there aren't any robots in the world at the moment!");
                    } else {
                        listRobots();
                        System.out.println();
                    }
                } else if (userInput.equals("dump")) {
                    displayObstaclesAndRobots();
                }else if (userInput.equalsIgnoreCase("view")){
                    viewMenu();

                } else if (userInput.equals("quit")) {
                    flag = true;
                    System.out.println("Game terminated!");
                    terminateConnections();
                    sc.close();
                    System.exit(0);
                } else {
                    System.out.println("Sorry I did not understand '" + userInput + ". enter 'view' for assistance.");
                }
            }
        });
        userInputThread.start();
    }

    private static void acceptClientConnections(ServerSocket serverSocket) {
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                socketList.add(socket);
                Runnable serverTask = new SimpleServer(socket);
                if (!flag) {
                    new Thread(serverTask).start();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private static void terminateConnections() {
        for (Socket eachSocket : socketList) {
            try (PrintStream out = new PrintStream(eachSocket.getOutputStream())) {
                out.println("quit");
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        for (Socket eachSocket : socketList) {
            try {
                eachSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static int[] getCoordinates(Scanner sc) {
        while (true) {
            try {
                String input = sc.nextLine();
                if (input.startsWith("(") && input.endsWith(")") && input.length() > 2) {
                    return parseCoordinates(input);
                } else {
                    System.out.println("Oops! I think you made a mistake. Let's try again!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Oops! I think you made a mistake. Let's try again!");
            }
        }
    }

    private static int[] parseCoordinates(String input) {
        String[] parts = input.strip().split(",");
        int x = Integer.parseInt(parts[0].replace("(", "").trim());
        int y = Integer.parseInt(parts[1].replace(")", "").trim());
        return new int[]{x, y};
    }
}
