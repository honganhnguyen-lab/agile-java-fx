package com.groupesan.project.java.scrumsimulator.mainpackage.ui.panels;

import com.fasterxml.jackson.core.type.TypeReference;
import com.groupesan.project.java.scrumsimulator.mainpackage.impl.Blocker1;
import com.groupesan.project.java.scrumsimulator.mainpackage.impl.BlockerStore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.groupesan.project.java.scrumsimulator.mainpackage.state.SimulationStateManager;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public class UpdateBlockerForm extends JDialog {
    private final String blkId;
    private final BlockerStore blkrSt;
    private final BlockerManagementPane blkMngPane;
    private JTextField blkName;
    private JTextArea descArea;
    private JSlider probabilityRange;

    public UpdateBlockerForm(String blkId, BlockerManagementPane blkMngPane, BlockerStore blkrSt) {

        super((JFrame) null, "Update Blocker", true);
        this.blkId = blkId;
        this.blkMngPane = blkMngPane;
        this.blkrSt = blkrSt;

        SimulationStateManager simulationStateManager = SimulationStateManager.getInstance();

        Blocker1 blocker = blkrSt.getBlKById(blkId);
        if (blocker == null) {
            JOptionPane.showMessageDialog(this, "Blocker not found.", "There's an Error", JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }

        setTitle("Update Blocker");
        setSize(600, 450);
        setLocationRelativeTo(blkMngPane);
        setLayout(new BorderLayout());

        JPanel inP = new JPanel(new GridLayout(4, 1, 10, 10));
        inP.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        inP.add(new JLabel("Blocker Name:"));
        blkName = new JTextField(blocker.getBName());
        inP.add(blkName);

        inP.add(new JLabel("Description:"));
        descArea = new JTextArea(blocker.getDesc(), 4, 20);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        inP.add(new JScrollPane(descArea));

        ObjectMapper objectMapper = new ObjectMapper();
        inP.add(new JLabel("Probability:"));
        probabilityRange = new JSlider(0, 100);
        probabilityRange.setMajorTickSpacing(10);
        probabilityRange.setPaintTicks(true);
        probabilityRange.setPaintLabels(true);
        try {
            Map<String, String> userRoleMap = objectMapper.readValue(
                    new File("Current_User.json"), new TypeReference<Map<String, String>>() {
                    });
            String name = userRoleMap.get("current_user");
            boolean isAdminOrMaster = "Scrum Master".equals(name) || "Scrum Administrator".equals(name);
            boolean isRunning = simulationStateManager.isRunning();

            if (isAdminOrMaster && isRunning) {
                probabilityRange.setEnabled(false);
                JOptionPane.showMessageDialog(this,
                        "Scrum Administrators or Scrum Masters cannot change probabilities during an active simulation.",
                        "Permission Denied", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        inP.add(probabilityRange);

        add(inP, BorderLayout.CENTER);

        JPanel btnPnl = new JPanel(new FlowLayout());
        JButton updtBtn = new JButton("Update");
        updtBtn.addActionListener(e -> updateBlocker());
        btnPnl.add(updtBtn);

        add(btnPnl, BorderLayout.SOUTH);
    }

    private void updateBlocker() {
        String blockerName = blkName.getText().trim();
        String description = descArea.getText().trim();
        Integer probability = probabilityRange.getValue();

        if (!blockerName.trim().isEmpty() && !description.trim().isEmpty()) {
            Blocker1 blocker = blkrSt.getBlKById(blkId);

            if (blocker != null) {
                blocker.setBName(blockerName);
                blocker.setDesc(description);
                blocker.setProbability(probability);
                blkrSt.updateBlk(blocker);
                JOptionPane.showMessageDialog(this, "Awesome! The blocker has been updated successfully.");
                blkMngPane.loadBlockers();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Couldn’t find the blocker you’re trying to update", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "Some fields are empty. Please make sure to fill in both the name and description before updating.",
                    "Input Error", JOptionPane.ERROR_MESSAGE);
        }

    }
}
