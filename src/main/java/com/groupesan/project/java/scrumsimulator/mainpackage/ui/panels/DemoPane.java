package com.groupesan.project.java.scrumsimulator.mainpackage.ui.panels;

import com.groupesan.project.java.scrumsimulator.mainpackage.core.Player;
import com.groupesan.project.java.scrumsimulator.mainpackage.core.ScrumRole;
import com.groupesan.project.java.scrumsimulator.mainpackage.impl.BlockerStore;
import com.groupesan.project.java.scrumsimulator.mainpackage.impl.DeleteUser;
import com.groupesan.project.java.scrumsimulator.mainpackage.state.SimulationManager;
import com.groupesan.project.java.scrumsimulator.mainpackage.ui.utils.WizardManager;
import com.groupesan.project.java.scrumsimulator.mainpackage.ui.widgets.BaseComponent;
import com.groupesan.project.java.scrumsimulator.mainpackage.utils.CustomConstraints;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DemoPane extends JFrame implements BaseComponent {
    private Player player = new Player("bob", new ScrumRole("demo"));
    private BlockerStore blockerStore = new BlockerStore();

    private SimulationPanel simulationPanel;

    public DemoPane() {
        this.init();
        player.doRegister();
    }

    /**
     * Initialization of Demo Pane. Demonstrates creation of User stories, Sprints,
     * and Simulation
     * start.
     */
    public void init() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Demo");
        setSize(1920, 1080);

        GridBagLayout myGridbagLayout = new GridBagLayout();
        JPanel myJpanel = new JPanel();
        myJpanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        myJpanel.setLayout(myGridbagLayout);

        // Existing buttons
        JButton sprintsButton = new JButton("Sprints");
        sprintsButton.addActionListener(e -> {
            SprintListPane form = new SprintListPane();
            form.setVisible(true);
        });
        myJpanel.add(sprintsButton,
                new CustomConstraints(0, 0, GridBagConstraints.WEST, 1.0, 1.0, GridBagConstraints.HORIZONTAL));

        JButton spikesButton = new JButton("View Spikes");
        spikesButton.addActionListener(e -> {
            SpikeListPane form = new SpikeListPane(false);
            form.setVisible(true);
        });
        myJpanel.add(spikesButton,
                new CustomConstraints(1, 1, GridBagConstraints.WEST, 1.0, 1.0, GridBagConstraints.HORIZONTAL));

        JButton manageSpikesButton = new JButton("Manage Spikes");
        manageSpikesButton.addActionListener(e -> {
            SpikeListPane form = new SpikeListPane(true);
            form.setVisible(true);
        });
        myJpanel.add(manageSpikesButton,
                new CustomConstraints(2, 1, GridBagConstraints.WEST, 1.0, 1.0, GridBagConstraints.HORIZONTAL));

        JButton userStoriesButton = new JButton("Product Backlog");
        userStoriesButton.addActionListener(e -> {
            UserStoryListPane form = new UserStoryListPane();
            form.setVisible(true);
        });
        myJpanel.add(userStoriesButton,
                new CustomConstraints(1, 0, GridBagConstraints.WEST, 1.0, 1.0, GridBagConstraints.HORIZONTAL));

        JButton createNewSpikeButton = new JButton("Create New Spike");
        createNewSpikeButton.addActionListener(e -> {
            NewSpikeForm form = new NewSpikeForm();
            form.setVisible(true);
        });
        myJpanel.add(createNewSpikeButton,
                new CustomConstraints(0, 1, GridBagConstraints.WEST, 1.0, 1.0, GridBagConstraints.HORIZONTAL));

        JButton blockerButton = new JButton("Blockers");
        blockerButton.addActionListener(e -> {
            BlockerManagementPane blockerPane = new BlockerManagementPane(blockerStore);
            blockerPane.setVisible(true);
        });
        myJpanel.add(blockerButton,
                new CustomConstraints(3, 1, GridBagConstraints.WEST, 1.0, 1.0, GridBagConstraints.HORIZONTAL));

        JButton solutionManagementButton = new JButton("Solutions");
        solutionManagementButton.addActionListener(e -> {
            SolutionPane solutionPane = new SolutionPane();
            solutionPane.setVisible(true);
        });
        myJpanel.add(solutionManagementButton,
                new CustomConstraints(4, 1, GridBagConstraints.WEST, 1.0, 1.0, GridBagConstraints.HORIZONTAL));

        JButton simulationButton = new JButton("Simulation");
        simulationButton.addActionListener(e -> {
            if (simulationPanel == null || !simulationPanel.isVisible()) {
                simulationPanel = new SimulationPanel();
                simulationPanel.setVisible(true);
            } else {
                simulationPanel.toFront();
            }
        });

        myJpanel.add(simulationButton,
                new CustomConstraints(5, 1, GridBagConstraints.WEST, 1.0, 1.0, GridBagConstraints.HORIZONTAL));

        JButton assignBlockersButton = new JButton("Assign Blockers to Sprint");
        assignBlockersButton.addActionListener(e -> {
            SprintBlockerAssignmentPane assignmentPane = new SprintBlockerAssignmentPane(blockerStore);
            assignmentPane.setVisible(true);
        });

        myJpanel.add(assignBlockersButton,
                new CustomConstraints(0, 2, GridBagConstraints.WEST, 1.0, 1.0, GridBagConstraints.HORIZONTAL));

        JButton updateStoryStatusButton = new JButton("Update User Story Status");
        updateStoryStatusButton.addActionListener(e -> {
            UpdateUserStoryPanel form = new UpdateUserStoryPanel();
            form.setVisible(true);
        });
        myJpanel.add(updateStoryStatusButton,
                new CustomConstraints(2, 0, GridBagConstraints.WEST, 1.0, 1.0, GridBagConstraints.HORIZONTAL));

        JButton deleteUserStoryButton = new JButton("Delete User Story");
        deleteUserStoryButton.addActionListener(e -> {
            DeleteUserStory form = new DeleteUserStory();
            form.setVisible(true);
        });

        myJpanel.add(deleteUserStoryButton,
                new CustomConstraints(6, 1, GridBagConstraints.WEST, 1.0, 1.0, GridBagConstraints.HORIZONTAL));

        // Add User button
        JButton addUserButton = new JButton("Add User");
        addUserButton.addActionListener(e -> {
            SimulationPane simulationPane = new SimulationPane();
            simulationPane.setVisible(true);
        });
        myJpanel.add(addUserButton,
                new CustomConstraints(3, 0, GridBagConstraints.WEST, 1.0, 1.0, GridBagConstraints.HORIZONTAL));

        // Delete User button

        // Inside your DemoPane class

        JButton deleteUserButton = new JButton("Delete User");
        deleteUserButton.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // Prompt the user for confirmation
                        String username = JOptionPane.showInputDialog("Enter the username to delete:");
                        if (username != null && !username.trim().isEmpty()) {
                            int confirmation = JOptionPane.showConfirmDialog(
                                    null,
                                    "Are you sure you want to delete user: " + username + "?",
                                    "Delete Confirmation",
                                    JOptionPane.YES_NO_OPTION);

                            // If the user confirms deletion (clicks 'Yes')
                            if (confirmation == JOptionPane.YES_OPTION) {
                                boolean isDeleted = DeleteUser.deleteUser(username);
                                if (isDeleted) {
                                    JOptionPane.showMessageDialog(null, "User " + username + " deleted successfully.");
                                } else {
                                    JOptionPane.showMessageDialog(null,
                                            "User " + username + " not found or could not be deleted.",
                                            "Error", JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Username cannot be empty.",
                                    "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                });

        JButton newSimulationButton = new JButton("New Simulation");
        newSimulationButton.addActionListener(e -> {
            WizardManager.get().showSimulationWizard();
        });

        myJpanel.add(newSimulationButton,
                new CustomConstraints(5, 0, GridBagConstraints.WEST, 1.0, 1.0, GridBagConstraints.HORIZONTAL));

        JButton backlogManagementButton = new JButton("Backlog Management");
        backlogManagementButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open the Backlog Management Pane
                BacklogManagementPane backlogPane = new BacklogManagementPane();
                backlogPane.setVisible(true);
            }
        });

        myJpanel.add(backlogManagementButton,
                new CustomConstraints(7, 1, GridBagConstraints.WEST, 1.0, 1.0, GridBagConstraints.HORIZONTAL));

        myJpanel.add(
                deleteUserButton,
                new CustomConstraints(
                        4, 0, GridBagConstraints.WEST, 1.0, 1.0, GridBagConstraints.HORIZONTAL));

        JButton modifySimulationButton = new JButton("Modify Simulation");
        modifySimulationButton.addActionListener(e -> {
            SimulationManager simulationManager = new SimulationManager();
            ModifySimulationPane modifySimulationPane = new ModifySimulationPane(simulationManager);
            modifySimulationPane.setVisible(true);
        });
        myJpanel.add(modifySimulationButton,
                new CustomConstraints(6, 0, GridBagConstraints.WEST, 1.0, 1.0, GridBagConstraints.HORIZONTAL));

        JButton joinSimulationButton = new JButton("Join Simulation");
        joinSimulationButton.addActionListener(e -> {
            SimulationUI simulationUserInterface = new SimulationUI();
            simulationUserInterface.setVisible(true);
        });
        myJpanel.add(joinSimulationButton,
                new CustomConstraints(7, 0, GridBagConstraints.WEST, 1.0, 1.0, GridBagConstraints.HORIZONTAL));

        JButton simulationSwitchRoleButton = new JButton("Switch Role");
        simulationSwitchRoleButton.addActionListener(e -> {
            SimulationSwitchRolePane feedbackPanelUI = new SimulationSwitchRolePane();
            feedbackPanelUI.setVisible(true);
        });
        myJpanel.add(simulationSwitchRoleButton,
                new CustomConstraints(8, 1, GridBagConstraints.WEST, 1.0, 1.0, GridBagConstraints.HORIZONTAL));

        JButton variantSimulationUIButton = new JButton("Variant Simulation UI");
        variantSimulationUIButton.addActionListener(e -> {
            VariantSimulationUI variantSimulationUI = new VariantSimulationUI();
            variantSimulationUI.setVisible(true);
        });
        myJpanel.add(variantSimulationUIButton,
                new CustomConstraints(8, 0, GridBagConstraints.WEST, 1.0, 1.0, GridBagConstraints.HORIZONTAL));
        JButton resolveBlockerButton = new JButton("Resolve Blocker");
        resolveBlockerButton.addActionListener(e -> {
            ResolvePane resolvePane = new ResolvePane();
            resolvePane.setVisible(true);
            resolvePane.requestFocus();
        });



        myJpanel.add(resolveBlockerButton,
                new CustomConstraints(1, 2, GridBagConstraints.WEST, 1.0, 1.0, GridBagConstraints.HORIZONTAL));
        JButton SprintUIButton = new JButton("US Selection UI");
        SprintUIButton.addActionListener(e -> {
            SprintUIPane sprintUIPane = new SprintUIPane(player);
            sprintUIPane.setVisible(true);
        });
        myJpanel.add(SprintUIButton,
                new CustomConstraints(7, 1, GridBagConstraints.WEST, 1.0, 1.0, GridBagConstraints.HORIZONTAL));

        add(myJpanel);
    }
}