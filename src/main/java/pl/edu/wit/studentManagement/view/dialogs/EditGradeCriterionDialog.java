package pl.edu.wit.studentManagement.view.dialogs;

import javax.swing.*;
import java.awt.*;

/**
 * Dialog do edycji istniejącego kryterium oceniania (nazwa i maksymalna liczba punktów).
 *
 * @author Wojciech Berdowski
 */
public class EditGradeCriterionDialog {
    public static String[] showDialog(Component parent, String currentName, String currentPoints) {
        JTextField nameField = new JTextField(currentName);
        JTextField pointsField = new JTextField(currentPoints);

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Nazwa kryterium:"));
        panel.add(nameField);
        panel.add(new JLabel("Maksymalna liczba punktów:"));
        panel.add(pointsField);

        int result = JOptionPane.showConfirmDialog(
                parent,
                panel,
                "Edytuj kryterium oceniania",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim();
            String points = pointsField.getText().trim();
            return new String[]{name, points};
        }
        return null;
    }
}

