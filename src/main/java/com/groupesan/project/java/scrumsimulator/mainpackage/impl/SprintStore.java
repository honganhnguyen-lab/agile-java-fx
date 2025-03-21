package com.groupesan.project.java.scrumsimulator.mainpackage.impl;

import java.util.ArrayList;
import java.util.List;

public class SprintStore {
    private static SprintStore sprintStore;
    private List<Sprint> sprints = new ArrayList<>();

    public static SprintStore getInstance() {
        if (sprintStore == null) {
            sprintStore = new SprintStore();
        }
        return sprintStore;
    }

    private SprintStore() {
        sprints = new ArrayList<>();
    }

    public void addSprint(Sprint sprint) {
        sprints.add(sprint);
    }

    public List<Sprint> getSprints() {
        return new ArrayList<>(sprints);
    }

    public Sprint getActiveSprint() {
        for (Sprint sprint : sprints) {
            if (sprint.isActive()) {
                return sprint;
            }
        }
        return null;
    }

    public void setActiveSprint(long sprintId) {
        for (Sprint sprint : sprints) {
            sprint.setActive(sprint.getId() == sprintId);
        }
    }
}

