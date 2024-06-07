package org.communication.server;

public class Config {
    public static int topLeftX_world = -200;
    public static int topLeftY_world = 200;
    public static int bottomRightX_world = 200;
    public static int bottomRightY_world = -200;
    public static int visibility = 200;
    public static int maxShield = 20;
    public static int maxShot = 20;
    public static int reloadTime = 5000;
    public static int repairTime = 5000;

    // Getters and Setters
    public static int getTopLeftX_world() {
        return topLeftX_world;
    }

    public static void setTopLeftX_world(int value) {
        topLeftX_world = value;
    }

    public static int getTopLeftY_world() {
        return topLeftY_world;
    }

    public static void setTopLeftY_world(int value) {
        topLeftY_world = value;
    }

    public static int getBottomRightX_world() {
        return bottomRightX_world;
    }
    public static void setBottomRightX_world(int value) {
        bottomRightX_world = value;
    }

    public static int getBottomRightY_world() {
        return bottomRightY_world;
    }

    public static void setBottomRightY_world(int value) {
        bottomRightY_world = value;
    }

    public static int getVisibility() {
        return visibility;
    }
    public static void setVisibility(int value) {
        visibility = value;
    }

    public static int getMaxShield() {
        return maxShield;
    }
    public static void setMaxShield(int value) {
        maxShield = value;
    }

    public static int getMaxShot() {
        return maxShot;
    }
    public static void setMaxShot(int value) {
        maxShot = value;
    }

    public static int getReloadTime() {
        return reloadTime;
    }
    public static void setReloadTime(int value) {
        reloadTime = value;
    }

    public static int getRepairTime() {
        return repairTime;
    }
    public static void setRepairTime(int value) {
        repairTime = value;
    }
}
