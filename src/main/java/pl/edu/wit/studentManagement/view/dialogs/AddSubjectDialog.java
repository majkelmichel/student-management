package pl.edu.wit.studentManagement.view.dialogs;

import javax.swing.*;
import java.awt.*;

public class AddSubjectDialog {
    public static class SubjectData {
        public final String name;
        public SubjectData(String name) {
            this.name = name;
        }
    }

    public SubjectData showDialog(Component parent) {
        JTextField nameField = new JTextField();
        nameField.setPreferredSize(new Dimension(220, 28));

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Nazwa przedmiotu:"), gbc);
        gbc.gridx = 1;
        panel.add(nameField, gbc);

        int result = JOptionPane.showConfirmDialog(parent, panel, "Dodaj przedmiot", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim();
            if (!name.isEmpty()) {
                return new SubjectData(name);
            }
        }
        return null;
    }
}
