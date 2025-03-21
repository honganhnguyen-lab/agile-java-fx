package com.groupesan.project.java.scrumsimulator.mainpackage.ui.panels;

import com.groupesan.project.java.scrumsimulator.mainpackage.impl.BlockerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NewBlockerForm extends JDialog {
    private JSlider probabilitySlider;
    private JTextField nameFl;
    private JTextArea desc;
    private final BlockerFactory blkFactory;
    private boolean isBlkrCreated = false; // To track successful creation

    public NewBlockerForm(JFrame parent, BlockerFactory blkFactory) {
        super(parent, "Add New Blocker", true);
        this.blkFactory = blkFactory;

        setLayout(new BorderLayout());
        setSize(600, 450);
        setLocationRelativeTo(parent);
        
        JPanel inputPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel nameLabel = new JLabel("Blocker Name:");
        nameFl = new JTextField(20);
        inputPanel.add(nameLabel);
        inputPanel.add(nameFl);
        
        JLabel descLbl = new JLabel("Blocker Description:");
        desc = new JTextArea(4, 20);
        desc.setLineWrap(true);
        desc.setWrapStyleWord(true);
        inputPanel.add(descLbl);
        inputPanel.add(new JScrollPane(desc));
        
        JLabel probabilityLabel = new JLabel("Probability:");
        inputPanel.add(probabilityLabel);

        probabilitySlider = new JSlider(0, 100);
        probabilitySlider.setMajorTickSpacing(10);
        probabilitySlider.setPaintTicks(true);
        probabilitySlider.setPaintLabels(true);
        probabilitySlider.setValue(0);
        inputPanel.add(probabilitySlider);

        add(inputPanel, BorderLayout.CENTER);
        
        
        JPanel btnpnl = new JPanel(new FlowLayout());
        JButton addButton = new JButton("Add");
        addButton.addActionListener(new AddBlockerActionListener());
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dispose());

        btnpnl.add(addButton);
        btnpnl.add(cancelButton);

        add(btnpnl, BorderLayout.SOUTH);
    }

    private class AddBlockerActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String name = nameFl.getText().trim();
            String description = desc.getText().trim();
            int probability = probabilitySlider.getValue();

            if (name.isEmpty() || description.isEmpty()) {
                JOptionPane.showMessageDialog(NewBlockerForm.this, "Please fill out all fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
            } else {

                blkFactory.createBlocker(name, description, probability);
                isBlkrCreated = true;
                dispose();
            }
        }
    }

    public boolean isBlockerCreated() {
        return isBlkrCreated;
    }
}
