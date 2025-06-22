package pl.edu.wit.studentManagement;

import pl.edu.wit.studentManagement.view.AppWindow;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            var mainWindow = new AppWindow();
            mainWindow.show();
        });
    }
}