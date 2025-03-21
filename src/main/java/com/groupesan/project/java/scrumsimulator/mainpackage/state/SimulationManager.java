package com.groupesan.project.java.scrumsimulator.mainpackage.state;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class SimulationManager {

    private static final String SIMULATION_FILE = "src/main/resources/simulation.json"; // Path to your JSON file

    // Method to modify an existing simulation
    public boolean modifySimulation(String simName, String numberOfSprints, String durationOfSprint, String userId) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Read the entire JSON structure
            SimulationData data = objectMapper.readValue(new File(SIMULATION_FILE), SimulationData.class);
            List<Simulation> simulations = data.getSimulations(); // Get the list of simulations

            // Check if the user ID exists and if the role is "scrum master"
            boolean userExists = false;
            boolean isScrumMaster = false;

            for (Simulation sim : simulations) {
                for (User user : sim.getUsers()) {
                    if (user.getId().equals(userId)) {
                        userExists = true;
                        if (user.getRole().equalsIgnoreCase("scrum master")) {
                            isScrumMaster = true;
                        }
                        break;
                    }
                }
            }

            if (!userExists) {
                System.out.println("User ID does not exist in the simulation.");
                return false;
            }

            if (!isScrumMaster) {
                System.out.println("User is not a scrum master.");
                return false;
            }

            // Find the simulation by name and proceed with modification
            for (Simulation sim : simulations) {
                if (sim.getName().equals(simName)) {
                    // Update fields
                    sim.setNumberOfSprints(numberOfSprints);
                    sim.setDurationOfSprint(durationOfSprint);

                    // Create a Sprints array of specified length
                    int sprintsCount = Integer.parseInt(numberOfSprints);
                    String[] sprints = new String[sprintsCount];
                    for (int i = 0; i < sprintsCount; i++) {
                        sprints[i] = durationOfSprint;
                    }
                    sim.setSprints(sprints);

                    // Write the updated simulations back to the JSON file
                    objectMapper.writeValue(new File(SIMULATION_FILE), data); // Write the complete data back
                    return true; // Modification successful
                }
            }
        } catch (IOException e) {
            e.printStackTrace(); // Consider using a logging framework for better logging
        }
        return false; // Simulation name not found
    }

    // Define a class for the SimulationData to match the JSON structure
    public static class SimulationData {
        @JsonProperty("Simulations")
        private List<Simulation> simulations;

        public List<Simulation> getSimulations() {
            return simulations;
        }

        public void setSimulations(List<Simulation> simulations) {
            this.simulations = simulations;
        }
    }

    // Define a class for the Simulation to match the JSON structure
    public static class Simulation {
        @JsonProperty("Status")
        private String status;

        @JsonProperty("Sprints")
        private String[] sprints;

        @JsonProperty("Events")
        private String[] events;

        @JsonProperty("NumberOfSprints")
        private String numberOfSprints;

        @JsonProperty("ID")
        private String id;

        @JsonProperty("Users")
        private List<User> users;

        @JsonProperty("Name")
        private String name;

        @JsonProperty("DurationOfSprint")
        private String durationOfSprint;

        // Getters and setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getNumberOfSprints() { return numberOfSprints; }
        public void setNumberOfSprints(String numberOfSprints) { this.numberOfSprints = numberOfSprints; }

        public String getDurationOfSprint() { return durationOfSprint; }
        public void setDurationOfSprint(String durationOfSprint) { this.durationOfSprint = durationOfSprint; }

        public String[] getSprints() { return sprints; }
        public void setSprints(String[] sprints) { this.sprints = sprints; }

        public List<User> getUsers() { return users; }
        public void setUsers(List<User> users) { this.users = users; }
    }

    // Define a class for User to match the JSON structure
    public static class User {
        @JsonProperty("ID")
        private String id;

        @JsonProperty("Username")
        private String username;

        @JsonProperty("Role")
        private String role;

        // Getters and setters
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }

        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
    }
}
