package org.communication.server;

import java.util.ArrayList;
import java.util.Map;

public class Response {
    private String result;
    private Map<String, Object> data;
    private State state;


    // Getter and setter methods
    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }


}
