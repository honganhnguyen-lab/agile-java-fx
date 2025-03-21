package com.groupesan.project.java.scrumsimulator.mainpackage.impl;

public class Solution {
    private String soln_id;
    private String soln_name;
    private String blockerId;
    private int probability;

    public Solution(String sid, String sname, String blockerId, int probability) {
        this.soln_id = sid;
        this.soln_name = sname;
        this.blockerId = blockerId;
        this.probability = probability;
    }

    public String getSid() {
        return soln_id;
    }

    public void setSid(String sid) {
        this.soln_id = sid;
    }

    public String getSname() {
        return soln_name;
    }

    public void setSname(String sname) {
        this.soln_name = sname;
    }

    public String getBlockerId() {
        return blockerId;
    }

    public void setBlockerId(String blockerId) {
        this.blockerId = blockerId;
    }

    public int getProbability() {
        return probability;
    }

    public void setProbability(int probability) {
        this.probability = probability;
    }

}