package pl.edu.wit.studentManagement.view;

import pl.edu.wit.studentManagement.translations.Language;
import pl.edu.wit.studentManagement.translations.Translator;
import pl.edu.wit.studentManagement.view.fragments.*;
import com.formdev.flatlaf.FlatLightLaf;
import pl.edu.wit.studentManagement.view.interfaces.Fragment;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Main application window for the student management system.
 *
 * @author Wojciech Berdowski
 */
public final class AppWindow {
    private static JFrame frame;
    private static JPanel contentPanel;
    private static JLabel topBarHeader;
    private static JButton backButton;
    private static Runnable backButtonAction;
    private static Class<? extends Fragment> currentFragment;
    private JButton langButton;
    private JButton aboutButton;

    public AppWindow() {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            try {
                for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        frame = new JFrame(Translator.translate("app.title"));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1280, 720);
        frame.setLocationRelativeTo(null);

        var mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(createTopBar(), BorderLayout.NORTH);

        contentPanel = new JPanel(new BorderLayout());
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        frame.setContentPane(mainPanel);
    }

    private static void showAboutDialog(ActionEvent e) {
        JOptionPane.showMessageDialog(frame,
                Translator.translate("about.description"),
                Translator.translate("about"),
                JOptionPane.INFORMATION_MESSAGE);
    }

    public void show() {
        navigateToDashboard();
        frame.setVisible(true);
    }

    private JPanel createTopBar() {
        var topBar = new JPanel(new BorderLayout());

        var leftPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));

        backButton = new JButton(Translator.translate("back"));
        backButton.addActionListener(e -> backButtonAction.run());
        leftPanel.add(backButton);

        topBarHeader = new JLabel("-");
        topBarHeader.setFont(new Font("SansSerif", Font.BOLD, 22));
        leftPanel.add(topBarHeader);

        topBar.add(leftPanel, BorderLayout.WEST);

        var rightButtonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));

        var selectedLanguage = Translator.getLanguage().name();
        langButton = new JButton(selectedLanguage);
        langButton.addActionListener(e -> switchLanguage());
        rightButtonsPanel.add(langButton);

        aboutButton = new JButton(Translator.translate("about"));
        aboutButton.addActionListener(AppWindow::showAboutDialog);
        rightButtonsPanel.add(aboutButton);

        topBar.add(rightButtonsPanel, BorderLayout.EAST);

        return topBar;
    }

    private void updateTexts() {
        frame.setTitle(Translator.translate("app.title"));
        backButton.setText(Translator.translate("back"));
        aboutButton.setText(Translator.translate("about"));
        langButton.setText(Translator.getLanguage().name());
    }

    private void switchLanguage() {
        var selectedLanguage = Translator.getLanguage();
        var newLanguage = selectedLanguage == Language.ENGLISH ? Language.POLISH : Language.ENGLISH;
        Translator.setLanguage(newLanguage);
        updateTexts();

        if( currentFragment != null) {
            setFragment(currentFragment);
        }
    }

    private static void setFragment(Class<? extends Fragment> fragment) {
        if (fragment == null) {
            return;
        }
        try {
            var newContent = fragment.getDeclaredConstructor().newInstance().getPanel();
            contentPanel.removeAll();
            contentPanel.add(newContent, BorderLayout.CENTER);
            contentPanel.revalidate();
            contentPanel.repaint();
            currentFragment = fragment;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void setTitle(String title) {
        topBarHeader.setText(title);
    }

    private static void setBackButtonAction(Runnable action) {
        if (action != null) {
            backButtonAction = action;
            backButton.setVisible(true);
            return;
        }

        backButton.setVisible(false);
    }

    public static void navigateToDashboard() {
        setFragment(DashboardFragment.class);
        setTitle(Translator.translate("home.panel"));
        setBackButtonAction(null);
    }

    public static void navigateToStudents() {
        setFragment(StudentsFragment.class);
        setTitle(Translator.translate("students"));
        setBackButtonAction(AppWindow::navigateToDashboard);
    }

    public static void navigateToGroups() {
        setFragment(GroupsFragment.class);
        setTitle(Translator.translate("groups"));
        setBackButtonAction(AppWindow::navigateToDashboard);
    }

    public static void navigateToSubjects() {
        setFragment(SubjectsFragment.class);
        setTitle(Translator.translate("subjects"));
        setBackButtonAction(AppWindow::navigateToDashboard);
    }

    public static void navigateToGrades() {
        setFragment(GradesFragment.class);
        setTitle(Translator.translate("grades"));
        setBackButtonAction(AppWindow::navigateToDashboard);
    }
}
