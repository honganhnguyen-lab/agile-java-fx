package com.groupesan.project.java.scrumsimulator.mainpackage.ui.panels;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SimulationSwitchRolePane extends JFrame {

    private JRadioButton developerRadioButton;
    private JRadioButton scrumMasterRadioButton;
    private JRadioButton productOwnerRadioButton;
    private JRadioButton ScrumAdministratorRadioButton;
    private ButtonGroup roleButtonGroup;
    private JButton switchButton;
    private String userRole;

    public SimulationSwitchRolePane() {
        setTitle("Simulation Status");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 1));

        JLabel label = new JLabel("Roles:");
        panel.add(label);

        developerRadioButton = new JRadioButton("Developer");
        scrumMasterRadioButton = new JRadioButton("Scrum Master");
        productOwnerRadioButton = new JRadioButton("Product Owner");
        ScrumAdministratorRadioButton = new JRadioButton("Scrum Administrator");
        roleButtonGroup = new ButtonGroup();
        roleButtonGroup.add(developerRadioButton);
        roleButtonGroup.add(scrumMasterRadioButton);
        roleButtonGroup.add(productOwnerRadioButton);
        roleButtonGroup.add(ScrumAdministratorRadioButton);
        panel.add(developerRadioButton);
        panel.add(scrumMasterRadioButton);
        panel.add(productOwnerRadioButton);
        panel.add(ScrumAdministratorRadioButton);

        switchButton = new JButton("Switch Role");
        switchButton.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // Logic for join button
                        onSwitchButtonClicked();
                    }
                });

        setLayout(new BorderLayout());
        add(switchButton, BorderLayout.SOUTH);
        add(panel);
    }

    private void onSwitchButtonClicked() {
        ObjectMapper objectMapper = new ObjectMapper();
        if (developerRadioButton.isSelected()) {
            userRole = "Developer";
            JOptionPane.showMessageDialog(
                    null, "Switched to Developer", "Role Switching", JOptionPane.PLAIN_MESSAGE);
        } else if (scrumMasterRadioButton.isSelected()) {
            userRole = "Scrum Master";
            JOptionPane.showMessageDialog(
                    null, "Switched to Scrum Master", "Role Switching", JOptionPane.PLAIN_MESSAGE);
        } else if (productOwnerRadioButton.isSelected()) {
            userRole = "Product Owner";
            JOptionPane.showMessageDialog(
                    null, "Switched to Product Owner", "Role Switching", JOptionPane.PLAIN_MESSAGE);
        } else if (ScrumAdministratorRadioButton.isSelected()) {
            userRole = "Scrum Administrator";
            JOptionPane.showMessageDialog(
                    null, "Switched to Scrum Administrator", "Role Switching", JOptionPane.PLAIN_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(
                    null,
                    "Failed to Switch Role",
                    "Role Switching Error",
                    JOptionPane.ERROR_MESSAGE);
        }

        try {
            Map<String, String> userRoleMap = new HashMap<>();
            userRoleMap.put("current_user", userRole);
            objectMapper.writeValue(new File("Current_User.json"), userRoleMap);
        } catch (IOException e) {
            e.printStackTrace();
        }

        roleButtonGroup.clearSelection();
        dispose();
        return;
    }
}
