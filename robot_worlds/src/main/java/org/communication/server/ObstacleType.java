package org.communication.server;

public class ObstacleType {

//    private String[] lookArguments;
    private String direction;
    private String type;
    private int distance;

    public ObstacleType(String direction, String type, int distance) {
        this.direction = direction;
        this.type = type;
        this.distance = distance;
    }

    // Getter and setter methods
    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }
}
