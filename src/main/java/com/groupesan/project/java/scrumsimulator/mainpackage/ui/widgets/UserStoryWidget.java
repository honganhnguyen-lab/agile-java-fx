package com.groupesan.project.java.scrumsimulator.mainpackage.ui.widgets;

import com.groupesan.project.java.scrumsimulator.mainpackage.impl.UserStory;
import com.groupesan.project.java.scrumsimulator.mainpackage.ui.panels.EditUserStoryForm;
import com.groupesan.project.java.scrumsimulator.mainpackage.utils.CustomConstraints;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class UserStoryWidget extends JPanel implements BaseComponent {

    JLabel id;
    JLabel points;
    JLabel name;
    JLabel desc;
    JLabel bvp;
    JLabel status;

    private UserStory userStory;

    ActionListener actionListener = e -> {};

    MouseAdapter openEditDialog =
            new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    EditUserStoryForm form = new EditUserStoryForm(userStory);
                    form.setVisible(true);

                    form.addWindowListener(
                            new java.awt.event.WindowAdapter() {
                                public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                                    init();
                                }
                            });
                }
            };

    public UserStoryWidget(UserStory userStory) {
        this.userStory = userStory;

        this.init();
    }

    public void init() {
        removeAll();

        id = new JLabel(userStory.getId().toString());
        id.addMouseListener(openEditDialog);
        points = new JLabel(Double.toString(userStory.getPointValue()));
        points.addMouseListener(openEditDialog);
        name = new JLabel(userStory.getName());
        name.addMouseListener(openEditDialog);
        desc = new JLabel(userStory.getDescription());
        desc.addMouseListener(openEditDialog);
        bvp = new JLabel(userStory.getBusinessValue().toString());
        bvp.addMouseListener(openEditDialog);
        status = new JLabel(userStory.getStoryStatus().toString());
        status.addMouseListener(openEditDialog);
        GridBagLayout myGridBagLayout = new GridBagLayout();

        setLayout(myGridBagLayout);

        add(
                id,
                new CustomConstraints(
                        0, 0, GridBagConstraints.WEST, 0.1, 0.0, GridBagConstraints.HORIZONTAL));
        add(
                points,
                new CustomConstraints(
                        1, 0, GridBagConstraints.WEST, 0.1, 0.0, GridBagConstraints.HORIZONTAL));
        add(
                name,
                new CustomConstraints(
                        2, 0, GridBagConstraints.WEST, 0.2, 0.0, GridBagConstraints.HORIZONTAL));
        add(
                desc,
                new CustomConstraints(
                        3, 0, GridBagConstraints.WEST, 0.7, 0.0, GridBagConstraints.HORIZONTAL));
        add(
                bvp,
                new CustomConstraints(
                        4, 0, GridBagConstraints.WEST, 0.7, 0.0, GridBagConstraints.HORIZONTAL));
        add(
                status,
                new CustomConstraints(
                        5, 0, GridBagConstraints.WEST, 0.4, 0.0, GridBagConstraints.HORIZONTAL));
        revalidate();
        repaint();
    }
}
