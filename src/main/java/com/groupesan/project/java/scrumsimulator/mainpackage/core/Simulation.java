package com.groupesan.project.java.scrumsimulator.mainpackage.core;

import java.util.ArrayList;
import java.util.List;

public class Simulation {

    private String simulationName;
    private Teacher teacher;
    private final List<Player> players = new ArrayList<>();
    private int sprintCount;
    private int durationOfSprintValue;

    public Simulation(String simulationName, Teacher teacher, int sprintCount, int durationOfSprintValue) {
        this.simulationName = simulationName;
        this.teacher = teacher;
        this.sprintCount = sprintCount;
        this.durationOfSprintValue = durationOfSprintValue;
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public boolean removePlayer(Player player) {
        return players.remove(player);
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher newTeacher) {
        teacher = newTeacher;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public String getSimulationName() {
        return simulationName;
    }

    public void setSimulationName(String simulationName) {
        this.simulationName = simulationName;
    }

    public void setSprintCount(int sprintCount) {
        this.sprintCount = sprintCount;
    }

    public void setDurationOfSprintValue(int durationOfSprintValue) {
        this.durationOfSprintValue = durationOfSprintValue;
    }

    @Override
    public String toString() {
        String result = "[Simulation] " + getSimulationName() + "\n";
        result += "Sprints: " + sprintCount + "\n";
        result += "Duration of Sprint: " + durationOfSprintValue + "\n";
        for (Player player : players) {
            result += player + "\n";
        }
        return result;
    }
}
