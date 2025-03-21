package com.groupesan.project.java.scrumsimulator.mainpackage.ui.panels;

import com.groupesan.project.java.scrumsimulator.mainpackage.impl.Sprint;
import com.groupesan.project.java.scrumsimulator.mainpackage.impl.SprintStore;
import com.groupesan.project.java.scrumsimulator.mainpackage.impl.UserStory;
import com.groupesan.project.java.scrumsimulator.mainpackage.impl.UserStoryStore;
import com.groupesan.project.java.scrumsimulator.mainpackage.ui.widgets.BaseComponent;
import com.groupesan.project.java.scrumsimulator.mainpackage.ui.widgets.UserStoryWidget;
import com.groupesan.project.java.scrumsimulator.mainpackage.utils.CustomConstraints;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserStoryListPane extends JFrame implements BaseComponent {
    private JComboBox<Sprint> sprintDropdown;
    private JPanel userStoryPanel;
    private List<UserStoryWidget> widgets = new ArrayList<>();
    private SprintStore sprintStore;

    public UserStoryListPane() {
        this.sprintStore = SprintStore.getInstance();
        this.init();
    }

    public void init() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("User Stories by Sprint");
        setSize(400, 300);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));


        sprintDropdown = new JComboBox<>();
        loadSprints();
        sprintDropdown.addActionListener(e -> loadUserStoriesForSprint());


        JPanel dropdownPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        dropdownPanel.add(new JLabel("Select Sprint:"));
        dropdownPanel.add(sprintDropdown);
        mainPanel.add(dropdownPanel, BorderLayout.NORTH);


        userStoryPanel = new JPanel(new GridBagLayout());
        JScrollPane scrollPane = new JScrollPane(userStoryPanel);
        mainPanel.add(scrollPane, BorderLayout.CENTER);


        JButton newUserStoryButton = new JButton("New User Story");
        newUserStoryButton.addActionListener(e -> openNewUserStoryForm());
        mainPanel.add(newUserStoryButton, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void loadSprints() {
        sprintDropdown.removeAllItems();
        for (Sprint sprint : sprintStore.getSprints()) {
            sprintDropdown.addItem(sprint);
        }
    }

    private void loadUserStoriesForSprint() {
        Sprint selectedSprint = (Sprint) sprintDropdown.getSelectedItem();
        if (selectedSprint != null) {
            userStoryPanel.removeAll();
            widgets.clear();

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.anchor = GridBagConstraints.WEST;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 1.0;

            int i = 0;
            for (UserStory userStory : selectedSprint.getUserStories()) {
                UserStoryWidget widget = new UserStoryWidget(userStory);
                widgets.add(widget);
                gbc.gridy = i++;
                userStoryPanel.add(widget, gbc);
            }

            userStoryPanel.revalidate();
            userStoryPanel.repaint();
        }
    }

    private void openNewUserStoryForm() {
        NewUserStoryForm form = new NewUserStoryForm();
        form.setVisible(true);

        form.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                UserStory userStory = form.getUserStoryObject();
                UserStoryStore.getInstance().addUserStory(userStory);
                Sprint selectedSprint = (Sprint) sprintDropdown.getSelectedItem();
                if (selectedSprint != null) {
                    selectedSprint.addUserStory(userStory);
                    loadUserStoriesForSprint();
                }
            }
        });
    }
}
