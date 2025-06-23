package pl.edu.wit.studentManagement.view.fragments;

import pl.edu.wit.studentManagement.translations.Translator;
import pl.edu.wit.studentManagement.view.AppWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Fragment for displaying the dashboard in the application
 *
 * @author Wojciech Berdowski
 */
public class DashboardFragment {
    private final JPanel panel;

    private static final Color studentColor = Color.decode("#8fc2f2");
    private static final Color groupColor = Color.decode("#f2d68f");
    private static final Color subjectColor = Color.decode("#8ff29b");
    private static final Color gradeColor = Color.decode("#d0a6e3");
    private static final Font bigButtonFont = new Font("SansSerif", Font.BOLD, 24);


    public DashboardFragment() {
        panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));

        JButton studentButton = addBigButton(
                Translator.translate("students"),
                studentColor,
                loadIcon("icons/student.png"),
                e -> AppWindow.navigateToStudents()
        );
        buttonsPanel.add(studentButton);

        JButton groupButton = addBigButton(
                Translator.translate("groups"),
                groupColor,
                loadIcon("icons/group.png"),
                e -> AppWindow.navigateToGroups()
        );
        buttonsPanel.add(groupButton);

        JButton subjectButton = addBigButton(
                Translator.translate("subjects"),
                subjectColor,
                loadIcon("icons/subject.png"),
                e -> AppWindow.navigateToSubjects()
        );
        buttonsPanel.add(subjectButton);

        JButton gradesButton = addBigButton(
                Translator.translate("grades"),
                gradeColor,
                loadIcon("icons/grade.png"),
                e -> AppWindow.navigateToGrades()
        );
        buttonsPanel.add(gradesButton);

        panel.add(buttonsPanel, BorderLayout.CENTER);
    }

    private JButton addBigButton(String text, Color color, Icon icon, ActionListener onClick) {
        JButton button = new JButton(text, icon);
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setVerticalTextPosition(SwingConstants.BOTTOM);
        button.setPreferredSize(new Dimension(200, 200));
        button.setBackground(color);
        button.setFont(bigButtonFont);
        button.addActionListener(onClick);
        return button;
    }

    private Icon loadIcon(String path) {
        var url = getClass().getClassLoader().getResource(path);
        if (url == null) return null;
        ImageIcon icon = new ImageIcon(url);
        Image scaledImage = icon.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }

    public JPanel getPanel() {
        return panel;
    }
}