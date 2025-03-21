package com.groupesan.project.java.scrumsimulator.mainpackage.ui.panels;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.groupesan.project.java.scrumsimulator.mainpackage.impl.Blocker1;
import com.groupesan.project.java.scrumsimulator.mainpackage.impl.Sprint;
import com.groupesan.project.java.scrumsimulator.mainpackage.impl.SprintStore;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ResolvePane extends JFrame {
    private JComboBox<Sprint> sprintDropdown;
    private DefaultTableModel resolutionTableModel;
    private JTable resolutionTable;
    private JTextField outcomeTextField;
    private JButton resolveButton;
    private JButton applyButton;
    private List<Blocker1> blockers = new ArrayList<>();
    private List<String> availableSolutions = new ArrayList<>();
    private static final String SP_BLOCKER_PATH = "src/main/resources/sprint_blockers.json";
    private static final String SOLUTION_PATH = "src/main/resources/solution.json";

    public ResolvePane() {
        setTitle("Resolve Blockers");
        setSize(800, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridBagLayout());
        setLocationRelativeTo(null);
        GridBagConstraints gbc = new GridBagConstraints();


        sprintDropdown = new JComboBox<>();
        populateSprintDropdown();
        sprintDropdown.addActionListener(e -> loadBlockersForSelectedSprint());


        resolutionTableModel = new DefaultTableModel(new Object[]{"Blocker ID", "Name", "Selected Solution", "Outcome"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3;
            }
        };
        resolutionTable = new JTable(resolutionTableModel);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        resolutionTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        resolutionTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        resolutionTable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        resolutionTable.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);


        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;


        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(sprintDropdown, gbc);


        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weighty = 1.0;
        add(new JScrollPane(resolutionTable), gbc);


        outcomeTextField = new JTextField();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.weighty = 0;
        add(outcomeTextField, gbc);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        resolveButton = new JButton("Resolve");
        applyButton = new JButton("Apply");
        applyButton.setEnabled(false);

        resolveButton.addActionListener(e -> resolveBlocker());
        applyButton.addActionListener(e -> applySolution());

        buttonPanel.add(resolveButton);
        buttonPanel.add(applyButton);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.weighty = 0;
        add(buttonPanel, gbc);


        resolutionTable.getColumnModel().getColumn(0).setPreferredWidth(100);
        resolutionTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        resolutionTable.getColumnModel().getColumn(2).setPreferredWidth(150);
        resolutionTable.getColumnModel().getColumn(3).setPreferredWidth(150);
    }

    private void populateSprintDropdown() {
        List<Sprint> sprints = SprintStore.getInstance().getSprints();
        for (Sprint sprint : sprints) {
            sprintDropdown.addItem(sprint);
        }
        Sprint activeSprint = SprintStore.getInstance().getActiveSprint();
        if (activeSprint != null) {
            sprintDropdown.setSelectedItem(activeSprint);
        }
    }

    private void loadBlockersForSelectedSprint() {
        Sprint selectedSprint = (Sprint) sprintDropdown.getSelectedItem();
        if (selectedSprint != null) {
            loadBlockersFromJson(selectedSprint.getId());
        }
    }

    private void loadBlockersFromJson(long sprintId) {
        resolutionTableModel.setRowCount(0);
        blockers.clear();
        File file = new File(SP_BLOCKER_PATH);
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            JsonNode rootNode = objectMapper.readTree(file);
            JsonNode spIdNode = rootNode.path("sprintId");
            if (spIdNode.asLong() == sprintId) {
                JsonNode blockersNode = rootNode.path("blockers");
                for (JsonNode blockerNode : blockersNode) {
                    String id = blockerNode.path("id").asText();
                    String name = blockerNode.path("name").asText();
                    String description = blockerNode.path("description").asText();
                    blockers.add(new Blocker1(id, name, description, 0));


                    resolutionTableModel.addRow(new Object[]{id, name, "", ""});
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error reading blockers from file.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void resolveBlocker() {
        int selectedRow = resolutionTable.getSelectedRow();
        if (selectedRow != -1) {
            String selectedBlockerId = (String) resolutionTableModel.getValueAt(selectedRow, 0);
            availableSolutions = getAvailableSolutions(selectedBlockerId);

            if (!availableSolutions.isEmpty()) {
                String selectedSolution = (String) JOptionPane.showInputDialog(this,
                        "Select a solution for blocker " + selectedBlockerId + ":",
                        "Select Solution", JOptionPane.QUESTION_MESSAGE, null, availableSolutions.toArray(), availableSolutions.get(0));

                if (selectedSolution != null) {

                    resolutionTableModel.setValueAt(selectedSolution, selectedRow, 2);
                    applyButton.setEnabled(true);
                }
            } else {
                JOptionPane.showMessageDialog(this, "No solutions available for blocker: " + selectedBlockerId,
                        "No Solutions", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Select a blocker to resolve.", "Error!", JOptionPane.WARNING_MESSAGE);
        }
    }

    private List<String> getAvailableSolutions(String blockerId) {
        List<String> solutions = new ArrayList<>();
        File file = new File(SOLUTION_PATH);
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            JsonNode rootNode = objectMapper.readTree(file);
            JsonNode solutionsNode = rootNode.path("Solutions");
            for (JsonNode solutionNode : solutionsNode) {
                if (solutionNode.path("blockerId").asText().equals(blockerId)) {
                    String solutionName = solutionNode.path("sname").asText();
                    solutions.add(solutionName);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error reading solutions from file.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return solutions;
    }

    private void applySolution() {
        int selectedRow = resolutionTable.getSelectedRow();
        if (selectedRow != -1) {
            String outcome = outcomeTextField.getText().trim();
            if (!outcome.isEmpty()) {

                resolutionTableModel.setValueAt(outcome, selectedRow, 3);


                for (int i = 0; i < resolutionTableModel.getRowCount(); i++) {
                    if (i != selectedRow) {
                        resolutionTableModel.setValueAt("", i, 2);
                        resolutionTableModel.setValueAt("", i, 3);
                    }
                }

                applyButton.setEnabled(false);
                JOptionPane.showMessageDialog(this, "Solution applied and outcome recorded.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Please enter an outcome.", "No Outcome", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Select a blocker to apply a solution.", "Error!", JOptionPane.WARNING_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ResolvePane resolvePane = new ResolvePane();
            resolvePane.setVisible(true);
        });
    }
}
