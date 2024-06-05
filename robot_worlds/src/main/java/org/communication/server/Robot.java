package org.communication.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import static org.communication.server.SimpleServer.*;


public class Robot {
    private final Position TOP_LEFT = new Position(Config.getTopLeftX_world(), Config.getTopLeftY_world());
    private final Position BOTTOM_RIGHT = new Position(Config.getBottomRightX_world(), Config.getBottomRightY_world());
    public static final Position CENTRE = new Position(0,0);


    private State state;
    private Position position;
    private Direction currentDirection;
    private String status;
    private String name;
    public boolean positionCheck;
    public boolean pathCheck;
    public boolean positionCheckRobot;
    public boolean pathCheckRobot;
    private int distance;
    public ArrayList<String> obstaclesNorth = new ArrayList<>();
    public ArrayList<String> obstaclesEast = new ArrayList<>();
    public ArrayList<String> obstaclesSouth = new ArrayList<>();
    public ArrayList<String> obstaclesWest = new ArrayList<>();
    public Map<String, Integer> obstacleSteps = new HashMap<>();

    ArrayList<String> allObstacles = new ArrayList<>();
    public void setState(State state) {
        this.state = state;
    }
    public Direction getCurrentDirection() {
        return currentDirection;
    }

    public String getStatus() {
        return status;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }
    //randomPosition(this,world);
    public Robot(String name) {
        World world = World.getInstance();
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
        pathCheckRobot = world.isPathBlockedRobot(position.getX(),position.getY(), newPosition.getX(), newPosition.getY(), this);

        if(positionCheck){
            return false;
        }
        if(pathCheck){
            return false;
        }
        if(pathCheckRobot){
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
        pathCheckRobot = world.isPathBlockedRobot(position.getX(), position.getY(), position.getX(), position.getY() + range, this);
        if (pathCheck || pathCheckRobot) {
            for (Obstacle obs : world.obstaclesLook) {
                obstaclesNorth.add(String.format("North Obstacle at (%d, %d) to (%d, %d)",
                        obs.getX(), obs.getY(), obs.getX() + 4, obs.getY() + 4));
                obstacleSteps.put("North_obstacle", obs.getY() - position.getY());
            }
            for (Robot robot : world.robotLook) {
                obstaclesNorth.add(String.format("North Robot at (%d, %d)", robot.getPosition().getX(), robot.getPosition().getY()));
                obstacleSteps.put("North_robot", robot.getPosition().getY() - position.getY());
            }
        } else {
            obstaclesNorth.add("No Obstacles for North");
            obstacleSteps.put("North_none", 0);
        }
        world.obstaclesLook.clear();
        world.robotLook.clear();

        // Check East direction
        pathCheck = world.isPathBlocked(position.getX(), position.getY(), position.getX() + range, position.getY());
        pathCheckRobot = world.isPathBlockedRobot(position.getX(), position.getY(), position.getX() + range, position.getY(), this);
        if (pathCheck || pathCheckRobot) {
            for (Obstacle obs : world.obstaclesLook) {
                obstaclesEast.add(String.format("East Obstacle at (%d, %d) to (%d, %d)",
                        obs.getX(), obs.getY(), obs.getX() + 4, obs.getY() + 4));
                obstacleSteps.put("East_obstacle", obs.getX() - position.getX());
            }
            for (Robot robot : world.robotLook) {
                obstaclesEast.add(String.format("East Robot at (%d, %d)", robot.getPosition().getX(), robot.getPosition().getY()));
                obstacleSteps.put("East_robot", robot.getPosition().getX() - position.getX());
            }
        } else {
            obstaclesEast.add("No Obstacles for East");
            obstacleSteps.put("East_none", 0);
        }
        world.obstaclesLook.clear();
        world.robotLook.clear();

        // Check South direction
        pathCheck = world.isPathBlocked(position.getX(), position.getY(), position.getX(), position.getY() - range);
        pathCheckRobot = world.isPathBlockedRobot(position.getX(), position.getY(), position.getX(), position.getY() - range, this);
        if (pathCheck || pathCheckRobot) {
            for (Obstacle obs : world.obstaclesLook) {
                obstaclesSouth.add(String.format("South Obstacle at (%d, %d) to (%d, %d)",
                        obs.getX(), obs.getY(), obs.getX() + 4, obs.getY() + 4));
                obstacleSteps.put("South_obstacle", position.getY() - obs.getY());
            }
            for (Robot robot : world.robotLook) {
                obstaclesSouth.add(String.format("South Robot at (%d, %d)", robot.getPosition().getX(), robot.getPosition().getY()));
                obstacleSteps.put("South_robot", position.getY() - robot.getPosition().getY());
            }
        } else {
            obstaclesSouth.add("No Obstacles for South");
            obstacleSteps.put("South_none", 0);
        }
        world.obstaclesLook.clear();
        world.robotLook.clear();

        // Check West direction
        pathCheck = world.isPathBlocked(position.getX(), position.getY(), position.getX() - range, position.getY());
        pathCheckRobot = world.isPathBlockedRobot(position.getX(), position.getY(), position.getX() - range, position.getY(), this);
        if (pathCheck || pathCheckRobot) {
            for (Obstacle obs : world.obstaclesLook) {
                obstaclesWest.add(String.format("West Obstacle at (%d, %d) to (%d, %d)",
                        obs.getX(), obs.getY(), obs.getX() + 4, obs.getY() + 4));
                obstacleSteps.put("West_obstacle", position.getX() - obs.getX());
            }
            for (Robot robot : world.robotLook) {
                obstaclesWest.add(String.format("West Robot at (%d, %d)", robot.getPosition().getX(), robot.getPosition().getY()));
                obstacleSteps.put("West_robot", position.getX() - robot.getPosition().getX());
            }
        } else {
            obstaclesWest.add("No Obstacles for West");
            obstacleSteps.put("West_none", 0);
        }
        world.obstaclesLook.clear();
        world.robotLook.clear();

        // Combine all obstacles into a single list to return
        allObstacles.addAll(obstaclesNorth);
        allObstacles.addAll(obstaclesEast);
        allObstacles.addAll(obstaclesSouth);
        allObstacles.addAll(obstaclesWest);
    }

    public Robot fireShots() {
        World world = World.getInstance();
        int targetX = this.position.getX();
        int targetY = this.position.getY();
        Robot hitRobot = null;

        // Calculate the target position based on the current direction
        if (this.currentDirection.equals(Direction.NORTH) || this.currentDirection.equals(Direction.UP)) {
            targetY += 5;
        } else if (this.currentDirection.equals(Direction.DOWN)) {
            targetY -= 5;
        } else if (this.currentDirection.equals(Direction.RIGHT)) {
            targetX += 5;
        } else if (this.currentDirection.equals(Direction.LEFT)) {
            targetX -= 5;
        }

        // Check each robot in the world to see if they are within the target range
        for (Robot robot : robotObjects) {
            if (!robot.equals(this)) { // Ensure not checking against itself
                int distance = 0;

                if (this.currentDirection.equals(Direction.NORTH) || this.currentDirection.equals(Direction.UP)) {
                    if (robot.getPosition().getX() == this.position.getX() && robot.getPosition().getY() > this.position.getY() && robot.getPosition().getY() <= targetY) {
                        distance = robot.getPosition().getY() - this.position.getY();
                    }
                } else if (this.currentDirection.equals(Direction.DOWN)) {
                    if (robot.getPosition().getX() == this.position.getX() && robot.getPosition().getY() < this.position.getY() && robot.getPosition().getY() >= targetY) {
                        distance = this.position.getY() - robot.getPosition().getY();
                    }
                } else if (this.currentDirection.equals(Direction.RIGHT)) {
                    if (robot.getPosition().getY() == this.position.getY() && robot.getPosition().getX() > this.position.getX() && robot.getPosition().getX() <= targetX) {
                        distance = robot.getPosition().getX() - this.position.getX();
                    }
                } else if (this.currentDirection.equals(Direction.LEFT)) {
                    if (robot.getPosition().getY() == this.position.getY() && robot.getPosition().getX() < this.position.getX() && robot.getPosition().getX() >= targetX) {
                        distance = this.position.getX() - robot.getPosition().getX();
                    }
                }
                this.setDistance(distance);

                if (distance > 0 && distance <= 5) {
                    hitRobot = robot;
                    int damage = 0;
                    if (distance == 1) {
                        damage = 5;
                    } else if (distance == 2) {
                        damage = 4;
                    } else if (distance == 3) {
                        damage = 3;
                    } else if (distance == 4) {
                        damage = 2;
                    } else if (distance == 5) {
                        damage = 1;
                    }

                    robot.getState().decrementShieldBy(damage);
                    hitRobot.setState(robot.getState());}
            }
        }

        // Decrement the shots for the firing robot
        this.getState().decrementShots();

        return hitRobot;


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

    public Position randomPosition(Robot robot, World world){
        Random random = new Random();
        int randomX = random.nextInt(-190, 190);
        int randomY = random.nextInt(-190,190);
        positionCheck = world.isPositionBlocked(randomX,randomY);
        positionCheckRobot = world.isPositionBlockedRobot(randomX,randomY, robot);
        while (positionCheck || positionCheckRobot){
            randomX = random.nextInt(-190, 190);
            randomY = random.nextInt(-190,190);
            positionCheck = world.isPositionBlocked(randomX,randomY);
            positionCheckRobot = world.isPositionBlockedRobot(randomX,randomY, robot);
        }

        return new Position(randomX,randomY);
    }



}