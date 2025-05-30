package pl.edu.wit.studentManagement.view;

import pl.edu.wit.studentManagement.view.fragments.DashboardFragment;
import pl.edu.wit.studentManagement.view.fragments.StudentsFragment;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;

public final class AppWindow {
    private static JFrame frame;

    public AppWindow() {
        try {
            // Ustaw FlatLaf jako domyślny motyw
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            // Jeśli FlatLaf niedostępny, spróbuj Nimbus
            try {
                for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (Exception ex) {
                // Jeśli Nimbus niedostępny, zostanie domyślny
            }
        }

        frame = new JFrame("Aplikacja do zarządzania studentami");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1280, 720);
        frame.setLocationRelativeTo(null);
    }

    public void show() {
        navigateToDashboard();
        frame.setVisible(true);
    }

    public static void navigateToDashboard() {
        frame.setContentPane(new DashboardFragment().getPanel());
        frame.revalidate();
        frame.repaint();
    }

    public static void navigateToStudents() {
        frame.setContentPane(new StudentsFragment().getPanel());
        frame.revalidate();
        frame.repaint();
    }

    public static void navigateToGroups() {
//        frame.setContentPane(new StudentsFragment().getPanel());
//        frame.revalidate();
//        frame.repaint();
    }

    public static void navigateToSubjects() {
//        frame.setContentPane(new StudentsFragment().getPanel());
//        frame.revalidate();
//        frame.repaint();
    }
}
