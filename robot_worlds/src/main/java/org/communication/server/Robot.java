package org.communication.server;

import java.util.ArrayList;

public class Robot {
    private final Position TOP_LEFT = new Position(-200,200);
    private final Position BOTTOM_RIGHT = new Position(200,-200);
    public static final Position CENTRE = new Position(0,0);

    private Position position;
    private Direction currentDirection;
    private String status;
    private String name;
    public boolean positionCheck;
    public boolean pathCheck;
    public ArrayList<String> obstaclesNorth = new ArrayList<>();
    public ArrayList<String> obstaclesEast = new ArrayList<>();
    public ArrayList<String> obstaclesSouth = new ArrayList<>();
    public ArrayList<String> obstaclesWest = new ArrayList<>();

    public Robot(String name) {
        this.name = name;
        this.status = "Ready";
        this.position = CENTRE;
        this.currentDirection = Direction.NORTH;
    }

    public boolean handleCommand(Command command) {
        return command.execute(this);
    }

    public void updateDirection(boolean directionHeading) {
        switch (currentDirection) {
            case NORTH, UP:
                currentDirection = directionHeading ? Direction.RIGHT : Direction.LEFT;
                break;
            case RIGHT:
                currentDirection = directionHeading ? Direction.DOWN : Direction.UP;
                break;
            case DOWN:
                currentDirection = directionHeading ? Direction.LEFT : Direction.RIGHT;
                break;
            case LEFT:
                currentDirection = directionHeading ? Direction.UP : Direction.DOWN;
                break;
        }
    }
    public boolean updatePosition(int nrSteps){
        int newX = this.position.getX();
        int newY = this.position.getY();

        if (Direction.NORTH.equals(this.currentDirection) || Direction.UP.equals(this.currentDirection)) {
            newY = newY + nrSteps;
        }else if (Direction.DOWN.equals(this.currentDirection)){
            newY = newY - nrSteps;
        }else if (Direction.LEFT.equals(this.currentDirection)){
            newX = newX - nrSteps;
        }else if (Direction.RIGHT.equals(this.currentDirection)){
            newX = newX + nrSteps;
        }
        World world = World.getInstance();
        Position newPosition = new Position(newX, newY);
        positionCheck = world.isPositionBlocked(newPosition.getX(),newPosition.getY());
        pathCheck = world.isPathBlocked(position.getX(),position.getY(),newPosition.getX(),newPosition.getY());

        if(positionCheck){
            return false;
        }
        if(pathCheck){
            return false;
        }
        if (newPosition.isIn(TOP_LEFT,BOTTOM_RIGHT)){
            this.position = newPosition;
            return true;
        }
        return false;
    }


    public ArrayList<String> look(){
        World world = World.getInstance();

        if (Direction.NORTH.equals(this.currentDirection) || Direction.UP.equals(this.currentDirection)){
            pathCheck = world.isPathBlocked(position.getX(),position.getY(),position.getX(), 200);

            if (pathCheck){
                for (Obstacle obs : world.obstaclesLook){
                    obstaclesNorth.add(String.format("North Obstacles " + obs.getX() + ", " +  obs.getY() + " (to " +  (obs.getX()+4) + ", " + (obs.getY()+4) +")"));
                }
                world.obstaclesLook.clear();
                return obstaclesNorth;
            }else {
                System.out.println("No Obstacles for North");
            }
        }else if (Direction.RIGHT.equals(this.currentDirection)){
            pathCheck = world.isPathBlocked(position.getX(),position.getY(),200, position.getY());
            if (pathCheck){
                for (Obstacle obs : world.obstaclesLook){
                    obstaclesEast.add(String.format("East Obstacles " + obs.getX() + ", " +  obs.getY() + " (to " +  (obs.getX()+4) + ", " + (obs.getY()+4) +")"));
                }
                world.obstaclesLook.clear();
                return obstaclesEast;
            }else {
                System.out.println("No Obstacles for EAST");
            }
        }else if (Direction.DOWN.equals(this.currentDirection)) {
            pathCheck = world.isPathBlocked(position.getX(), position.getY(), position.getX(), -200);
            if (pathCheck) {
                for (Obstacle obs : world.obstaclesLook) {
                    obstaclesSouth.add(String.format("South Obstacles " + obs.getX() + ", " + obs.getY() + " (to " + (obs.getX() + 4) + ", " + (obs.getY() + 4) + ")"));
                }
                world.obstaclesLook.clear();
                return obstaclesSouth;
            } else {
                System.out.println("No Obstacles for EAST");
            }
        }
        else if (Direction.LEFT.equals(this.currentDirection)) {
            pathCheck = world.isPathBlocked(position.getX(), position.getY(), -200, position.getY());
            if (pathCheck) {
                for (Obstacle obs : world.obstaclesLook) {
                    obstaclesWest.add(String.format("West Obstacles " + obs.getX() + ", " + obs.getY() + " (to " + (obs.getX() + 4) + ", " + (obs.getY() + 4) + ")"));
                }
                world.obstaclesLook.clear();
                return obstaclesWest;
            } else {
                System.out.println("No Obstacles for EAST");
            }
        }
        return new ArrayList<>() ;
    }

    @Override
    public String toString() {
        return "[" + this.position.getX() + "," + this.position.getY() + "] "
                + this.name + "> " + this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

}