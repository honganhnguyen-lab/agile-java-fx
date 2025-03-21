package com.groupesan.project.java.scrumsimulator.mainpackage.impl;

public class SpikeFactory {

    private static SpikeFactory spikeFactory;

    public static SpikeFactory getInstance() {
        if (spikeFactory == null) {
            spikeFactory = new SpikeFactory();
        }
        return spikeFactory;
    }

    private SpikeFactory() {}

    public Spike createNewSpike(String spikeName, String description, String spikeOutcome, Integer timeBoxDuration, String status, String selectedUserStory) {
        Spike newSpike = new Spike(spikeName, description, spikeOutcome, timeBoxDuration, status, selectedUserStory);
        return newSpike;
    }
}
