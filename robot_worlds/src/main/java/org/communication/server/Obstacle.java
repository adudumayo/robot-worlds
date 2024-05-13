
package org.communication.server;

public class Obstacle {
    private int x;
    private int y;

    // Constructor
    public Obstacle(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // Getters
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return "Obstacles: - At position (" + getX() + ", " + getY() + ")";
    }
}