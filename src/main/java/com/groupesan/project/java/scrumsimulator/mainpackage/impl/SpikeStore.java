package com.groupesan.project.java.scrumsimulator.mainpackage.impl;

import java.util.ArrayList;
import java.util.List;

public class SpikeStore {
    private static SpikeStore spikeStore;

    public static SpikeStore getInstance() {
        if (spikeStore == null) {
            spikeStore = new SpikeStore();
        }
        return spikeStore;
    }

    private List<Spike> spikes;

    private SpikeStore() {
        spikes = new ArrayList<>();
    }

    public void addSpike(Spike spike) {
        spikes.add(spike);
    }

    public List<Spike> getSpikes() {
        return new ArrayList<>(spikes);
    }
}
