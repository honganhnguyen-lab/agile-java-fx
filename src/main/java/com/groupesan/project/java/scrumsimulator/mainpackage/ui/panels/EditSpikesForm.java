package com.groupesan.project.java.scrumsimulator.mainpackage.ui.panels;

import com.groupesan.project.java.scrumsimulator.mainpackage.impl.Spike;
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

public class EditSpikesForm extends JFrame implements BaseComponent {

    String[] statusList = { "new", "In Research", "Blocked", "In Documentation", "Review", "Completed",
            "Cancelled" };
    Integer[] timeBoxDuration = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14 };
    private Spike spike;

    public EditSpikesForm(Spike spike) {
        this.spike = spike;
        this.init();
    }

    private JTextField spikeTitleField = new JTextField();
    private JTextArea spikeDescArea = new JTextArea();
    private JTextArea spikeOutcomeArea = new JTextArea();
    private JComboBox<Integer> timeBoxDurationField = new JComboBox<>(timeBoxDuration);
    List<String> userStories = UserStoryStateManager.getUserStories();
    JComboBox<String> userStoryComboBox = new JComboBox<>(userStories.toArray(new String[0]));
    private JComboBox<String> statusComboBox = new JComboBox<>(statusList);

    public void init() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Edit Spike");
        setSize(400, 300);

        spikeTitleField.setText(spike.getName());
        spikeDescArea.setText(spike.getDescription());
        spikeOutcomeArea.setText(spike.getExpectedOutcome());
        userStoryComboBox.setSelectedItem(spike.getUserStory());
        timeBoxDurationField.setSelectedItem(spike.getTimeBoxDuration());
        statusComboBox.setSelectedItem(spike.getSpikeStatus());

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

        JLabel statusLabel = new JLabel("Status:");
        statusLabel.setBounds(10, 20, 120, 25);
        myJpanel.add(statusLabel, new CustomConstraints(
                0, 5, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL));
        statusComboBox.setBounds(150, 20, 200, 25);
        myJpanel.add(statusComboBox, new CustomConstraints(
                1, 5, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL));

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
                        String spikeName = spikeTitleField.getText();
                        String description = spikeDescArea.getText();
                        String spikeOutcome = spikeOutcomeArea.getText();
                        String selectedUserStory = (String) userStoryComboBox.getSelectedItem();
                        Integer timeBoxDuration = (Integer) timeBoxDurationField.getSelectedItem();
                        String status = (String) statusComboBox.getSelectedItem();

                        spike.setName(spikeName);
                        spike.setDescription(description);
                        spike.setExpectedOutcome(spikeOutcome);
                        spike.setTimeBoxDuration(timeBoxDuration);
                        spike.setSpikeStatus(status);
                        spike.setUserStory(selectedUserStory);
                        dispose();
                    }
                });

        myJpanel.add(cancelButton,
                new CustomConstraints(0, 6, GridBagConstraints.EAST, GridBagConstraints.NONE));
        myJpanel.add(submitButton,
                new CustomConstraints(1, 6, GridBagConstraints.WEST, GridBagConstraints.NONE));

        add(myJpanel);
    }
}