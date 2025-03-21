package com.groupesan.project.java.scrumsimulator.mainpackage.ui.panels;

import com.groupesan.project.java.scrumsimulator.mainpackage.impl.Blocker1;
import com.groupesan.project.java.scrumsimulator.mainpackage.impl.BlockerStore;
import com.groupesan.project.java.scrumsimulator.mainpackage.impl.BlockerFactory;
import com.groupesan.project.java.scrumsimulator.mainpackage.ui.utils.WrappedCellRenderer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.List;

public class BlockerManagementPane extends JFrame {
    private BlockerStore blkS;
    private JTable blkTb;
    private DefaultTableModel tM;

    public BlockerManagementPane(BlockerStore blkS) {
        this.blkS = blkS;

        setTitle("Blockers");
        setSize(700, 400);  // Adjust size to fit new column
        setLayout(new BorderLayout());

        tM = new DefaultTableModel(new Object[]{"ID", "Name", "Description", "Probability"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 3;
            }
        };
        blkTb = new JTable(tM);

        loadBlockers();

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        blkTb.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);

        blkTb.getColumnModel().getColumn(1).setCellRenderer(new WrappedCellRenderer());
        blkTb.getColumnModel().getColumn(2).setCellRenderer(new WrappedCellRenderer());
        blkTb.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);  // Center-align probability

        // Adjust column widths to accommodate new field
        blkTb.getColumnModel().getColumn(0).setPreferredWidth(50);
        blkTb.getColumnModel().getColumn(1).setPreferredWidth(150);
        blkTb.getColumnModel().getColumn(2).setPreferredWidth(250);
        blkTb.getColumnModel().getColumn(3).setPreferredWidth(80);

        blkTb.setRowHeight(80);

        JScrollPane tableScrollPane = new JScrollPane(blkTb);
        add(tableScrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());

        JButton addButton = new JButton("Add Blocker");
        addButton.addActionListener(e -> openAddBlockerForm());
        buttonPanel.add(addButton);

        JButton updateButton = new JButton("Update Blocker");
        updateButton.addActionListener(e -> openUpdateBlockerForm());
        buttonPanel.add(updateButton);

        JButton deleteButton = new JButton("Delete Blocker");
        deleteButton.addActionListener(e -> deleteBlocker());
        buttonPanel.add(deleteButton);

        JButton randomizeButton = new JButton("Randomize");
        randomizeButton.addActionListener(e -> handleRandomize());
        buttonPanel.add(randomizeButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    public void loadBlockers() {
        tM.setRowCount(0);
        List<Blocker1> blockers = blkS.getBlK();
        for (Blocker1 b : blockers) {
            String probabilityDisplay = b.getProbability() + "%";
            tM.addRow(new Object[]{b.getBId(), b.getBName(), b.getDesc(), probabilityDisplay});
        }
    }

    private void handleRandomize() {
        blkS.randomizeProbabilities();
        loadBlockers();
        JOptionPane.showMessageDialog(this, "Blocker probabilities randomized.");
    }

    private void openAddBlockerForm() {
        BlockerFactory blockerFactory = new BlockerFactory(blkS);
        NewBlockerForm newBlockerForm = new NewBlockerForm(this, blockerFactory);
        newBlockerForm.setVisible(true);

        if (newBlockerForm.isBlockerCreated()) {
            loadBlockers();
        }
    }

    private void openUpdateBlockerForm() {
        int selectedRow = blkTb.getSelectedRow();
        if (selectedRow >= 0) {
            String blockerId = (String) tM.getValueAt(selectedRow, 0);
            new UpdateBlockerForm(blockerId, this, blkS).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Select a blocker to update.", "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void deleteBlocker() {
        int selectedRow = blkTb.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Select a blocker to delete.");
            return;
        }
        if (selectedRow >= 0) {
            String blockerId = (String) tM.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(null,
                    "Are you sure you want to delete blocker " + blockerId + " and its associated solutions?",
                    "Delete Confirmation", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (blkS.deleteBlk(blockerId)) {
                    loadBlockers();
                    JOptionPane.showMessageDialog(null, "Blocker and associated solutions deleted successfully.");
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to delete blocker.");
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Select a blocker to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }
}
