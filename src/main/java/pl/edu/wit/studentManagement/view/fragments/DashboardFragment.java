package pl.edu.wit.studentManagement.view.fragments;

import pl.edu.wit.studentManagement.view.AppWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class DashboardFragment {
    private final JPanel panel;

    private static final Color studentColor = Color.decode("#58A3E9");
    private static final Color groupColor = Color.decode("#F3C033");
    private static final Color subjectColor = Color.decode("#55D54E");
    private static final Font bigButtonFont = new Font("SansSerif", Font.BOLD, 24);


    public DashboardFragment() {
        panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));

        JButton studentButton = addBigButton(
                "Studenci",
                studentColor,
                e -> AppWindow.navigateToStudents()
        );
        panel.add(studentButton);

        JButton groupButton = addBigButton(
                "Grupy",
                groupColor,
                e -> AppWindow.navigateToGroups()
        );
        panel.add(groupButton);

        JButton subjectButton = addBigButton(
                "Przedmioty",
                subjectColor,
                e -> AppWindow.navigateToSubjects()
        );
        panel.add(subjectButton);
    }

    private JButton addBigButton(String text, Color color, ActionListener onClick) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(200, 200));
        button.setBackground(color);
        button.setFont(bigButtonFont);
        button.addActionListener(onClick);
        return button;
    }

    public JPanel getPanel() {
        return panel;
    }
}
