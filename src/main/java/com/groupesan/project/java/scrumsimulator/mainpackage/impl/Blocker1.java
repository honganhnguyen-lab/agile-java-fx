package com.groupesan.project.java.scrumsimulator.mainpackage.impl;

public class Blocker1 {
    private String id;
    private String BName;
    private String Bdesc; // blocker description
    private int probability;
    private boolean resolved; //blocker

    public Blocker1() {}

    public Blocker1(String id, String BName, String Bdesc, int probability) {
        this.id = id;
        this.BName = BName;
        this.Bdesc = Bdesc;
        this.probability = probability;
        this.resolved = false;
    }

    public String getBId() { return id; }
    public void setBId(String id) { this.id = id; }

    public String getBName() { return BName; }
    public void setBName(String BName) { this.BName = BName; }

    public String getDesc() { return Bdesc; }
    public void setDesc(String Bdesc) { this.Bdesc = Bdesc; }

    public Integer getProbability() { return probability; }
    public void setProbability(int probability) { this.probability = probability; }

    public boolean isResolved() { return resolved; }
    public void setResolved(boolean resolved) { this.resolved = resolved; }
}