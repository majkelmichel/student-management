package pl.edu.wit.studentManagement.view.dialogs;

import pl.edu.wit.studentManagement.translations.Translator;

import javax.swing.*;
import java.awt.*;

/**
 * Dialog for editing a grade criterion.
 *
 * @author Wojciech Berdowski
 */
public class EditGradeCriterionDialog {
    public static String[] showDialog(Component parent, String currentName, String currentPoints) {
        JTextField nameField = new JTextField(currentName);
        JTextField pointsField = new JTextField(currentPoints);

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel(Translator.translate("gradeCriterion.name") + ":"));
        panel.add(nameField);
        panel.add(new JLabel(Translator.translate("gradeCriterion.maxPoints") + ":"));
        panel.add(pointsField);

        int result = JOptionPane.showConfirmDialog(
                parent,
                panel,
                Translator.translate("gradeCriterion.edit"),
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

