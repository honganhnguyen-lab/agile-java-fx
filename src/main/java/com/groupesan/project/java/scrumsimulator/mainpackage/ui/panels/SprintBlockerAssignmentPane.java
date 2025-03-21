package com.groupesan.project.java.scrumsimulator.mainpackage.ui.panels;

import com.groupesan.project.java.scrumsimulator.mainpackage.impl.Blocker1;
import com.groupesan.project.java.scrumsimulator.mainpackage.impl.BlockerStore;
import com.groupesan.project.java.scrumsimulator.mainpackage.impl.Sprint;
import com.groupesan.project.java.scrumsimulator.mainpackage.impl.SprintStore;
import com.groupesan.project.java.scrumsimulator.mainpackage.ui.utils.WrappedCellRenderer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class SprintBlockerAssignmentPane extends JFrame {
    private Sprint activeSprt;
    private BlockerStore blkrSt;
    private JTable blkrTb;
    private DefaultTableModel tbM;
    private List<Blocker1> assignedBlkrs;
    private JComboBox<Sprint> spDropdn;
    private static final String SP_BLOCKER = "src/main/resources/sprint_blockers.json";

    public SprintBlockerAssignmentPane(BlockerStore blkrSt) {
        this.blkrSt = blkrSt;
        this.assignedBlkrs = new ArrayList<>();

        setTitle("Sprint Blocker Assignment");
        setSize(1200, 700);
        setLayout(new BorderLayout());

        JPanel tpPnl = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel selectSpLbl = new JLabel("Select Active Sprint:");
        spDropdn = new JComboBox<>();
        populateSpDropdown();
        spDropdn.addActionListener(e -> onSpSelectionChanged());
        tpPnl.add(selectSpLbl);
        tpPnl.add(spDropdn);
        add(tpPnl, BorderLayout.NORTH);

        tbM = new DefaultTableModel(new Object[]{"ID", "Name", "Description", "Assign"}, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 3) {
                    return Boolean.class;
                }
                return super.getColumnClass(columnIndex);
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3;
            }
        };

        blkrTb = new JTable(tbM);
        loadBlkrsForActiveSp();

        blkrTb.getColumnModel().getColumn(3).setCellRenderer(blkrTb.getDefaultRenderer(Boolean.class));


        DefaultTableCellRenderer cntrRenderer = new DefaultTableCellRenderer();
        cntrRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        blkrTb.getColumnModel().getColumn(0).setCellRenderer(cntrRenderer);
        blkrTb.getColumnModel().getColumn(1).setCellRenderer(new WrappedCellRenderer());
        blkrTb.getColumnModel().getColumn(2).setCellRenderer(new WrappedCellRenderer());


        blkrTb.setRowHeight(80);

        JScrollPane tbScrollPane = new JScrollPane(blkrTb);
        add(tbScrollPane, BorderLayout.CENTER);

        JPanel btnPnl = new JPanel(new FlowLayout());

        JButton saveBtn = new JButton("Save Assignments");
        saveBtn.addActionListener(e -> saveAssignments());
        btnPnl.add(saveBtn);

        JButton viewBtn = new JButton("View Assigned Blockers");
        viewBtn.addActionListener(e -> viewAssigndBlkrs());
        btnPnl.add(viewBtn);

        add(btnPnl, BorderLayout.SOUTH);
    }

    private void populateSpDropdown() {
        List<Sprint> sprints = SprintStore.getInstance().getSprints();
        for (Sprint sprint : sprints) {
            spDropdn.addItem(sprint);
        }
        activeSprt = SprintStore.getInstance().getActiveSprint();
        if (activeSprt != null) {
            spDropdn.setSelectedItem(activeSprt);
        }
    }

    private void loadBlkrsForActiveSp() {
        tbM.setRowCount(0);
        List<Blocker1> blkrs = blkrSt.getBlK();
        for (Blocker1 blkr : blkrs) {
            tbM.addRow(new Object[]{blkr.getBId(), blkr.getBName(), blkr.getDesc(), Boolean.FALSE});
        }
    }


    private void onSpSelectionChanged() {
        Sprint selectedSp = (Sprint) spDropdn.getSelectedItem();
        if (selectedSp != null) {
            SprintStore.getInstance().setActiveSprint(selectedSp.getId());
            activeSprt = selectedSp;
            loadBlkrsForActiveSp();
        }
    }

    private void saveAssignments() {
        assignedBlkrs.clear();
        for (int i = 0; i < blkrTb.getRowCount(); i++) {
            Boolean isAssigned = (Boolean) blkrTb.getValueAt(i, 3);
            if (Boolean.TRUE.equals(isAssigned)) {
                String blockerId = (String) blkrTb.getValueAt(i, 0);
                Blocker1 blkr = blkrSt.getBlKById(blockerId);
                if (blkr != null) {
                    assignedBlkrs.add(blkr);
                }
            }
        }
        saveAssignedBlockersToSprint(activeSprt.getId(), assignedBlkrs);
        JOptionPane.showMessageDialog(this, "Blockers assigned to Sprint " + activeSprt.getName() + " have been saved.");
    }

    private void viewAssigndBlkrs() {
        if (activeSprt == null) {
            JOptionPane.showMessageDialog(this, "No active sprint selected.", "View Assigned Blockers", JOptionPane.WARNING_MESSAGE);
            return;
        }

        StringBuilder assignedBlkrsInfo = new StringBuilder("Assigned Blockers for Sprint: " + activeSprt.getName() + "\n");
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            File file = new File(SP_BLOCKER);
            if (file.exists()) {
                JsonNode rtNode = objectMapper.readTree(file);
                JsonNode spIdNode = rtNode.path("sprintId");
                JsonNode blkrNode = rtNode.path("blockers");
                if (spIdNode.asLong() == activeSprt.getId()) {
                    for (JsonNode blkNode : blkrNode) {
                        assignedBlkrsInfo.append("ID: ").append(blkNode.path("id").asText())
                                .append(", Name: ").append(blkNode.path("name").asText())
                                .append(", Description: ").append(blkNode.path("description").asText()).append("\n");
                    }
                } else {
                    assignedBlkrsInfo.append("No blockers assigned to this sprint.");
                }
            } else {
                assignedBlkrsInfo.append("No saved data available.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error reading assigned blockers file.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        JOptionPane.showMessageDialog(this, assignedBlkrsInfo.toString(), "Assigned Blockers", JOptionPane.INFORMATION_MESSAGE);
    }

    private void saveAssignedBlockersToSprint(long sprintId, List<Blocker1> assignedBlkrs) {
        File file = new File(SP_BLOCKER);
        try (FileOutputStream fos = new FileOutputStream(file);
             OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
             BufferedWriter writer = new BufferedWriter(osw)) {
            writer.write("{\n");
            writer.write("  \"sprintId\": " + sprintId + ",\n");
            writer.write("  \"blockers\": [\n");
            for (int i = 0; i < assignedBlkrs.size(); i++) {
                Blocker1 blkr = assignedBlkrs.get(i);
                writer.write("    {\n");
                writer.write("      \"id\": \"" + blkr.getBId() + "\",\n");
                writer.write("      \"name\": \"" + blkr.getBName() + "\",\n");
                writer.write("      \"description\": \"" + blkr.getDesc() + "\",\n");
                writer.write("      \"probability\": " + blkr.getProbability() + "\n");
                if (i < assignedBlkrs.size() - 1) {
                    writer.write("    },\n");
                } else {
                    writer.write("    }\n");
                }
            }
            writer.write("  ]\n");
            writer.write("}\n");
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving blockers", "Save Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
