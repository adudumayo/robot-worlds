package org.communication.client;

import java.net.*;
import java.io.*;
import java.util.Scanner;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.communication.server.DisplayHeaders;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class SimpleClient extends DisplayHeaders {
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

            //Keep sending and receiving messages util the user decides to quit
            while(true){
                String userInput = sc.nextLine().toLowerCase();

                // check if user wants to quit
                if (userInput.equals("quit") || userInput.equals("off")) {
                    System.exit(0);
//
                }else if(userInput.equals("state")){
                    robotState();
                    continue;
                }
                else {
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

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}