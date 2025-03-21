package com.groupesan.project.java.scrumsimulator.mainpackage.ui.panels;

import com.groupesan.project.java.scrumsimulator.mainpackage.ui.widgets.BaseComponent;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import javax.swing.*;

/**
 * RoleSelectionPane provides a user interface for users to select their role in
 * the simulation. It
 * supports the selection of roles like 'Pig', 'Product Owner', and 'Scrum
 * Master'.
 */
public class RoleSelectionPane extends JFrame implements BaseComponent {
    private Consumer<String> onRoleSelected;

    /**
     * Constructor for RoleSelectionPane.
     *
     * @param onRoleSelected A Consumer functional interface to handle the selected
     *                       role.
     */
    public RoleSelectionPane(Consumer<String> onRoleSelected) {
        this.onRoleSelected = onRoleSelected;
        setTitle("Select Role");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        init();
    }

    /**
     * Initializes the UI components for role selection. Adds buttons for each role
     * and sets up
     * action listeners.
     */
    @Override
    public void init() {
        setLayout(new FlowLayout());

        JButton pigButton = new JButton("Pig");
        JButton productOwnerButton = new JButton("Product Owner");
        JButton scrumMasterButton = new JButton("Scrum Master");
        JButton scrumAdministratorButton = new JButton("Scrum Administrator");

        pigButton.addActionListener(e -> selectRole("Pig"));
        productOwnerButton.addActionListener(e -> selectRole("Product Owner"));
        scrumMasterButton.addActionListener(e -> selectRole("Scrum Master"));
        scrumAdministratorButton.addActionListener(e -> selectRole("Scrum Administrator"));

        add(pigButton);
        add(productOwnerButton);
        add(scrumMasterButton);
        add(scrumAdministratorButton);
    }

    /**
     * Handles the role selection and notifies the SimulationUI.
     *
     * @param role The role selected by the user.
     */
    private void selectRole(String role) {
        ObjectMapper objectMapper = new ObjectMapper();
        if (onRoleSelected != null) {
            onRoleSelected.accept(role);
            try {
                Map<String, String> userRoleMap = new HashMap<>();
                userRoleMap.put("current_user", role);

                objectMapper.writeValue(new File("Current_User.json"), userRoleMap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.dispose();
    }
}
