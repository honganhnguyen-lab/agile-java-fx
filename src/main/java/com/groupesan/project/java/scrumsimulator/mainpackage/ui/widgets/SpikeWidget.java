package com.groupesan.project.java.scrumsimulator.mainpackage.ui.widgets;

import com.groupesan.project.java.scrumsimulator.mainpackage.impl.Spike;
import com.groupesan.project.java.scrumsimulator.mainpackage.ui.panels.EditSpikesForm;
import com.groupesan.project.java.scrumsimulator.mainpackage.utils.CustomConstraints;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.ActionListener;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class SpikeWidget extends JPanel implements BaseComponent {

    JLabel spikeName;
    JLabel description;
    JLabel spikeOutcome;
    JLabel selectedUserStory;
    JLabel timeBoxDuration;
    JLabel status;

    private Boolean value = false;
    private transient Spike spike;

    ActionListener actionListener = e -> {
    };

    transient MouseAdapter openEditDialog = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (value) {

                EditSpikesForm form = new EditSpikesForm(spike);
                form.setVisible(true);

                form.addWindowListener(
                        new java.awt.event.WindowAdapter() {
                            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                                init();
                            }
                        });
            }
        }
    };

    public SpikeWidget(Spike spike, Boolean value) {
        this.value = value;
        this.spike = spike;
        this.init();
    }

    public void init() {
        removeAll();

        spikeName = new JLabel(spike.getName());
        spikeName.addMouseListener(openEditDialog);
        description = new JLabel(spike.getDescription());
        description.addMouseListener(openEditDialog);
        spikeOutcome = new JLabel(spike.getExpectedOutcome());
        spikeOutcome.addMouseListener(openEditDialog);
        selectedUserStory = new JLabel(spike.getUserStory());
        selectedUserStory.addMouseListener(openEditDialog);
        timeBoxDuration = new JLabel(Integer.toString(spike.getTimeBoxDuration()));
        timeBoxDuration.addMouseListener(openEditDialog);
        status = new JLabel(spike.getSpikeStatus().toString());
        status.addMouseListener(openEditDialog);
        GridBagLayout myGridBagLayout = new GridBagLayout();

        setLayout(myGridBagLayout);

        add(
                spikeName,
                new CustomConstraints(
                        0, 0, GridBagConstraints.WEST, 0.1, 0.0,
                        GridBagConstraints.HORIZONTAL));
        add(
                description,
                new CustomConstraints(
                        1, 0, GridBagConstraints.WEST, 0.1, 0.0,
                        GridBagConstraints.HORIZONTAL));
        add(
                spikeOutcome,
                new CustomConstraints(
                        2, 0, GridBagConstraints.WEST, 0.2, 0.0,
                        GridBagConstraints.HORIZONTAL));
        add(
                selectedUserStory,
                new CustomConstraints(
                        3, 0, GridBagConstraints.WEST, 0.7, 0.0,
                        GridBagConstraints.HORIZONTAL));
        add(
                timeBoxDuration,
                new CustomConstraints(
                        4, 0, GridBagConstraints.WEST, 0.7, 0.0,
                        GridBagConstraints.HORIZONTAL));
        add(
                status,
                new CustomConstraints(
                        5, 0, GridBagConstraints.WEST, 0.4, 0.0,
                        GridBagConstraints.HORIZONTAL));
        revalidate();
        repaint();
    }
}
