package org.communication.server;
import java.util.ArrayList;


public class World {
    private static final Position TOP_LEFT = new Position(-200, 200);
    private static final Position BOTTOM_RIGHT = new Position(200, -200);

    private static World instance;
    private Obstacle obstacle;
    public ArrayList<Obstacle> obstacles = new ArrayList<>();


    private World() {
        // Add hardcoded obstacle
//        addObstacle(new Obstacle(-100, 100)); // Example coordinates
//        addObstacle(new Obstacle(10, 10));
        this.obstacle = new Obstacle(-100,100);
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
        for (Obstacle obstacle : obstacles) {
            // Check if the position (x, y) is within the obstacle's area
            if (x >= obstacle.getX() && x <= obstacle.getX() + 4 &&
                    y >= obstacle.getY() && y <= obstacle.getY() + 4) {
                return true; // Position is blocked by an obstacle
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
