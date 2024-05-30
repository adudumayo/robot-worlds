package org.communication.server;

import java.util.ArrayList;


public class Robot {
    private final Position TOP_LEFT = new Position(Config.getTopLeftX_world(), Config.getTopLeftY_world());
    private final Position BOTTOM_RIGHT = new Position(Config.getBottomRightX_world(), Config.getBottomRightY_world());
    public static final Position CENTRE = new Position(0,0);

    public void setState(State state) {
        this.state = state;
    }
    private State state;
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
    public ArrayList<Integer> obstacleSteps = new ArrayList<>();

    ArrayList<String> allObstacles = new ArrayList<>();

    public Direction getCurrentDirection() {
        return currentDirection;
    }

    public String getStatus() {
        return status;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Robot(String name) {
        this.name = name;
        this.status = "NORMAL";
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

    public void look(int range) {
        World world = World.getInstance();

        allObstacles.clear();
        obstacleSteps.clear();
        obstaclesNorth.clear();
        obstaclesEast.clear();
        obstaclesSouth.clear();
        obstaclesWest.clear();

        // Check North direction
        pathCheck = world.isPathBlocked(position.getX(), position.getY(), position.getX(), position.getY() + range);
        if (pathCheck) {
            for (Obstacle obs : world.obstaclesLook) {
                obstaclesNorth.add(String.format("North Obstacle at (%d, %d) to (%d, %d)",
                        obs.getX(), obs.getY(), obs.getX() + 4, obs.getY() + 4));
                obstacleSteps.add(obs.getY() - position.getY());  // Calculate steps to the obstacle

            }
        } else {
            obstaclesNorth.add("No Obstacles for North");
            obstacleSteps.add(0);

        }
        world.obstaclesLook.clear();

        // Check East direction
        pathCheck = world.isPathBlocked(position.getX(), position.getY(), position.getX() + range, position.getY());
        if (pathCheck) {
            for (Obstacle obs : world.obstaclesLook) {
                obstaclesEast.add(String.format("East Obstacle at (%d, %d) to (%d, %d)",
                        obs.getX(), obs.getY(), obs.getX() + 4, obs.getY() + 4));
                obstacleSteps.add(obs.getX() - position.getX());  // Calculate steps to the obstacle

            }
        } else {
            obstaclesEast.add("No Obstacles for East");
            obstacleSteps.add(0);

        }
        world.obstaclesLook.clear();

        // Check South direction
        pathCheck = world.isPathBlocked(position.getX(), position.getY(), position.getX(), position.getY() - range);
        if (pathCheck) {
            for (Obstacle obs : world.obstaclesLook) {
                obstaclesSouth.add(String.format("South Obstacle at (%d, %d) to (%d, %d)",
                        obs.getX(), obs.getY(), obs.getX() + 4, obs.getY() + 4));
                obstacleSteps.add(position.getY() - obs.getY());  // Calculate steps to the obstacle

            }
        } else {
            obstaclesSouth.add("No Obstacles for South");
            obstacleSteps.add(0);

        }
        world.obstaclesLook.clear();

        // Check West direction
        pathCheck = world.isPathBlocked(position.getX(), position.getY(), position.getX() - range, position.getY());
        if (pathCheck) {
            for (Obstacle obs : world.obstaclesLook) {
                obstaclesWest.add(String.format("West Obstacle at (%d, %d) to (%d, %d)",
                        obs.getX(), obs.getY(), obs.getX() + 4, obs.getY() + 4));
                obstacleSteps.add(position.getX() - obs.getX());  // Calculate steps to the obstacle

            }
        } else {
            obstaclesWest.add("No Obstacles for West");
            obstacleSteps.add(0);

        }
        world.obstaclesLook.clear();

        // Combine all obstacles into a single list to return
        allObstacles.addAll(obstaclesNorth);
        allObstacles.addAll(obstaclesEast);
        allObstacles.addAll(obstaclesSouth);
        allObstacles.addAll(obstaclesWest);
    }


    public void fireShots(){
        this.getState().decrementShots();
    }

    public ArrayList<String> displayObstaclesForLook(){
        return allObstacles;
    }

    @Override
    public String toString() {
        return "[" + this.position.getX() + "," + this.position.getY() + "] "
                + this.name + "> " + this.status;
    }

    public String coordinatePosition(){
        return "[" + this.position.getX() + "," + this.position.getY() + "]";
    }


    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public State getState() {
        return state;
    }

}