package com.groupesan.project.java.scrumsimulator.mainpackage.impl;

import com.groupesan.project.java.scrumsimulator.mainpackage.core.ScrumIdentifier;
import com.groupesan.project.java.scrumsimulator.mainpackage.core.ScrumObject;
import java.util.ArrayList;
import java.util.List;

public class Spike extends ScrumObject {
    private SpikeIdentifier id;
    private String userStory;
    private String spikeName;
    private String description;
    private String expectedOutcome;
    private Integer timeBoxDuration;
    private String status;


    // private ArrayList<Task> tasks;  TODO: implement tasks

    /**
     * Creates a spike. Leaves the description as an empty string.
     *
     * @param spikeName the name for the spike
     * @param description the description for the spike as a way of estimating required effort.
     * @param expectedOutcome the expected outcome for the spike.
     * @param timeBoxDuration the timeBoxDuration for the spike as a way of estimating time required to complete spike.
     * @param status the status of the spike.
     * @param userStory the status of the spike.
     */
    public Spike(String spikeName, String description, String expectedOutcome, Integer timeBoxDuration, String status, String userStory) {
        this.userStory = userStory;
        this.spikeName = spikeName;
        this.description = description;
        this.expectedOutcome = expectedOutcome;
        this.timeBoxDuration = timeBoxDuration;
        this.status = status;
    }


    protected void register() {
        this.id = new SpikeIdentifier(ScrumIdentifierStoreSingleton.get().getNextId());
    }

    /**
     * Gets the identifier object for this Spike. **This will throw an exception if register()
     * has not been called yet.**
     *
     * @return The ScrumIdentifier for this Spike
     */
    public ScrumIdentifier getId() {
        if (!isRegistered()) {
            throw new IllegalStateException(
                    "This Spike has not been registered and does not have an ID yet!");
        }
        return id;
    }

    public Integer getTimeBoxDuration() {
        return timeBoxDuration;
    }

    public void setTimeBoxDuration(Integer timeBoxDuration) {
        this.timeBoxDuration = timeBoxDuration;
    }
    /**
     * Get the name for this Spike
     *
     * @return the name of this Spike as a string
     */
    public String getName() {
        return spikeName;
    }

    /**
     * Sets the name of the Spike to the specified string
     *
     * @param spikeName the string to set the name to
     */
    public void setName(String spikeName) {
        this.spikeName = spikeName;
    }

    /**
     * Get the description text of this Spike
     *
     * @return the description of this Spike as a string.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the Description of the User Story to the specified string
     *
     * @param description the string to set the description to
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Get the expected outcome of this Spike
     *
     * @return the expected outcome of this Spike as a string
     */
    public String getExpectedOutcome() {
        return expectedOutcome;
    }

    /**
     * Set the  of the User Story to the specified value
     *
     * @param expectedOutcome the point value as a double. Usually an element of the fibonacci sequence.
     */
    public void setExpectedOutcome(String expectedOutcome) {
        this.expectedOutcome = expectedOutcome;
    }

    public String getSpikeStatus() {return status;}

    public void setSpikeStatus(String status) { this.status = status;}

    public String getUserStory() {return userStory;}

    public void setUserStory(String userStory) { this.userStory = userStory;}

    /**
     * [NOT IMPLEMENTED] return all child scrum objects of this object. Usually this would be tasks.
     *
     * @return a List containing all child ScrumObjects of this UserStory
     */
    public List<ScrumObject> getChildren() {
        return new ArrayList<>(); // TODO: implement tasks
    }

    /**
     * returns this spike's ID and name as text in the following format: Spike#3
     *
     * @return a string of the following format: "Spike#3"
     */
    @Override
    public String toString() {
        if (isRegistered()) {
            return this.getId().toString() + " - " + spikeName;
        }
        return "(unregistered) - " + getName();
    }

}
