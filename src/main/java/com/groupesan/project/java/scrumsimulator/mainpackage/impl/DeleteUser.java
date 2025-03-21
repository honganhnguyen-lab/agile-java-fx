package com.groupesan.project.java.scrumsimulator.mainpackage.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

public class DeleteUser {
    private static final String JSON_FILE_PATH = "src/main/resources/simulation.json";

    /**
     * Deletes a user from the simulation.json file based on the provided user ID.
     *
     * @param userId The ID of the user to be deleted.
     * @return true if the user was deleted successfully, false otherwise.
     */
    public static boolean deleteUser(String username) {
        File file = new File(JSON_FILE_PATH);

        // Check if the JSON file exists
        if (!file.exists()) {
            System.err.println("JSON file not found.");
            return false; // Exit if the file does not exist
        }

        // Create variables for input and output streams
        try (FileInputStream fis = new FileInputStream(file)) {
            // Create the JSONTokener outside of try-with-resources
            JSONTokener tokener = new JSONTokener(fis);
            JSONObject root = new JSONObject(tokener);
            JSONArray simulations = root.optJSONArray("Simulations");
            if (simulations == null || simulations.length() == 0) {
                System.err.println("No simulations found.");
                return false; // Exit if there are no simulations
            }

            // Assuming we want to delete from the first simulation
            JSONObject simulation = simulations.getJSONObject(0);
            JSONArray users = simulation.optJSONArray("Users");

            if (users == null) {
                System.err.println("No users found.");
                return false; // Exit if there are no users
            }

            // Iterate through the users to find and remove the user with the given ID
            boolean userFound = false;
            for (int i = 0; i < users.length(); i++) {
                JSONObject user = users.getJSONObject(i);
                if (user.getString("Username").equals(username)) {
                    users.remove(i);
                    userFound = true;
                    System.out.println("User deleted successfully");
                    break; // Exit the loop once the user is found and removed
                }
            }

            if (!userFound) {
                System.out.println("Username not found");
                return false;
            }

            // Write the updated JSON back to the file
            try (FileWriter fileWriter = new FileWriter(file, StandardCharsets.UTF_8)) {
                fileWriter.write(root.toString(4)); // Write updated JSON data back to file
            }

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
