package pl.edu.wit.studentManagement.view.dialogs;

import javax.swing.*;
import java.awt.*;

/**
 * Dialog for adding a new grade criterion with a name and maximum points.
 *
 * @author Wojciech Berdowski
 */
public class AddGradeCriterionDialog {
    public static String[] showDialog(Component parent) {
        JTextField nameField = new JTextField();
        JTextField pointsField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Nazwa kryterium:"));
        panel.add(nameField);
        panel.add(new JLabel("Maksymalna liczba punktów:"));
        panel.add(pointsField);

        int result = JOptionPane.showConfirmDialog(
                parent,
                panel,
                "Dodaj kryterium oceniania",
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
