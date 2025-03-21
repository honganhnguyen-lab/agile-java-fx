package com.groupesan.project.java.scrumsimulator.mainpackage.ui.panels;

import com.groupesan.project.java.scrumsimulator.mainpackage.impl.SolutionManager;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.groupesan.project.java.scrumsimulator.mainpackage.impl.Solution;
import com.groupesan.project.java.scrumsimulator.mainpackage.state.SimulationStateManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class SolutionPane extends JFrame {

    private JComboBox<String> blkrDropdown;
    private DefaultTableModel solnTableModel;
    private JTable solnTable;
    private SolutionManager solnManager;
    private Map<String, String> blkMp;
    private SimulationStateManager stateManager;

    public SolutionPane() {
        setTitle("Manage List of Solutions for Blockers");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        solnManager = new SolutionManager();
        blkMp = solnManager.getAllBlockers();
        stateManager = SimulationStateManager.getInstance();

        blkrDropdown = new JComboBox<>();
        loadBlockers();
        blkrDropdown.addActionListener(e -> loadSolutionsForSelectedBlocker());

        solnTableModel = new DefaultTableModel(new Object[] { "Solution", "Probability" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make all cells non-editable
            }
        };
        solnTable = new JTable(solnTableModel);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        solnTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);

        add(blkrDropdown, BorderLayout.NORTH);
        add(new JScrollPane(solnTable), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        buttonPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JButton addButton = new JButton("Add Soln");
        JButton updateButton = new JButton("Update Soln");
        JButton deleteButton = new JButton("Delete Soln");
        JButton randomizeButton = new JButton("Randomize Probabilities");

        addButton.addActionListener(e -> addSolution());
        updateButton.addActionListener(e -> updateSolution());
        deleteButton.addActionListener(e -> deleteSolution());
        randomizeButton.addActionListener(e -> randomizeProbabilities());

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(randomizeButton);

        add(buttonPanel, BorderLayout.SOUTH);

        loadSolutionsForSelectedBlocker();
    }

    private void loadBlockers() {
        blkrDropdown.removeAllItems();
        for (Map.Entry<String, String> entry : blkMp.entrySet()) {
            blkrDropdown.addItem(entry.getKey() + ": " + entry.getValue());
        }
    }

    private void loadSolutionsForSelectedBlocker() {
        solnTableModel.setRowCount(0);
        String selectedBlkr = (String) blkrDropdown.getSelectedItem();
        if (selectedBlkr != null) {
            String blkId = selectedBlkr.split(":")[0];
            List<Solution> solns = solnManager.getSolutionsForBlocker(blkId);

            for (Solution soln : solns) {
                String solnName = soln.getSname();
                String probability = soln.getProbability() + "%";
                solnTableModel.addRow(new Object[] { solnName, probability });
            }
        }
    }

    private void addSolution() {
        String blkId = ((String) blkrDropdown.getSelectedItem()).split(":")[0];

        JPanel inputPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        JTextField solnNameField = new JTextField();

        JSlider probSlider = new JSlider(0, 100, 50);
        probSlider.setMajorTickSpacing(20);
        probSlider.setMinorTickSpacing(5);
        probSlider.setPaintTicks(true);
        probSlider.setPaintLabels(true);

        JLabel probLabel = new JLabel("Probability: " + probSlider.getValue() + "%");

        probSlider.addChangeListener(e -> probLabel.setText("Probability: " + probSlider.getValue() + "%"));

        inputPanel.add(new JLabel("Enter Solution Name:"));
        inputPanel.add(solnNameField);
        inputPanel.add(probLabel);
        inputPanel.add(probSlider);

        int result = JOptionPane.showConfirmDialog(this, inputPanel,
                "Add Solution", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String solnName = solnNameField.getText().trim();
            Integer probability = probSlider.getValue();
            if (!solnName.isEmpty()) {
                if (solnManager.addSolution(solnName, blkId, probability)) {
                    loadSolutionsForSelectedBlocker();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to add solution.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Solution name cannot be empty.", "Error",
                        JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private void updateSolution() {
        int selectedRow = solnTable.getSelectedRow();
        if (selectedRow != -1) {
            String blkId = ((String) blkrDropdown.getSelectedItem()).split(":")[0];

            String selectedSolnName = (String) solnTableModel.getValueAt(selectedRow, 0);
            String selectedSolnProb = ((String) solnTableModel.getValueAt(selectedRow, 1)).replace("%", "");
            ;

            JPanel inputPanel = new JPanel(new GridLayout(0, 2, 5, 5));
            JTextField solnNameField = new JTextField(selectedSolnName);

            ObjectMapper objectMapper = new ObjectMapper();
            int initialProbability = Integer.parseInt(selectedSolnProb.trim());
            JSlider probSlider = new JSlider(0, 100, initialProbability);
            probSlider.setMajorTickSpacing(20);
            probSlider.setMinorTickSpacing(5);
            probSlider.setPaintTicks(true);
            probSlider.setPaintLabels(true);

            try {
                Map<String, String> userRoleMap = objectMapper.readValue(
                        new File("Current_User.json"), new TypeReference<Map<String, String>>() {
                        });
                String name = userRoleMap.get("current_user");
                boolean isAdminOrMaster = "Scrum Master".equals(name) || "Scrum Administrator".equals(name);
                if (isAdminOrMaster && stateManager.isRunning()) {
                    probSlider.setEnabled(false);
                    JOptionPane.showMessageDialog(this,
                            "Scrum Administrators or Scrum Masters cannot change probabilities during an active simulation.",
                            "Permission Denied", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            JLabel probLabel = new JLabel("Probability: " + probSlider.getValue() + "%");

            probSlider.addChangeListener(e -> probLabel.setText("Probability: " + probSlider.getValue() + "%"));

            inputPanel.add(new JLabel("Enter Solution Name:"));
            inputPanel.add(solnNameField);
            inputPanel.add(probLabel);
            inputPanel.add(probSlider);

            int result = JOptionPane.showConfirmDialog(this, inputPanel,
                    "Update Solution", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                String solnName = solnNameField.getText().trim();

                if (!solnName.isEmpty()) {
                    int probability = probSlider.getValue();
                    if (solnManager.updateSolution(selectedSolnName, blkId, solnName, probability)) {
                        loadSolutionsForSelectedBlocker();
                    } else {
                        JOptionPane.showMessageDialog(this, "Failed to add solution.", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Solution name cannot be empty.", "Error",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Select a solution to update.", "Error!", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void deleteSolution() {
        int selectedRow = solnTable.getSelectedRow();
        if (selectedRow != -1) {
            String selectedSolnName = (String) solnTableModel.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure that you want to delete this solution?");
            if (confirm == JOptionPane.YES_OPTION) {
                String blkId = ((String) blkrDropdown.getSelectedItem()).split(":")[0];
                boolean isDeletedFlag = solnManager.deleteSolution(selectedSolnName, blkId);
                if (isDeletedFlag) {
                    loadSolutionsForSelectedBlocker();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed! could not delete solution.", "Error!",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Select a solution to delete.", "Error!", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void randomizeProbabilities() {
        String blkId = ((String) blkrDropdown.getSelectedItem()).split(":")[0];
        if (solnManager.randomizeProbabilitiesForSolutions(blkId)) {
            loadSolutionsForSelectedBlocker();
            JOptionPane.showMessageDialog(this, "Probabilities randomized successfully.");
        } else {
            JOptionPane.showMessageDialog(this, "Failed to randomize probabilities.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }



}