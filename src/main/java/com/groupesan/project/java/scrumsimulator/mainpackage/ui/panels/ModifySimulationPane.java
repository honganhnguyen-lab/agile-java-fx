package com.groupesan.project.java.scrumsimulator.mainpackage.ui.panels;

import com.groupesan.project.java.scrumsimulator.mainpackage.state.SimulationManager;
import com.groupesan.project.java.scrumsimulator.mainpackage.ui.widgets.BaseComponent;
import com.groupesan.project.java.scrumsimulator.mainpackage.utils.CustomConstraints;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ModifySimulationPane extends JFrame implements BaseComponent {

    private SimulationManager simulationManager;
    private JTextField simulationNameField;
    private JTextField numberOfSprintsField;
    private JTextField durationOfSprintField;
    private JTextField userIdField;

    public ModifySimulationPane(SimulationManager manager) {
        this.simulationManager = manager;
        this.init();
    }

    /** Initializes the UI components of the ModifySimulationPane. */
    @Override
    public void init() {
        ObjectMapper objectMapper = new ObjectMapper();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Modify Simulation");
        setSize(400, 300);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        simulationNameField = new JTextField(20);
        numberOfSprintsField = new JTextField(20);
        durationOfSprintField = new JTextField(20);
        userIdField = new JTextField(20); // New field for User ID

        try {
            Map<String, String> userRoleMap = objectMapper.readValue(
                    new File("Current_User.json"), new TypeReference<Map<String, String>>() {
                    });
            String name = userRoleMap.get("current_user");
            if (!"Scrum Master".equals(name)) {
                numberOfSprintsField.setEditable(false);
                durationOfSprintField.setEditable(false);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        JLabel nameLabel = new JLabel("Simulation Name:");
        JLabel sprintsLabel = new JLabel("Number of Sprints:");
        JLabel durationOfSprintLabel = new JLabel("Duration of Sprint (in weeks):");
        JLabel userIdLabel = new JLabel("User ID:"); // Label for User ID

        panel.add(nameLabel,
                new CustomConstraints(0, 0, GridBagConstraints.WEST, 1.0, 1.0,
                        GridBagConstraints.HORIZONTAL));
        panel.add(simulationNameField,
                new CustomConstraints(1, 0, GridBagConstraints.WEST, 1.0, 1.0,
                        GridBagConstraints.HORIZONTAL));

        panel.add(sprintsLabel,
                new CustomConstraints(0, 1, GridBagConstraints.WEST, 1.0, 1.0,
                        GridBagConstraints.HORIZONTAL));
        panel.add(numberOfSprintsField,
                new CustomConstraints(1, 1, GridBagConstraints.WEST, 1.0, 1.0,
                        GridBagConstraints.HORIZONTAL));

        panel.add(durationOfSprintLabel,
                new CustomConstraints(0, 2, GridBagConstraints.WEST, 1.0, 1.0,
                        GridBagConstraints.HORIZONTAL));
        panel.add(durationOfSprintField,
                new CustomConstraints(1, 2, GridBagConstraints.WEST, 1.0, 1.0,
                        GridBagConstraints.HORIZONTAL));

        panel.add(userIdLabel,
                new CustomConstraints(0, 3, GridBagConstraints.WEST, 1.0, 1.0,
                        GridBagConstraints.HORIZONTAL));
        panel.add(userIdField,
                new CustomConstraints(1, 3, GridBagConstraints.WEST, 1.0, 1.0,
                        GridBagConstraints.HORIZONTAL));

        JButton submitButton = new JButton("Modify Simulation");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String simulationName = simulationNameField.getText().trim();
                String numberOfSprints = numberOfSprintsField.getText().trim();
                String durationOfSprint = durationOfSprintField.getText().trim();
                String userId = userIdField.getText().trim(); // Get user ID from input

                // Attempt to modify the simulation using the SimulationManager
                boolean success = simulationManager.modifySimulation(simulationName, numberOfSprints,
                        durationOfSprint,
                        userId);

                if (success) {
                    JOptionPane.showMessageDialog(null, "Simulation modified successfully!");
                } else {
                    JOptionPane.showMessageDialog(null,
                            "Simulation name not found or User ID does not exist, or the user is not a scrum master.");
                }
            }
        });

        panel.add(submitButton,
                new CustomConstraints(0, 4, GridBagConstraints.CENTER, 2, 1,
                        GridBagConstraints.HORIZONTAL));

        add(panel);
    }
}
