package pl.edu.wit.studentManagement.view;

import pl.edu.wit.studentManagement.view.fragments.*;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;

/**
 * Main application window for the student management system.
 * @author Wojciech Berdowski
 */
public final class AppWindow {
    private static JFrame frame;
    private static JPanel contentPanel;
    private static JLabel topBarHeader;
    private static JButton backButton;
    private static Runnable backButtonAction;

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
            }
        }

        frame = new JFrame("Aplikacja do zarządzania studentami");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1280, 720);
        frame.setLocationRelativeTo(null);


        var mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(createTopBar(), BorderLayout.NORTH);

        contentPanel = new JPanel(new BorderLayout());
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        frame.setContentPane(mainPanel);
    }

    public void show() {
        navigateToDashboard();
        frame.setVisible(true);
    }

    private JPanel createTopBar(){
        var topBar = new JPanel(new BorderLayout());

        var leftPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));

        backButton = new JButton("← Powrót");
        backButton.addActionListener(e -> backButtonAction.run());
        leftPanel.add(backButton);

        topBarHeader = new JLabel("-");
        topBarHeader.setFont(new Font("SansSerif", Font.BOLD, 22));
        leftPanel.add(topBarHeader);

        topBar.add(leftPanel, BorderLayout.WEST);

        var rightButtonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));

        var langButton = new JButton("PL");
        langButton.addActionListener(e -> {

        });
        rightButtonsPanel.add(langButton);

        var aboutButton = new JButton("O programie");
        aboutButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame,
                    "Program do zarządzania studentami\nWersja 1.0\nAutorzy: ...",
                    "O programie",
                    JOptionPane.INFORMATION_MESSAGE);
        });
        rightButtonsPanel.add(aboutButton);

        topBar.add(rightButtonsPanel, BorderLayout.EAST);

        return topBar;
    }

    private static void setContent(JComponent newContent) {
        contentPanel.removeAll();
        contentPanel.add(newContent, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private  static void setTitle(String title) {
        topBarHeader.setText(title);
    }

    private static void setBackButtonAction(Runnable action){
        if(action != null) {
            backButtonAction = action;
            backButton.setVisible(true);
            return;
        }

        backButton.setVisible(false);
    }

    public static void navigateToDashboard() {
        setContent(new DashboardFragment().getPanel());
        setTitle("Panel startowy");
        setBackButtonAction(null);
    }

    public static void navigateToStudents() {
        setContent(new StudentsFragment().getPanel());
        setTitle("Studenci");
        setBackButtonAction(AppWindow::navigateToDashboard);
    }

    public static void navigateToGroups() {
        setContent(new GroupsFragment().getPanel());
        setTitle("Grupy");
        setBackButtonAction(AppWindow::navigateToDashboard);
    }

    public static void navigateToSubjects() {
        setContent(new SubjectsFragment().getPanel());
        setTitle("Przedmioty");
        setBackButtonAction(AppWindow::navigateToDashboard);
    }

    public static void navigateToGrades() {
        setContent(new GradesFragment().getPanel());
        setTitle("Oceny");
        setBackButtonAction(AppWindow::navigateToDashboard);
    }
}
