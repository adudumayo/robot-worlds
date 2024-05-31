package org.communication.server;
import java.util.ArrayList;
import java.util.Random;
import static org.communication.server.SimpleServer.*;
public class World {
    private static final Position TOP_LEFT = new Position(Config.getTopLeftX_world(), Config.getTopLeftY_world());
    private static final Position BOTTOM_RIGHT = new Position(Config.getBottomRightX_world(), Config.getBottomRightY_world());
    private static World instance;
    private Obstacle obstacle;
//    private Robot robot;

    public ArrayList<Object> obstacles = new ArrayList<>();
    public ArrayList<Obstacle> obstaclesLook = new ArrayList<>();
    public ArrayList<Robot> robotLook = new ArrayList<>();

    private World() {
        Random random = new Random();
        this.obstacle = new Obstacle(0, 100);
        obstacles.add(obstacle);
        this.obstacle = new Obstacle(100, 0);
        obstacles.add(obstacle);
        this.obstacle = new Obstacle(0, -100);
        obstacles.add(obstacle);
        this.obstacle = new Obstacle(-100, 0);
        obstacles.add(obstacle);

    }

    public static World getInstance() {
        if (instance == null) {
            synchronized (World.class) {
                if (instance == null) {
                    instance = new World();
                }
            }
        }
        return instance;
    }

    public Obstacle getObstacle() {
        return obstacle;
    }

    public boolean isPositionBlocked(int x, int y) {
        for (Object obs : obstacles) {
            if (obs instanceof Obstacle obstacle) {
                // Check if the position (x, y) is within the obstacle's area
                if (x >= obstacle.getX() && x <= obstacle.getX() + 4 &&
                        y >= obstacle.getY() && y <= obstacle.getY() + 4) {
                    obstaclesLook.add(new Obstacle(obstacle.getX(), obstacle.getY()));
                    return true; // Position is blocked by an obstacle
                }
            }
        }
        return false; // Position is not blocked by any obstacle
    }

    public boolean isPathBlocked(int x1, int y1, int x2, int y2) {
        if (x1 == x2) {
            for (int i = Math.min(y1, y2); i <= Math.max(y1, y2); i++) {
                if (isPositionBlocked(x1, i)) {
                    return true; // Path is blocked by an obstacle
                }
            }
        } else if (y1 == y2) {
            for (int j = Math.min(x1, x2); j <= Math.max(x1, x2); j++) {
                if (isPositionBlocked(j, y1)) {
                    return true; // Path is blocked by an obstacle
                }
            }
        }
        return false; // Path is not blocked by any obstacle
    }

    public boolean isPositionBlockedRobot(int x, int y, Robot currentRobot) {
        for ( Robot robotObj : robotObjects ) {

            if (x == robotObj.getPosition().getX() && y == robotObj.getPosition().getY() && !robotObj.getName().equals(currentRobot.getName())){
                robotLook.add(robotObj);
                return true;
            }
        }
        return false; // Position is not blocked by any obstacle
    }

    public boolean isPathBlockedRobot(int x1, int y1, int x2, int y2, Robot currentRobot) {
        if (x1 == x2) {
            for (int i = Math.min(y1, y2); i <= Math.max(y1, y2); i++) {
                if (isPositionBlockedRobot(x1, i, currentRobot )) {
                    return true; // Path is blocked by an obstacle
                }
            }
        } else if (y1 == y2) {
            for (int j = Math.min(x1, x2); j <= Math.max(x1, x2); j++) {
                if (isPositionBlockedRobot(j, y1, currentRobot)) {
                    return true; // Path is blocked by an obstacle
                }
            }
        }
        return false; // Path is not blocked by any obstacle
    }


    public Position getTopLeft() {
        return TOP_LEFT;
    }

    public Position getBottomRight() {
        return BOTTOM_RIGHT;
    }

    public boolean isInWorld(Position position) {
        return position.isIn(TOP_LEFT, BOTTOM_RIGHT);
    }
}
