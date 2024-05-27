package org.communication.server;
import java.util.ArrayList;

import static org.communication.server.MultiServers.topLeftX_world;
import static org.communication.server.MultiServers.topLeftY_world;
import static org.communication.server.MultiServers.bottomRightX_world;
import static org.communication.server.MultiServers.bottomRightY_world;
public class World {
    private static final Position TOP_LEFT = new Position(topLeftX_world, topLeftY_world);
    private static final Position BOTTOM_RIGHT = new Position(bottomRightX_world, bottomRightY_world);

    private static World instance;
    private Obstacle obstacle;
    public ArrayList<Object> obstacles = new ArrayList<>();
    public ArrayList<Obstacle> obstaclesLook = new ArrayList<>();


    private World() {
        // Add hardcoded obstacle
//        addObstacle(new Obstacle(-100, 100)); // Example coordinates
//        addObstacle(new Obstacle(10, 10));
//
//        this.obstacle = new Obstacle(-100,100);
//        obstacles.add(obstacle);
        // font obstacle
        this.obstacle = new Obstacle(0, 100);
        obstacles.add(obstacle);
//        this.obstacle = new Obstacle(0, 150);
//        obstacles.add(obstacle);
        this.obstacle = new Obstacle(100, 0);
        obstacles.add(obstacle);
        // back obstacle
        this.obstacle = new Obstacle(0, -100);
        obstacles.add(obstacle);
        // left obstacle
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
//    public void addObstacle(Obstacle obstacle) {
//        obstacles.add(obstacle);
//    }
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
