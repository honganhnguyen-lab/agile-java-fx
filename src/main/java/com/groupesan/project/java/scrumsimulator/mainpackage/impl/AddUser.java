package com.groupesan.project.java.scrumsimulator.mainpackage.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

public class AddUser {
    private static final String JSON_FILE_PATH = "src/main/resources/simulation.json";

    /**
     * Adds a user with a username, type, and role to the first simulation in the JSON file.
     *
     * @param username name of the user: String
     * @param type     type of user; player or teacher: String
     * @param roleName name of role: String
     */
    public static void addUser(String username, String type, String roleName) {
        File file = new File(JSON_FILE_PATH);

        // Create the file if it doesn't exist
        if (!file.exists()) {
            try {
                if (!file.createNewFile()) {
                    System.err.println("Failed to create the JSON file.");
                    return;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }

        // Read the JSON file and parse its content
        JSONObject root;
        try (FileInputStream fis = new FileInputStream(file)) {
            JSONTokener tokener = new JSONTokener(fis);
            if (file.length() > 0) { // Check if file is not empty
                root = new JSONObject(tokener);
            } else {
                root = new JSONObject();
                root.put("Simulations", new JSONArray());
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // Retrieve or create the "Simulations" array
        JSONArray simulations = root.optJSONArray("Simulations");
        if (simulations == null) {
            simulations = new JSONArray();
            root.put("Simulations", simulations);
        }

        // Use the first simulation if it exists, or create a new one
        JSONObject simulation;
        if (simulations.length() > 0) {
            simulation = simulations.getJSONObject(0); // Use the first simulation
        } else {
            simulation = new JSONObject();
            simulation.put("ID", "default_simulation_id");
            simulation.put("Status", "New");
            simulation.put("Sprints", new JSONArray());
            simulation.put("Events", new JSONArray());
            simulation.put("Users", new JSONArray());
            simulation.put("Name", "default_simulation");
            simulations.put(simulation);
        }

        // Retrieve or create the "Users" array in the simulation
        JSONArray users = simulation.optJSONArray("Users");
        if (users == null) {
            users = new JSONArray();
            simulation.put("Users", users);
        }

        // Create a new user object with a simpler structure
        JSONObject newUser = new JSONObject();
        String userId = "u" + System.currentTimeMillis();
        newUser.put("ID", userId);
        newUser.put("Username", username);
        newUser.put("Role", roleName);

        users.put(newUser); // Add the new user to the "Users" array

        // Write the updated JSON back to the file
        try (FileWriter fileWriter = new FileWriter(file, StandardCharsets.UTF_8)) {
            fileWriter.write(root.toString(4));
            System.out.println("User added successfully");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
