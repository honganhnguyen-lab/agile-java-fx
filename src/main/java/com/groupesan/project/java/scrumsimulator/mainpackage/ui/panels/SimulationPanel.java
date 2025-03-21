package com.groupesan.project.java.scrumsimulator.mainpackage.ui.panels;

import com.groupesan.project.java.scrumsimulator.mainpackage.state.SimulationStateManager;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class SimulationPanel extends JFrame {
    private JButton startSimulationButton;
    private JButton stopSimulationButton;

    // Access the singleton instance of SimulationStateManager
    private final SimulationStateManager simulationStateManager = SimulationStateManager.getInstance();

    public SimulationPanel() {
        setTitle("Simulation");
        setSize(400, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1));

        startSimulationButton = new JButton("Start Simulation");
        stopSimulationButton = new JButton("Stop Simulation");
        stopSimulationButton.setVisible(false);

        startSimulationButton.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        simulationStateManager.startSimulation();
                        JOptionPane.showMessageDialog(null, "Simulation started!");
                        updateButtonVisibility();
                    }
                });

        stopSimulationButton.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        simulationStateManager.stopSimulation();
                        JOptionPane.showMessageDialog(null, "Simulation stopped!");
                        updateButtonVisibility();
                    }
                });

        panel.add(startSimulationButton);
        panel.add(stopSimulationButton);

        setLayout(new BorderLayout());
        add(panel, BorderLayout.CENTER);

        updateButtonVisibility();
    }

    private void updateButtonVisibility() {
        if (simulationStateManager.isRunning()) {
            stopSimulationButton.setVisible(true);
            startSimulationButton.setVisible(false);
        } else {
            stopSimulationButton.setVisible(false);
            startSimulationButton.setVisible(true);
        }
    }
}
