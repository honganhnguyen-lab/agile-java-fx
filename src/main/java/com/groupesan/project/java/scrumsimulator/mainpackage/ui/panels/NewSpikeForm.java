package com.groupesan.project.java.scrumsimulator.mainpackage.ui.panels;

import com.groupesan.project.java.scrumsimulator.mainpackage.impl.Spike;
import com.groupesan.project.java.scrumsimulator.mainpackage.impl.SpikeFactory;
import com.groupesan.project.java.scrumsimulator.mainpackage.impl.SpikeStore;
import com.groupesan.project.java.scrumsimulator.mainpackage.state.UserStoryStateManager;
import com.groupesan.project.java.scrumsimulator.mainpackage.ui.widgets.BaseComponent;
import com.groupesan.project.java.scrumsimulator.mainpackage.utils.CustomConstraints;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class NewSpikeForm extends JFrame implements BaseComponent {

    Integer[] timeBoxDuration = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14 };

    public NewSpikeForm() {
        this.init();
    }

    private JTextField spikeTitleField = new JTextField();
    private JTextArea spikeDescArea = new JTextArea();
    private JTextArea spikeOutcomeArea = new JTextArea();
    private JComboBox<Integer> timeBoxDurationField = new JComboBox<>(timeBoxDuration);
    List<String> userStories = UserStoryStateManager.getUserStories();
    JComboBox<String> userStoryComboBox = new JComboBox<>(userStories.toArray(new String[0]));

    public void init() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Create New Spike");
        setSize(400, 300);

        spikeTitleField = new JTextField();
        spikeDescArea = new JTextArea();
        spikeOutcomeArea = new JTextArea();
        timeBoxDurationField = new JComboBox<>(timeBoxDuration);

        GridBagLayout myGridbagLayout = new GridBagLayout();
        JPanel myJpanel = new JPanel();
        myJpanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        myJpanel.setLayout(myGridbagLayout);

        BorderLayout myBorderLayout = new BorderLayout();

        setLayout(myBorderLayout);

        JLabel spikeNameLabel = new JLabel("Spike Name:");
        myJpanel.add(
                spikeNameLabel,
                new CustomConstraints(
                        0, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL));
        myJpanel.add(
                spikeTitleField,
                new CustomConstraints(
                        1, 0, GridBagConstraints.EAST, 1.0, 0.0,
                        GridBagConstraints.HORIZONTAL));

        JLabel spikeDescLabel = new JLabel("Description:");
        myJpanel.add(
                spikeDescLabel,
                new CustomConstraints(
                        0, 1, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL));
        myJpanel.add(
                new JScrollPane(spikeDescArea),
                new CustomConstraints(
                        1, 1, GridBagConstraints.EAST, 1.0, 0.3, GridBagConstraints.BOTH));

        JLabel spikeOutcomeLabel = new JLabel("Expected Outcome:");
        myJpanel.add(
                spikeOutcomeLabel,
                new CustomConstraints(
                        0, 2, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL));
        myJpanel.add(
                new JScrollPane(spikeOutcomeArea),
                new CustomConstraints(
                        1, 2, GridBagConstraints.EAST, 1.0, 0.3, GridBagConstraints.BOTH));

        JLabel timeBoxLabel = new JLabel("Timebox Duration:");
        myJpanel.add(
                timeBoxLabel,
                new CustomConstraints(
                        0, 3, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL));
        myJpanel.add(
                timeBoxDurationField,
                new CustomConstraints(
                        1, 3, GridBagConstraints.EAST, 1.0, 0.0,
                        GridBagConstraints.HORIZONTAL));

        JLabel userStoryLabel = new JLabel("Linked User Story:");
        userStoryLabel.setBounds(10, 20, 120, 25);
        myJpanel.add(userStoryLabel, new CustomConstraints(
                0, 4, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL));

        userStoryComboBox.setBounds(150, 20, 200, 25);
        myJpanel.add(userStoryComboBox, new CustomConstraints(
                1, 4, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL));

        JButton cancelButton = new JButton("Cancel");

        cancelButton.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        dispose();
                    }
                });

        JButton submitButton = new JButton("Submit");

        submitButton.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        getSpikeObject();
                        dispose();
                    }
                });

        myJpanel.add(cancelButton, new CustomConstraints(0, 6, GridBagConstraints.EAST, GridBagConstraints.NONE));
        myJpanel.add(submitButton, new CustomConstraints(1, 6, GridBagConstraints.WEST, GridBagConstraints.NONE));

        add(myJpanel);
    }

    public Spike getSpikeObject() {
        String spikeName = spikeTitleField.getText();
        String description = spikeDescArea.getText();
        String spikeOutcome = spikeOutcomeArea.getText();
        String selectedUserStory = (String) userStoryComboBox.getSelectedItem();
        Integer timeBoxDuration = (Integer) timeBoxDurationField.getSelectedItem();
        String status = "new";

        SpikeFactory spikeFactory = SpikeFactory.getInstance();

        Spike spike = spikeFactory.createNewSpike(spikeName, description, spikeOutcome, timeBoxDuration, status,
                selectedUserStory);

        spike.doRegister();

        SpikeStore.getInstance().addSpike(spike);
        return spike;
    }
}