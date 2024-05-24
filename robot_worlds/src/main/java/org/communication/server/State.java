package org.communication.server;

public class State {
    private String position;
    private Direction direction;
    private int shields; // number of hits the shield can absorb
    private int shots;// number of shots left in the gun
    private String status;

    public State(int shields, int shots){

        this.shields = shields;
        this.shots = shots;
    }

    // Getter and setter methods
    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public int getShields() {
        return shields;
    }

    public void setShields(int shields) {
        this.shields = shields;
    }

    public int getShots() {
        return shots;
    }

    public void setShots(int shots) {
        this.shots = shots;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
