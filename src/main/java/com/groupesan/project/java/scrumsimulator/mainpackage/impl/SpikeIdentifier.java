package com.groupesan.project.java.scrumsimulator.mainpackage.impl;

import com.groupesan.project.java.scrumsimulator.mainpackage.core.ScrumIdentifier;

public class SpikeIdentifier extends ScrumIdentifier {
    public SpikeIdentifier(int value) {
        super(value);
    }

    public String toString() {
        return "Spike#" + this.id;
    }
}
