package com.groupesan.project.java.scrumsimulator.mainpackage.ui.panels;

import com.groupesan.project.java.scrumsimulator.mainpackage.state.UserStoryStateManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class DeleteUserStory extends JFrame {

    public DeleteUserStory() {
        init();
    }

    private void init() {
        setTitle("Delete User Story");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        placeComponents(panel);
        add(panel);

        setLocationRelativeTo(null);
    }

    private void placeComponents(JPanel panel) {
        panel.setLayout(null);

        JLabel userStoryLabel = new JLabel("Select User Story:");
        userStoryLabel.setBounds(10, 20, 120, 25);
        panel.add(userStoryLabel);

        List<String> userStories = UserStoryStateManager.getUserStories();
        JComboBox<String> userStoryComboBox = new JComboBox<>(userStories.toArray(new String[0]));
        userStoryComboBox.setBounds(150, 20, 200, 25);
        panel.add(userStoryComboBox);

        JButton deleteButton = new JButton("Delete");
        deleteButton.setBounds(150, 80, 150, 25);
        panel.add(deleteButton);

        deleteButton.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String selectedUserStory = (String) userStoryComboBox.getSelectedItem();

                        if (selectedUserStory != null) {
                            UserStoryStateManager.deleteUserStory(
                                    selectedUserStory);
                            JOptionPane.showMessageDialog(null, "User Story deleted successfully!");
                            dispose();
                        } else {
                            JOptionPane.showMessageDialog(
                                    null, "Please select a User Story");
                        }
                    }
                });
    }
}
