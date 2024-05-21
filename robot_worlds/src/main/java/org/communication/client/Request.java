package org.communication.client;

import com.google.gson.Gson;
import org.communication.server.Robot;

import java.util.ArrayList;

public class Request {

    private String robotName;
    private String command;
    private String[] arguments;
    private Robot robot;

    public Request(){
    }

    public Request(String robotName, String command){
        this.robotName = robotName;
        this.command = command;
    }
    public Request(String robotName, String command, String[] arguments){
        this.robotName = robotName;
        this.command = command;
        this.arguments = arguments;
    }



    // Getter and setter methods

    public String getRobotName() {
        return robotName;
    }

    public void setRobot(String robot) {
        this.robotName = robot;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String[] getArguments() {
        return arguments;
    }

    public void setArguments(String[] arguments) {
        this.arguments = arguments;
    }

    public String launchRobot(String robotName, String command){

        Gson gson = new Gson();
        this.setRobot(robotName);
        this.setCommand(command);
        return gson.toJson(this);
    }
    public String commandRobot(String robotName, String command, String[] arguments){

        Gson gson = new Gson();
        this.setRobot(robotName);
        this.setCommand(command);
        this.setArguments(arguments);
        return gson.toJson(this);
    }


    @Override
    public String toString() {
        return "RequestMessage{" +
                "robot='" + robot + '\'' +
                ", command='" + command + '\'' +
                ", arguments=" + arguments +
                '}';
    }
}
