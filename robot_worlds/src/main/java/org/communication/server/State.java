package org.communication.server;

public class State {
    private String position;
    private Direction direction;
    private int shields; // number of hits the shield can absorb
    private int shots;// number of shots left in the gun
    private String status;

    public State(int shots){
        this.shots = shots;
    }

    public State(int shields, int shots){

        this.shields = shields;
        this.shots = shots;
        this.status = "NORMAL";
    }



    // Getter and setter methods

    public void decrementShots(){
        if (this.shots > 0){
            this.shots-=1;
        }else{
            this.shots = 0;
        }

    }

    public void decrementShield(){
        if (this.shields > 0){
            this.shields-=1;
        }else{
            this.shields = 0;
        }

    }

    public void decrementShieldBy(int amount) {
        this.shields = Math.max(0, this.shields - amount);
    }

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
