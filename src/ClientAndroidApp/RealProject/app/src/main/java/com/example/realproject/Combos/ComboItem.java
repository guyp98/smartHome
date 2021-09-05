package com.example.realproject.Combos;

public class ComboItem {
    private String username;
    private String scenarioOn;
    private String scenarioOff;



    public ComboItem(String username, String scenarioOn,String scenarioOff) {
        this.username = username;
        this.scenarioOn = scenarioOn;
        this.scenarioOff =scenarioOff;
    }

    public String getUsername() {
        return username;
    }

    public String getScenarioOn() {
        return scenarioOn;
    }
    public String getScenarioOff() {
        return scenarioOff;
    }


}
