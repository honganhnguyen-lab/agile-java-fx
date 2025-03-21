package com.groupesan.project.java.scrumsimulator.mainpackage.ui.panels;

import com.groupesan.project.java.scrumsimulator.mainpackage.impl.Sprint;
import com.groupesan.project.java.scrumsimulator.mainpackage.impl.SprintStore;
import com.groupesan.project.java.scrumsimulator.mainpackage.impl.UserStory;
import com.groupesan.project.java.scrumsimulator.mainpackage.impl.UserStoryStore;
import com.groupesan.project.java.scrumsimulator.mainpackage.utils.CustomConstraints;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class BacklogManagementPane extends JFrame {
    private JComboBox<Sprint> sprintDropdown;
    private HashMap<UserStory, JCheckBox> productBacklogCheckboxes = new HashMap<>();
    private HashMap<UserStory, JCheckBox> sprintBacklogCheckboxes = new HashMap<>();
    private JPanel productBacklogPanel;
    private JPanel sprintBacklogPanel;
    private SprintStore sprintStore;

    public BacklogManagementPane() {
        sprintStore = SprintStore.getInstance();
        initUI();
    }

    private void initUI() {
        setTitle("Backlog Management");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        sprintDropdown = new JComboBox<>();
        loadSprints();
        sprintDropdown.addActionListener(e -> loadSprintBacklog());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.5;
        gbc.weighty = 0.1;

        mainPanel.add(new JLabel("Select Sprint:"), gbc);

        gbc.gridx = 1;
        mainPanel.add(sprintDropdown, gbc);

        productBacklogPanel = new JPanel(new GridBagLayout());
        populateProductBacklog();

        sprintBacklogPanel = new JPanel(new GridBagLayout());

        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(new JLabel("Product Backlog"), gbc);

        gbc.gridx = 1;
        mainPanel.add(new JLabel("Sprint Backlog"), gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weighty = 0.8;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(new JScrollPane(productBacklogPanel), gbc);

        gbc.gridx = 1;
        mainPanel.add(new JScrollPane(sprintBacklogPanel), gbc);

        JButton moveToSprintButton = new JButton("Add to Sprint Backlog");
        moveToSprintButton.addActionListener(e -> moveSelectedToSprintBacklog());

        JButton moveToProductButton = new JButton("Remove from Sprint Backlog");
        moveToProductButton.addActionListener(e -> moveSelectedToProductBacklog());

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weighty = 0.1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(moveToSprintButton, gbc);

        gbc.gridx = 1;
        mainPanel.add(moveToProductButton, gbc);

        add(mainPanel);
    }

    private void loadSprints() {
        sprintDropdown.removeAllItems();
        for (Sprint sprint : sprintStore.getSprints()) {
            sprintDropdown.addItem(sprint);
        }
    }

    private void populateProductBacklog() {
        productBacklogPanel.removeAll();
        productBacklogCheckboxes.clear();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        int i = 0;
        for (UserStory userStory : UserStoryStore.getInstance().getUserStories()) {
            if (!userStory.isAssignedToSprint()) {  // Only add unassigned stories to Product Backlog
                JCheckBox checkbox = new JCheckBox(userStory.getName());
                productBacklogCheckboxes.put(userStory, checkbox);
                gbc.gridy = i++;
                productBacklogPanel.add(checkbox, gbc);
            }
        }
        productBacklogPanel.revalidate();
        productBacklogPanel.repaint();
    }

    private void loadSprintBacklog() {
        Sprint selectedSprint = (Sprint) sprintDropdown.getSelectedItem();
        sprintBacklogPanel.removeAll();
        sprintBacklogCheckboxes.clear();

        if (selectedSprint != null) {
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.anchor = GridBagConstraints.WEST;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 1.0;

            int i = 0;
            for (UserStory userStory : selectedSprint.getUserStories()) {
                JCheckBox checkbox = new JCheckBox(userStory.getName());
                sprintBacklogCheckboxes.put(userStory, checkbox);
                gbc.gridy = i++;
                sprintBacklogPanel.add(checkbox, gbc);
            }
        }
        sprintBacklogPanel.revalidate();
        sprintBacklogPanel.repaint();
    }

    private void moveSelectedToSprintBacklog() {
        Sprint selectedSprint = (Sprint) sprintDropdown.getSelectedItem();
        if (selectedSprint != null) {
            for (UserStory userStory : new ArrayList<>(productBacklogCheckboxes.keySet())) {
                JCheckBox checkbox = productBacklogCheckboxes.get(userStory);
                if (checkbox.isSelected() && !sprintBacklogCheckboxes.containsKey(userStory)) {
                    // Move from Product to Sprint Backlog only if not already present
                    productBacklogPanel.remove(checkbox);
                    productBacklogCheckboxes.remove(userStory);

                    JCheckBox sprintCheckbox = new JCheckBox(userStory.getName());
                    sprintBacklogCheckboxes.put(userStory, sprintCheckbox);
                    sprintBacklogPanel.add(sprintCheckbox, new GridBagConstraints(0, sprintBacklogCheckboxes.size() - 1, 1, 1, 1.0, 0.1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
                    selectedSprint.addUserStory(userStory);
                    userStory.setAssignedToSprint(true);  // Mark as assigned
                }
            }
            refreshPanels();
        }
    }

    private void moveSelectedToProductBacklog() {
        Sprint selectedSprint = (Sprint) sprintDropdown.getSelectedItem();
        if (selectedSprint != null) {
            for (UserStory userStory : new ArrayList<>(sprintBacklogCheckboxes.keySet())) {
                JCheckBox checkbox = sprintBacklogCheckboxes.get(userStory);
                if (checkbox.isSelected() && !productBacklogCheckboxes.containsKey(userStory)) {
                    // Move from Sprint to Product Backlog only if not already present
                    sprintBacklogPanel.remove(checkbox);
                    sprintBacklogCheckboxes.remove(userStory);

                    JCheckBox productCheckbox = new JCheckBox(userStory.getName());
                    productBacklogCheckboxes.put(userStory, productCheckbox);
                    productBacklogPanel.add(productCheckbox, new GridBagConstraints(0, productBacklogCheckboxes.size() - 1, 1, 1, 1.0, 0.1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
                    selectedSprint.removeUserStory(userStory);
                    userStory.setAssignedToSprint(false);  // Mark as unassigned
                }
            }
            refreshPanels();
        }
    }

    private void refreshPanels() {
        populateProductBacklog();
        loadSprintBacklog();
    }
}
